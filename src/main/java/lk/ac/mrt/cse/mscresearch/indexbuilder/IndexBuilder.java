package lk.ac.mrt.cse.mscresearch.indexbuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.Transformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.ClassParser;
import lk.ac.mrt.cse.mscresearch.persistance.dao.ClassDAO;
import lk.ac.mrt.cse.mscresearch.persistance.dao.JarDAO;
import lk.ac.mrt.cse.mscresearch.persistance.dao.MethodDAO;
import lk.ac.mrt.cse.mscresearch.persistance.entities.ClassIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.JarIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodBody;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;
import lk.ac.mrt.cse.mscresearch.persistance.indexer.ClassIndexer;
import lk.ac.mrt.cse.mscresearch.persistance.indexer.JarIndexer;
import lk.ac.mrt.cse.mscresearch.persistance.indexer.MethodIndexer;
import lk.ac.mrt.cse.mscresearch.util.CommandLineUtil;
import lk.ac.mrt.cse.mscresearch.util.MD5Hasher;

public class IndexBuilder {

	private static final Logger log = Logger.getLogger(IndexBuilder.class);
	
	private static final String temp = "D:\\development\\msc-research\\Temp\\";
	private final CommandLineUtil cmdUtil = new CommandLineUtil();
	
	public JarIndex buildIndex(File jar) throws IOException{
		JarIndex jarIndex = new JarIndex();
		jarIndex.setName(jar.getName());
		jarIndex.setArtifact("NOT IMPLEMENTED YET");
		String md5 = MD5Hasher.md5(jar);
		jarIndex.setJarHash(md5);
		jarIndex.setClasses(buildClassIndex(md5, jar));
		return jarIndex;
	}

	private Set<ClassIndex> buildClassIndex(String md5, File jar) throws IOException {
		File f = createTempDirectory(md5);
		File copy = cmdUtil.copy(jar, f);
		cmdUtil.unzip(copy);
		Set<ClassIndex> classes = buildClassIndexes(copy);
//		f.delete();
		return classes;
	}
	
	private File createTempDirectory(String md5) throws IOException {
		File f = new File(temp + md5);
		if(!f.exists()){
			f.mkdirs();
		}
//		f.deleteOnExit();
		return f;
	}
	
	private Set<ClassIndex> buildClassIndexes(File f) throws IOException {
		Map<String,String> fileHashes = cmdUtil.unzip(f);
		List<String> classNames = cmdUtil.getClassNamesInJar(f);
		System.out.println(classNames);
		String classPath = f.getParentFile().getAbsolutePath();
		return classNames.parallelStream().map(c->decompileAndindex(c, classPath, fileHashes.get(c)))
				                          .collect(Collectors.toSet());
	}

	private ClassIndex decompileAndindex(String clazz, String classPath, String classHash) {
		ClassUnderTransform c = new ClassUnderTransform();
		c.setClassPath(classPath);
		c.setFullyQualifiedName(clazz);
		Transformer t = new Transformer();
		t.disassemble(c);
		log.debug(c.getDisassembledCode());
		ClassParser classParser = new ClassParser(c, clazz, classHash);
		classParser.extractMethods();
		return classParser.getClassIndex();
	}

	public static void main(String[] arg) throws IOException{
		PropertyConfigurator.configure("log4j.properties");
		File f = new File("C:\\Users\\Sujan\\.m2\\repository\\commons-codec\\commons-codec\\1.9\\commons-codec-1.9.jar");
				//new File("C:\\Users\\Sujan\\.m2\\repository\\javax\\activation\\javax.activation-api\\1.2.0\\javax.activation-api-1.2.0.jar");
		JarIndex jarIndex = new IndexBuilder().buildIndex(f);
		jarIndex.getClasses();
		
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
				.addAnnotatedClass(JarIndex.class)
				.addAnnotatedClass(ClassIndex.class)
				.addAnnotatedClass(MethodIndex.class)
				.addAnnotatedClass(MethodBody.class)
				                                    .buildSessionFactory();
		
		Session se = factory.getCurrentSession();
		se.beginTransaction();
		MethodDAO methodDAO =  new MethodDAO(se);
		ClassDAO classDAO = new ClassDAO(se);
		JarDAO jarDAO = new JarDAO(se);
		
		MethodIndexer methodIndexer = new MethodIndexer(methodDAO);
		ClassIndexer classIndexer = new ClassIndexer(classDAO, methodIndexer);
		JarIndexer jarIndexer = new JarIndexer(jarDAO, classIndexer);
		
		jarIndexer.createIndexFor(jarIndex);
		se.getTransaction().commit();
//		f = new File("C:\\Users\\Sujan\\.m2\\repository\\commons-codec\\commons-codec\\1.9\\commons-codec-1.9.jar");
//		new IndexBuilder().buildIndex(f);
	}
}

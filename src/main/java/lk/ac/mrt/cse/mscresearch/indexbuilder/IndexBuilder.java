package lk.ac.mrt.cse.mscresearch.indexbuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionSorter;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.ClassParser;
import lk.ac.mrt.cse.mscresearch.remoting.ServerAdaptor;
import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.JarDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.FileWriterTimerTask;
import lk.ac.mrt.cse.mscresearch.util.IOUtil;
import lk.ac.mrt.cse.mscresearch.util.MD5Hasher;
import lk.ac.mrt.cse.mscresearch.util.ProgressTracker;

public class IndexBuilder {

	private static final Logger log = Logger.getLogger(IndexBuilder.class);
	
	private static final String temp = "D:\\development\\msc-research\\Temp\\";
	private final IOUtil ioUtil = new IOUtil();
	private final ServerAdaptor serverAdapter = new ServerAdaptor();
	
	public void buildIndex(File jar) throws Exception{
		String md5 = MD5Hasher.md5(jar);
		if(serverAdapter.isJarIndexed(md5)) {
			return;
		}
		JarDTO jarDTO = new JarDTO();
		jarDTO.setName(jar.getName());
		jarDTO.setArtifact("NOT IMPLEMENTED YET");
		jarDTO.setJarHash(md5);
		jarDTO.setClasses(buildClassIndex(md5, jar));
		serverAdapter.indexJar(jarDTO);
	}

	private Set<ClassDTO> buildClassIndex(String md5, File jar) throws Exception {
		File f = createTempDirectory(md5);
		File copy = ioUtil.copy(jar, f);
		ioUtil.unzip(copy);
		Set<ClassDTO> classes = buildClassIndexes(copy);
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
	
	private Set<ClassDTO> buildClassIndexes(File f) throws Exception {
		Map<String,String> fileHashes = ioUtil.unzip(f);
		Map<String, ClassDTO> alreadyIndexed = serverAdapter.getIndexedClasses(fileHashes);
		List<String> classNames = ioUtil.getClassNamesInJar(f).stream().filter(s->!alreadyIndexed.containsKey(s)).collect(Collectors.toList());
//		System.out.println(classNames);
		File parentFile = f.getParentFile();
		List<String> processedClasses = ioUtil.getStrings(parentFile, "processed.list");
		FileWriterTimerTask timer = new FileWriterTimerTask(new File(parentFile.getAbsolutePath() + File.separatorChar + "processed.list"));
		classNames.removeAll(processedClasses);
		ProgressTracker.addToCount(classNames.size());
		String classPath = parentFile.getAbsolutePath();
		Consumer<String> doneListener = s->{
			timer.addTask(s);
			ProgressTracker.notifyDone();
		};
		try {
			
		Set<ClassDTO> classes = classNames.stream().map(c->decompileAndindex(c, classPath, fileHashes.get(c), doneListener))
				                          .collect(Collectors.toSet());
		return classes;
		} finally {
		while(!timer.completed()) {
			Thread.sleep(1000);
		 }
		}
	}

	private ClassDTO decompileAndindex(String clazz, String classPath, String classHash, Consumer<String> doneListener) {
		ClassParser classParser = new ClassParser();
		String byteCode = ioUtil.disassembleClass(clazz, classPath);
		Set<MethodDTO> methods = classParser.extractMethods(byteCode, clazz, "gfhg");
		doneListener.accept(clazz);
		ClassDTO dto = new ClassDTO();
		dto.setClassHash(classHash);
		dto.setClassName(clazz);
		dto.setMethods(methods);
		return serverAdapter.indexClass(dto);
	}

	public static void main(String[] arg) throws Exception{
		PropertyConfigurator.configure("log4j.properties");
		File f =// new File("D:\\development\\msc-research\\Temp\\rt.jar");
				//new File("C:\\Users\\Sujan\\.m2\\repository\\commons-codec\\commons-codec\\1.9\\commons-codec-1.9.jar");
				new File("C:\\Users\\Sujan\\.m2\\repository\\javax\\activation\\javax.activation-api\\1.2.0\\javax.activation-api-1.2.0.jar");
		new IndexBuilder().buildIndex(f);
		System.out.println(ClassParser.unmappedCodes);
		System.out.println(InstructionSorter.typeCounter);
//		jarIndex.getClasses();
//		
//		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml")
//				.addAnnotatedClass(JarIndex.class)
//				.addAnnotatedClass(ClassIndex.class)
//				.addAnnotatedClass(MethodIndex.class)
//				.addAnnotatedClass(MethodBody.class)
//				                                    .buildSessionFactory();
//		
//		Session se = factory.getCurrentSession();
//		se.beginTransaction();
//		MethodDAO methodDAO =  new MethodDAO(se);
//		ClassDAO classDAO = new ClassDAO(se);
//		JarDAO jarDAO = new JarDAO(se);
//		
//		MethodIndexer methodIndexer = new MethodIndexer(methodDAO);
//		ClassIndexer classIndexer = new ClassIndexer(classDAO, methodIndexer);
//		JarIndexer jarIndexer = new JarIndexer(jarDAO, classIndexer);
//		
//		jarIndexer.createIndexFor(jarIndex);
//		se.getTransaction().commit();
//		f = new File("C:\\Users\\Sujan\\.m2\\repository\\commons-codec\\commons-codec\\1.9\\commons-codec-1.9.jar");
//		new IndexBuilder().buildIndex(f);
	}
}

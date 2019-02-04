package lk.ac.mrt.cse.mscresearch.localindex;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.LocalClassParser;
import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.IOUtil;
import lk.ac.mrt.cse.mscresearch.util.MD5Hasher;

public class IndexUpdater {

	private final String project;
	private final Set<String> dependencies;
	private final String outputLocation;
	private final FilenameFilter filter = (dir, name) -> name.endsWith(".class") || dir.isDirectory();
	private final IOUtil ioUtil = new IOUtil();
	private final LocalClassParser classParser = new LocalClassParser();
	
	public IndexUpdater(String project, Set<String> dependencies, String outputLocation) {
		this.project = project;
		this.dependencies = dependencies;
		this.outputLocation = outputLocation;
	}

	public void update() {
		LocalIndex li = new LocalIndex();
		Collection<String> currentIndex = li.getHashes();
		Map<String, String> fqnToMD5Hash = getClassFiles().stream().collect(Collectors.toMap(this::toFQN, MD5Hasher::md5));
//		Map<String, String> updatedFqnToMD5Hash = fqnToMD5Hash.entrySet().stream().filter(e-> !currentIndex.contains(e.getValue()))
//				                                                                  .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		List<ClassDTO> classes = fqnToMD5Hash.entrySet().stream().filter(e-> !currentIndex.contains(e.getValue()))
																 .map(this::decompileAndindex)
														         .collect(Collectors.toList());
		System.out.println(classes);
		for(ClassDTO cdto : classes) {
			if(cdto == null) continue;
			System.err.println(cdto.getClassName());
			for(MethodDTO mdto : cdto.getMethods()) {
				LocalMethodDTO lmdto = (LocalMethodDTO)mdto;
				String[] mbody = mdto.getBody().split("\n");
				System.out.println("\t" + mdto.getSignature());
				for(int i = 0; i<lmdto.getLineNumbers().length;i++) {
					System.out.println("\t\t" + lmdto.getLineNumbers()[i]+ " : " + mbody[i]);
				}
			}
		}
	}
	
	private ClassDTO decompileAndindex(Entry<String, String> cls) {
		return decompileAndindex(cls.getKey(), cls.getValue());
	}
	
	private ClassDTO decompileAndindex(String clazz, String classHash) {
		String byteCode = ioUtil.disassembleLocalClass(clazz, outputLocation);
		Set<MethodDTO> methods = classParser.extractMethods(byteCode, clazz);
		if(methods.isEmpty()) {
			return null;
		}
		ClassDTO dto = new ClassDTO();
		dto.setClassHash(classHash);
		dto.setClassName(clazz);
		dto.setMethods(methods);
		return dto;
	}
	
	public Set<File> getClassFiles(){
		HashSet<File> classes = new HashSet<>();
		findClasses(new File(outputLocation), classes);
		return classes;
	}
	
	private void findClasses(File f, Set<File> classes) {
		Stream.of(f.listFiles(filter)).forEach(child-> {
			if(child.isDirectory()) {
				findClasses(child, classes);
			} else {
				classes.add(child);
			}
		});
	}

	public String toFQN(File f) {
		String absolutePath = f.getAbsolutePath();
		return absolutePath.substring(outputLocation.length(), absolutePath.length() - 6).replaceAll("\\\\", ".");
	}
}

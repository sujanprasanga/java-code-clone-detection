package lk.ac.mrt.cse.mscresearch.localindex;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
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
	private final Set<String> dependentProjects;
	private final String outputLocation;
	private final FilenameFilter filter = (dir, name) -> name.endsWith(".class") || dir.isDirectory();
	private final IOUtil ioUtil = new IOUtil();
	private final LocalClassParser classParser = new LocalClassParser();
	private final LocalIndex li;
	private final Map<String, String> currentIndex;
	
	public static synchronized void update(String project, Set<String> dependentProjects, Set<String> dependencies, String outputLocation) {
		new IndexUpdater(project ,dependentProjects, dependencies, outputLocation).update();
	}
	
	public IndexUpdater(String project, Set<String> dependentProjects, Set<String> dependencies, String outputLocation) {
		this.project = project;
		this.dependentProjects = dependentProjects;
		this.dependencies = dependencies;
		this.outputLocation = outputLocation;
		li = new LocalIndex();
		currentIndex = li.getHashes();
	}

	public void update() {
		
		Set<File> classFiles = getClassFiles();
		Map<String, File> fqnToFile = getClassFiles().stream().collect(Collectors.toMap(this::toFQN, Function.identity()));
		Map<String, String> fqnToMD5Hash = classFiles.stream().collect(Collectors.toMap(this::toFQN, MD5Hasher::md5));
		Map<String, String> updatedFqnToMD5Hash = fqnToMD5Hash.entrySet().stream().filter(this::isUpdatedEntry)
				                                                                  .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		List<ClassDTO> classes = updatedFqnToMD5Hash.entrySet().stream()
                                                               .map(this::decompileAndindex)
														       .collect(Collectors.toList());
		System.out.println(classes);
		for(ClassDTO cdto : classes) {
			if(cdto == null) continue;
//			System.err.println(cdto.getClassName());
			updateLocalIndex(cdto, fqnToFile, updatedFqnToMD5Hash);
			for(MethodDTO mdto : cdto.getMethods()) {
				LocalMethodDTO lmdto = (LocalMethodDTO)mdto;
				String[] mbody = mdto.getBody().split("\n");
				System.out.println("\t" + mdto.getSignature());
				for(int i = 0; i<lmdto.getLineNumbers().length;i++) {
					System.out.println("\t\t" + lmdto.getLineNumbers()[i]+ " : " + mbody[i]);
				}
			}
		}
		LocalIndex.removeDeleted();
		updateDependecyMapping();
	}
	
	private void updateDependecyMapping() {
		Set<String> dependencyMapping = new HashSet<>();
		dependencies.stream().map(File::new).map(MD5Hasher::md5).forEach(dependencyMapping::add);
		dependentProjects.forEach(dependencyMapping::add);
		LocalIndex.updateDependecyMapping(project, dependencyMapping);
	}

	private boolean isUpdatedEntry(Entry<String, String> e) {
		String hash = currentIndex.get(e.getKey());
		return hash == null || !hash.equals(e.getValue());
	}
	
	private void updateLocalIndex(ClassDTO cdto, Map<String, File> fqnToFile, Map<String, String> updatedFqnToMD5Hash) {
		LocalIndex.updateLocalIndex(project, fqnToFile, cdto, updatedFqnToMD5Hash);
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

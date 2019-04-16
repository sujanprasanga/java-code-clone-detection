package lk.ac.mrt.cse.mscresearch.localindex;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;

public class LocalIndex {

	private static final List<LocalIndexEntry> localIndexEntry = new ArrayList<>(10000);
	
	private static final Map<String, Set<String>> dependencyMapping = new ConcurrentHashMap<>();
	
	public static List<LocalIndexEntry> getLocalIndexes(){
		return Collections.unmodifiableList(localIndexEntry);
	}
	
	public static synchronized void updateLocalIndex(String project, Map<String, File> fqnToFile, ClassDTO cdto, Map<String, String> fqnTohashValueHash) {
		List<LocalIndexEntry> tmp = localIndexEntry.stream().filter(e->e.getClazz().equals(cdto.getClassName()) && 
				                                                       e.getProject().equals(project))
				                                            .collect(Collectors.toList());
		localIndexEntry.removeAll(tmp);
		String clazz = cdto.getClassName();
		File f = fqnToFile.get(clazz);
		String clazzHash = fqnTohashValueHash.get(clazz);
		cdto.getMethods().forEach(m->{
			localIndexEntry.add(new LocalIndexEntry(project, f, clazz, clazzHash, m));
		});
	}
	

	public static void removeDeleted() {
		List<LocalIndexEntry> deleted = localIndexEntry.stream().filter(LocalIndex::isDeleted)
				                                                .collect(Collectors.toList());
		localIndexEntry.removeAll(deleted);
	}

	private static boolean isDeleted(LocalIndexEntry e) {
		return !e.getFile().exists();
	}
	
	public static void updateDependecyMapping(String project, Set<String> dependencies) {
		dependencies.add(project);
		dependencyMapping.put(project, dependencies);
	}
	
	public static Map<String, Set<String>> getDependencyMapping(){
		return dependencyMapping;
	}
	
	public static boolean isValidDependency(String project, String target) {
		Set<String> dependencies = dependencyMapping.get(project);
		return dependencies != null && dependencies.contains(target);
	}
}

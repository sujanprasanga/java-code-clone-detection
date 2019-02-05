package lk.ac.mrt.cse.mscresearch.localindex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;
import lk.ac.mrt.cse.mscresearch.remoting.dto.ClassDTO;

public class LocalIndex {

	private static final Map<String, List<MethodIndex>> localIndex = new ConcurrentHashMap<>();
	private static final Map<String, String> hashes = new ConcurrentHashMap<>();
	private static final List<LocalIndexEntry> localIndexEntry = new ArrayList<>(10000);
	
	public List<MethodIndex> getMethods(String clz){
		return localIndex.get(clz);
	}
	
	public void updateIndex(String clz, String hash, List<MethodIndex> methods) {
		localIndex.put(clz, methods);
		hashes.put(clz, hash);
	}
	
	public Collection<String> getHashes(){
		return hashes.values();
	}

	public static List<LocalIndexEntry> getLocalIndexes(){
		return Collections.unmodifiableList(localIndexEntry);
	}
	
	public static synchronized void updateLocalIndex(String project, ClassDTO cdto) {
		List<LocalIndexEntry> tmp = localIndexEntry.stream().filter(e->e.getClazz().equals(cdto.getClassName()) && 
				                                                       e.getProject().equals(project))
				                                            .collect(Collectors.toList());
		localIndexEntry.removeAll(tmp);
		String clazz = cdto.getClassName();
		cdto.getMethods().forEach(m->{
			localIndexEntry.add(new LocalIndexEntry(project, clazz, m.getSignature(), m.getBodyhash(), m.getPluginid()));
		});
	}
}

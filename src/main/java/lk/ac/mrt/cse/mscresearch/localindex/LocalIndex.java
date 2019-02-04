package lk.ac.mrt.cse.mscresearch.localindex;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class LocalIndex {

	private static final Map<String, List<MethodIndex>> localIndex = new ConcurrentHashMap<>();
	private static final Map<String, String> hashes = new ConcurrentHashMap<>();
	
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
}

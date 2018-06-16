package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches;

public class FieldCache {

	public static String getCachedId(String className, String field){
		// use id in constant pool ?
		
		return className + '#' + field;
	}
}

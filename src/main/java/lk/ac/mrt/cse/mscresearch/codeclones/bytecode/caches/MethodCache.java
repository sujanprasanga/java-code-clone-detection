package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches;

public class MethodCache {

	public static String getCachedId(String className, String method){
		// use id in constant pool ?
		
		return className + '#' + method;
	}
}

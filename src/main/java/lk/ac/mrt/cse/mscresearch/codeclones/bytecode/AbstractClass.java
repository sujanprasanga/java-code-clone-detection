package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.List;

public abstract class AbstractClass<MethodType> {
	
	private final String className;
	private final List<MethodType> methods;
	
	public AbstractClass(String className, List<MethodType> methods) {
		this.className = className;
		this.methods = methods;
	}

	public String getClassName() {
		return className;
	}
	
	public List<MethodType> getMethods() {
		return methods;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(className);
		for(MethodType c : methods){
			sb.append('\n').append(c.toString()).append('\n');
		}
		return sb.toString();
	}
}

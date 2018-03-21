package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.List;

public class FormattedClass {
	
	private final String className;
	private final List<FormattedMethod> methods;
	
	public FormattedClass(String className, List<FormattedMethod> methods) {
		this.className = className;
		this.methods = methods;
	}

	public String getClassName() {
		return className;
	}
	
	public List<FormattedMethod> getMethods() {
		return methods;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(className);
		for(FormattedMethod c : methods){
			sb.append('\n').append(c.toString()).append('\n');
		}
		return sb.toString();
	}
}

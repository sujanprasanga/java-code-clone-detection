package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.List;

public abstract class AbstractMethod {

	private final String methodName;
	private final String methodSignature;
	private final List<String> code;
	private final String className;
	
	public AbstractMethod(String className, String methodName, String methodSignature, List<String> code) {
		this.className = className;
		this.methodName = methodName;
		this.methodSignature = methodSignature;
		this.code = code;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<String> getCode() {
		return code;
	}

	public String getClassName() {
		return className;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(methodSignature);
		for(String c : code){
			sb.append('\n').append(c);
		}
		return sb.toString();
	}
}

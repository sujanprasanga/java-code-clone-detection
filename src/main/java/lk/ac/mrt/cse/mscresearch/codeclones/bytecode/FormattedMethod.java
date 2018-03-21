package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.List;
import java.util.Map;

public class FormattedMethod {

	private final String className;
	private final String methodName;
	private final String methodSignature;
	private final List<String> code;
	private final List<Integer> lables;
	private final  Map<Integer, Integer> lineNumberTable;
	
	public FormattedMethod(String className, String methodName, String methodSignature, List<String> code, List<Integer> lables, Map<Integer, Integer> lineNumberTable) {
		this.className = className;
		this.methodName = methodName;
		this.methodSignature = methodSignature;
		this.code = code;
		this.lables = lables;
		this.lineNumberTable = lineNumberTable;
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

//	public static FormattedMethod create(String className, String methodName, List<String> methodLines) {
//		List<String> subList = new ArrayList<>(methodLines.subList(2, methodLines.size()));
//		return new FormattedMethod(className, methodName, methodLines.get(0), subList);
//	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(methodSignature);
		for(String c : code){
			sb.append('\n').append(c);
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormattedMethod other = (FormattedMethod) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	public String getClassName() {
		return className;
	}

	public List<Integer> getLables() {
		return lables;
	}

	public Map<Integer, Integer> getLineNumberTable() {
		return lineNumberTable;
	}
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.ArrayList;
import java.util.List;

public class RefactorableEntity {

	private final String methodName;
	private final String className;
	private final int lineNumberStart;
	private final int lineNumberEnd;
	private final List<String> code;
	
	public RefactorableEntity(String methodName, String className, int lineNumberStart, int lineNumberEnd, List<String> code) {
		this.methodName = methodName;
		this.className = className;
		this.code = new ArrayList<>(code);
		this.lineNumberStart = lineNumberStart;
		this.lineNumberEnd = lineNumberEnd;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getClassName() {
		return className;
	}

	public int getLineNumberStart() {
		return lineNumberStart;
	}

	public int getLineNumberEnd() {
		return lineNumberEnd;
	}

	public List<String> getCode() {
		return code;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(className).append('#').append(methodName).append(':').append(lineNumberStart).append('-').append(lineNumberEnd);
		sb.append('\n').append("code:[");
		for(String s : code){
			sb.append('\n').append(s);
		}
		sb.append('\n').append("]");
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
		RefactorableEntity other = (RefactorableEntity) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if(code.size() != other.code.size()){
			return false;
		}
		else
		{
			for(int i = 0; i< code.size(); i++){
				if(!code.get(i).equals(other.code.get(i))){
					return false;
				}
			}
		}
		return true;
	}
	
	
}

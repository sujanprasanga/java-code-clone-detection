package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.File;
import java.util.List;

public class ClassUnderTransform {

	enum STAGE
	{
		INITIAL,
		DISASSEMBLED,
		FILTERED,
		METHODS_SPLIT
	}
	
	private String classPath;
	private STAGE stage;
	private String fullyQualifiedName;
	private String disassembledCode;
	private List<int[]> methodIndexes;
	private List<int[]> lineNumberIndexes;
	
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}
	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public STAGE getStage() {
		return stage;
	}
	public void setStage(STAGE stage) {
		this.stage = stage;
	}
	public String getDisassembledCode() {
		return disassembledCode;
	}
	public void setDisassembledCode(String disassembledCode) {
		this.disassembledCode = disassembledCode;
	}
	public List<int[]> getMethodIndexes() {
		return methodIndexes;
	}
	public void setMethodIndexes(List<int[]> methodIndexes) {
		this.methodIndexes = methodIndexes;
	}
	public List<int[]> getLineNumberIndexes() {
		return lineNumberIndexes;
	}
	public void setLineNumberIndexes(List<int[]> lineNumberIndexes) {
		this.lineNumberIndexes = lineNumberIndexes;
	}
	
	
}

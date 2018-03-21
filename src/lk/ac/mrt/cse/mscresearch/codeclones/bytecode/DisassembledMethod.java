package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.ArrayList;
import java.util.List;

public class DisassembledMethod extends AbstractMethod{

	private final List<String> lineNumberEntries;
	
	public DisassembledMethod(String className, String methodName, String methodSignature, List<String> code, List<String> lineNumberEntries) {
		super(className, methodName, methodSignature, code);
		this.lineNumberEntries = lineNumberEntries;
	}

	public static DisassembledMethod create(String className, String methodName, List<String> methodLines, List<String> lineNumberEntries) {
		List<String> subList = new ArrayList<>(methodLines.subList(2, methodLines.size()));
		return new DisassembledMethod(className, methodName, methodLines.get(0), subList, lineNumberEntries);
	}

	public List<String> getLineNumberTableEntries() {
		return lineNumberEntries;
	}
}

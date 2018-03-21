package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.List;

public class DisassembledClass extends AbstractClass<DisassembledMethod>{
	
	public DisassembledClass(String className, List<DisassembledMethod> methods) {
		super(className, methods);
	}

	public static DisassembledClass create(String className, String disassembledCode) {
		return new DisassembledClass(className, ClassSplitter.split(className, disassembledCode));
	}
}

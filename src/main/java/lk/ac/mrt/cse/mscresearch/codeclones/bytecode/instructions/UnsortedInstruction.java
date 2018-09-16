package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;

public class UnsortedInstruction extends Instruction {

	private final String instruction;
	
	private UnsortedInstruction(String arg, int label) {
		super(label, TYPE.RAW);
		this.instruction = arg;
	}

	public String getInstruction() {
		return instruction;
	}

	public static UnsortedInstruction forInstruction(InstructionCreateParam p) {
		int label = getLabelNumber(p.matcher);
		return new UnsortedInstruction(p.arg, label);
	}
	
	public String toString(){
		return "NOP";
	}
}

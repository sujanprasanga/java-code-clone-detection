package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class UnsortedInstruction extends Instruction {

	private final String instruction;
	
	private UnsortedInstruction(String arg, int label) {
		super(label, TYPE.RAW);
		this.instruction = arg;
	}

	public String getInstruction() {
		return instruction;
	}

	public static UnsortedInstruction forInstruction(String instruction, Matcher matcher){
		int label = getLabelNumber(matcher);
		return new UnsortedInstruction(instruction, label);
	}
	
	public String toString(){
		return "NOP";
	}
}

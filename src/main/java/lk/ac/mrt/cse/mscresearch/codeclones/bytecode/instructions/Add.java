package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Add extends Instruction {

	public Add(int label) {
		super(label, TYPE.ADD);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Add from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Add(label);
	}

    public String toString(){
		return "add";
	}
}

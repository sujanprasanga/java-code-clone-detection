package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Increment extends Instruction {

	public Increment(int label) {
		super(label, TYPE.INC);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Increment from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Increment(label);
	}

    public String toString(){
		return "increment";
	}
}

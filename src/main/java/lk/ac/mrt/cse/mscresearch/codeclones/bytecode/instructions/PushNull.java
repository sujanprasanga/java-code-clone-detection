package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class PushNull extends Instruction {

	public PushNull(int label) {
		super(label, TYPE.PUSH_NULL);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static PushNull from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new PushNull(label);
	}

    public String toString(){
		return "pushNull";
	}
}

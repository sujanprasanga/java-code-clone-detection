package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class NewArray extends Instruction {

	public NewArray(int label) {
		super(label, TYPE.NEW_ARRAY);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static NewArray from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new NewArray(label);
	}

    public String toString(){
		return "newArray";
	}
}

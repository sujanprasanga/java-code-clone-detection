package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Wide extends Instruction {

	public Wide(int label) {
		super(label, TYPE.WIDE);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Wide from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Wide(label);
	}

    public String toString(){
		return "wide";
	}
}

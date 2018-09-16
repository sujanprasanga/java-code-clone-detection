package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Neg extends Instruction {

	public Neg(int label) {
		super(label, TYPE.NEG);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Neg from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Neg(label);
	}

    public String toString(){
		return "negate";
	}
}

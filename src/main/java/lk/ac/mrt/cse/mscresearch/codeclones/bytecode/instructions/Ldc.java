package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Ldc extends Instruction {

	public Ldc(int label) {
		super(label, TYPE.LDC);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Ldc from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Ldc(label);
	}

    public String toString(){
		return "ldc";
	}
}

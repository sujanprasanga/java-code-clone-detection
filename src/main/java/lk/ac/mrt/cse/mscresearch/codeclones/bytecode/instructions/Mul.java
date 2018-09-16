package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Mul extends Instruction {

	public Mul(int label) {
		super(label, TYPE.MUL);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}

	public static Mul from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Mul(label);
	}

    public String toString(){
		return "multiply";
	}
}

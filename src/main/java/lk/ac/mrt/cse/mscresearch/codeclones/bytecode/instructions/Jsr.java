package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Jsr extends Instruction {

	public Jsr(int label) {
		super(label, TYPE.JSR);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Jsr from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Jsr(label);
	}

    public String toString(){
		return "jumpSubroutine";
	}
}

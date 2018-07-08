package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Nop extends Instruction {

	public Nop(int label) {
		super(label, TYPE.NOP);
	}

	public static Nop from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Nop(label);
	}

    public String toString(){
		return "nop";
	}
}

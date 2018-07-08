package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class CheckCast extends Instruction {

	public CheckCast(int label) {
		super(label, TYPE.CHECKCAST);
	}

	public static CheckCast from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new CheckCast(label);
	}

    public String toString(){
		return "checkCast";
	}
}

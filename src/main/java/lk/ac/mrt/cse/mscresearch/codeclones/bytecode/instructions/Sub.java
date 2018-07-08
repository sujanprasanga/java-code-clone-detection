package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Sub extends Instruction {

	public Sub(int label) {
		super(label, TYPE.SUB);
	}

	public static Sub from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Sub(label);
	}

    public String toString(){
		return "subsitute";
	}
}

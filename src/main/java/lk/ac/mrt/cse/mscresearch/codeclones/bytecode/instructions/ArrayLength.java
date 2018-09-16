package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class ArrayLength extends Instruction {

	public ArrayLength(int label) {
		super(label, TYPE.ARRAY_LENGH);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static ArrayLength from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new ArrayLength(label);
	}

    public String toString(){
		return "arraylength";
	}
}

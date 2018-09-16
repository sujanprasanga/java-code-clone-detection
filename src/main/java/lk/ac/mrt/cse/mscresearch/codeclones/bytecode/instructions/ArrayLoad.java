package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class ArrayLoad extends Instruction {

	public ArrayLoad(int label) {
		super(label, TYPE.ARRAY_LOAD);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static ArrayLoad from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new ArrayLoad(label);
	}

    public String toString(){
		return "arrayload";
	}
}

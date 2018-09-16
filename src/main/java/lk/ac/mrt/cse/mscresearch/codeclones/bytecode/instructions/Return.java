package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Return extends Instruction {

	public Return(int label) {
		super(label, TYPE.RETURN);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static Return from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Return(label);
	}

    public String toString(){
		return "return";
	}
}

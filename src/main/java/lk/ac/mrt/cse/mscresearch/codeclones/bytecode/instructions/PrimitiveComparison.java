package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class PrimitiveComparison extends Instruction {

	public PrimitiveComparison(int label) {
		super(label, TYPE.PRIMITIVE_COMPARISON);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static PrimitiveComparison from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new PrimitiveComparison(label);
	}

    public String toString(){
		return "primitiveComparison";
	}
}

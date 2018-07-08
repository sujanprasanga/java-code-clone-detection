package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class PrimitiveConversion extends Instruction {

	public PrimitiveConversion(int label) {
		super(label, TYPE.PRIMITIVE_CONVERSION);
	}

	public static PrimitiveConversion from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new PrimitiveConversion(label);
	}

    public String toString(){
		return "primitiveConversion";
	}
}

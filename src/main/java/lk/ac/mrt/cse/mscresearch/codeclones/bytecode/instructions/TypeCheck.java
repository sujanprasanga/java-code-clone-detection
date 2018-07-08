package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class TypeCheck extends Instruction {

	public TypeCheck(int label) {
		super(label, TYPE.TYPE_CHECK);
	}

	public static TypeCheck from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new TypeCheck(label);
	}

    public String toString(){
		return "typeCheck";
	}
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class ArrayStore extends Instruction {

	public ArrayStore(int label) {
		super(label, TYPE.ARRAY_STORE);
	}

	public static ArrayStore from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new ArrayStore(label);
	}

    public String toString(){
		return "arraystore";
	}
}

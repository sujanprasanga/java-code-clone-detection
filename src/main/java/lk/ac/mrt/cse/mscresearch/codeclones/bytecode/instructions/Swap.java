package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Swap extends Instruction {

	public Swap(int label) {
		super(label, TYPE.SWAP);
	}

	public static Swap from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Swap(label);
	}

    public String toString(){
		return "swap";
	}
}

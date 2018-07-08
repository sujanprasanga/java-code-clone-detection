package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Rem extends Instruction {

	public Rem(int label) {
		super(label, TYPE.REM);
	}

	public static Rem from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Rem(label);
	}

    public String toString(){
		return "remainder";
	}
}

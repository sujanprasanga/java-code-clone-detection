package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Div extends Instruction {

	public Div(int label) {
		super(label, TYPE.DIV);
	}

	public static Div from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Div(label);
	}

    public String toString(){
		return "divide";
	}
}

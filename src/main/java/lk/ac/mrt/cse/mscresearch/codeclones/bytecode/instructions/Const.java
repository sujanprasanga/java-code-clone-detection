package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Const extends Instruction{

	private Const(int label) {
		super(label, TYPE.CONST);
	}
	
	public static Const from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new Const(label);
	}
	
	public String toString(){
		return "const";
	} 
	
	public boolean stackPush(){
		return true;
	}
	
	public String getLocalVar() {
		return "constant";
	}
}

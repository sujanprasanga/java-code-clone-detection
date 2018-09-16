package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Throw extends Instruction{

	
	private Throw(int label) {
		super(label, TYPE.THROW);
	}
	
	public static Throw from(InstructionCreateParam p) {
		return forException(p.matcher, p.arg);
	}
	
	public static Throw forException(Matcher m, String exception){
		return new Throw(getLabelNumber(m));
	}
	
	public void resolvePreConditions(String[] body, int index){}
	
	public String toString(){
		return "throwexception";
	}
}

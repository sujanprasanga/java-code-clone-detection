package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Pop extends Instruction {

	private static final Pop instance = new Pop(); 
	
	public Pop() {
		super(0, TYPE.POP);
	}

	public static Pop from(Matcher m){
		return instance;
	}

	public boolean stackPop(){
		return true;
	}
	
	public String toString(){
		return "pop";
	}
	
}

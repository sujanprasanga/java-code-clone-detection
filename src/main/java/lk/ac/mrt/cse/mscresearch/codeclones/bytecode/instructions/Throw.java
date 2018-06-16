package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class Throw extends Instruction{

	private final static ImmutableUtil<Throw> immutableUtil = new ImmutableUtil<>((String arg, Matcher matcher, String tos)-> new Throw(tos));
	
	private final String exception;
	
	private Throw(String exception) {
		super(0, TYPE.THROW);
		this.exception = exception;
	}
	
	public String getException() {
		return exception;
	}

	public static Throw forException(String exception){
		return immutableUtil.get(exception);
	}
	
	public void resolvePreConditions(String[] body, int index){}
	
	public String toString(){
		return "throw-" + exception;
	}
}

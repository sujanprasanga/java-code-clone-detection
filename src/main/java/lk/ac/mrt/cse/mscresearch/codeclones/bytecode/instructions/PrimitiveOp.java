package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class PrimitiveOp extends Instruction{

	private final static ImmutableUtil<PrimitiveOp> immutableUtil = new ImmutableUtil<>((String arg, Matcher matcher)-> new PrimitiveOp(arg));
	
	private PrimitiveOp(String ops) {
		super(0, TYPE.PRIMITIVE_OP);
	}
	
	public static PrimitiveOp get(String ops, Matcher matcher){
		return immutableUtil.get("");
	}
	
	public void resolvePreConditions(String[] body, int index){}
	
	public String toString(){
		return "";
	}
}

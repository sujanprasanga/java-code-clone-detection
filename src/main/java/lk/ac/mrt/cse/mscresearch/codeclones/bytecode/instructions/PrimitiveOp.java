package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class PrimitiveOp extends Instruction{

//	private final static ImmutableUtil<PrimitiveOp> immutableUtil = new ImmutableUtil<>(arg-> new PrimitiveOp(arg));
	
	public static Instruction from(InstructionCreateParam p) {
		return get(p.arg, p.matcher);
	}
	
	private PrimitiveOp(String ops) {
		super(0, TYPE.PRIMITIVE_OP);
	}
	
	public static PrimitiveOp get(String ops, Matcher matcher){
		return new PrimitiveOp("");
	}
	
	public void resolvePreConditions(String[] body, int index){}
	
	public String toString(){
		return "";
	}
}

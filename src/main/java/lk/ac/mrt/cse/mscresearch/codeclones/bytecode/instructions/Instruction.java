package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.List;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public abstract class Instruction {

	enum TYPE{
		METHOD_CALL,
		GET_FIELD,
		SET_FIELD,
		BRANCH,
		LOOP,
		THROW,
		PRIMITIVE_OP,
		RAW,
		STORE,
		LOAD,
		POP, 
		NEW_CLASS,
		DUP,
		CONST
	}
	
	private final int label;
	private final TYPE type;
	
	public TYPE getType(){
		return type;
	}

	public void resolvePreConditions(String[] body, int index){}

	public Instruction(int label, TYPE type) {
		super();
		this.label = label;
		this.type = type;
	}
	
	public static int getLabelNumber(Matcher m){
		return Integer.parseInt(m.group(RegularExpressionUtil.INSTRUCTION_LABEL_CG_NAME));
	}

	public int getLabel() {
		return label;
	}

	public void add(List<Instruction> b) {
		throw new UnsupportedOperationException("cannot add " + b + " for type " + getClass());
	}
	
	public boolean isBranching(){
		return false;
	}
	
	public String toString(){
		throw new UnsupportedOperationException("Not Implemented");
	}
	
	public boolean localVarTableModifier(){
		return false;
	}
	
	public boolean localVarLoader(){
		return false;
	}
	
	public boolean stackPush(){
		return false;
	}
	
	public boolean stackPop(){
		return false;
	}
	
	public int getLocalVarIndex() {
		throw new UnsupportedOperationException("Not Implemented");
	}
	
	public String getLocalVar() {
		throw new UnsupportedOperationException("Not Implemented");
	}
	
	public boolean duplicateStack(){
		return false;
	}
	
	public int getDupIndex(){
		throw new UnsupportedOperationException("Not Implemented");
	}
}

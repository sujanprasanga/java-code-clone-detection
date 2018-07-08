package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public abstract class Instruction {

	public enum TYPE{
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
		CONST, 
		ARRAY_LOAD,
		ARRAY_STORE, PUSH_NULL, NEW_ARRAY, RETURN, ARRAY_LENGH, CHECKCAST, PRIMITIVE_CONVERSION, ADD, PRIMITIVE_COMPARISON, DIV, MUL, NEG, REM, SUB, INC, TYPE_CHECK, LOGIC_OP, JSR, LDC, MONITOR_ACCESS, NOP, SWAP, WIDE
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
	
	public void modifyStackAndLocalVarTable(Map<Integer, String> localVars, Deque<String> stack){}
}

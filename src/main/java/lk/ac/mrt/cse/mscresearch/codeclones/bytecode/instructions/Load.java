package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public class Load extends Instruction {

	private final int index;
	
	public Load(int label, int index) {
		super(label, TYPE.STORE);
		this.index = index;
	}

	public static Load from(String field, Matcher m){
		int label = getLabelNumber(m);
		int index = Integer.parseInt(m.group(RegularExpressionUtil.LOAD_CG_NAME));
		return new Load(label, index);
	}

	public int getLocalVarIndex() {
		return index;
	}
	
	public boolean stackPush(){
		return false;
	}
	
	public boolean stackPop(){
		return false;
	}
	
	public String toString(){
		return "load-" + index;
	}
	
	public boolean localVarLoader(){
		return true;
	}
}

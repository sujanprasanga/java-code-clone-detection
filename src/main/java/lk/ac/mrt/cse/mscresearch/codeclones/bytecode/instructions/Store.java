package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public class Store extends Instruction {

	private final int index;
	
	public Store(int label, int index) {
		super(label, TYPE.STORE);
		this.index = index;
	}

	public static Store from(String field, Matcher m){
		int label = getLabelNumber(m);
		int index = Integer.parseInt(m.group(RegularExpressionUtil.STORE_CG_NAME));
		return new Store(label, index);
	}

	public int getLocalVarIndex() {
		return index;
	}
	
	public boolean localVarTableModifier(){
		return true;
	}
	
	public boolean stackPush(){
		return false;
	}
	
	public boolean stackPop(){
		return true;
	}
	
	public String toString(){
		return "store-" + index;
	}
	
}

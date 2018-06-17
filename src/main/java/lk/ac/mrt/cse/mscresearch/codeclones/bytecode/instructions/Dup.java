package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public class Dup extends Instruction{

	private final int dupIndex;
	
	private Dup(int dupIndex, int label) {
		super(label, TYPE.DUP);
		this.dupIndex = dupIndex;
	}
	
	public static Dup from(String field, Matcher m){
		int label = getLabelNumber(m);
		int dup = 0;
		if(m.start(RegularExpressionUtil.DUP_CG_NAME) > 0){
			String tmp = m.group(RegularExpressionUtil.DUP_CG_NAME);
			if(!tmp.trim().isEmpty()){
				dup = Integer.parseInt(tmp);
			}
		}
		return new Dup(dup, label);
	}
	
	public String toString(){
		return "dup-" + dupIndex;
	} 
	
	public boolean duplicateStack(){
		return true;
	}
	
	public int getDupIndex(){
		return dupIndex;
	}
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.ClassCache;

public class New extends Instruction{

	private final String classid;
	
	private New(String classid, int label) {
		super(label, TYPE.NEW_CLASS);
		this.classid = classid;
	}
	
	public static New forClazz(String field, Matcher m){
		int label = getLabelNumber(m);
		String c = ClassCache.getCachedId(m.group(RegularExpressionUtil.NEW_CLASS_CG_NAME));
		return new New(c, label);
	}
	
	public String toString(){
		return "new-" + classid;
	} 
	
	public boolean stackPush(){
		return true;
	}
	
	public String getLocalVar() {
		return classid;
	}
}

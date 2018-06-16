package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.ClassCache;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.MethodCache;

public class MethodCall extends Instruction{

	private final String method;
	private final String classId;
	private final String returnType;
	
	private MethodCall(String classId, String method, String returnType, int label) {
		super(label, TYPE.THROW);
		this.method = method;
		this.classId = classId;
		this.returnType = returnType;
	}
	
	@Override
	public TYPE getType() {
		return TYPE.METHOD_CALL;
	}

	public String getMethod() {
		return method;
	}

	public static MethodCall forMethod(String code, Matcher m){
		String c = ClassCache.getCachedId(m.group(RegularExpressionUtil.ICLASS_CG_NAME));
		String r = ClassCache.getCachedId(m.group(RegularExpressionUtil.IRETURN_CG_NAME));
		String mId = MethodCache.getCachedId(c, m.group(RegularExpressionUtil.METHOD_CG_NAME));
		int label = getLabelNumber(m);
		String method = MethodCache.getCachedId(c, mId);
		return new MethodCall(c, method, r, label);
	}
	
	public void resolvePreConditions(String[] body, int index){}
	
	public String toString(){
		return classId + '#' + method;
	}
	
	public boolean stackPush(){
		return true;
	}
	
	public String getLocalVar() {
		return returnType;
	}
}

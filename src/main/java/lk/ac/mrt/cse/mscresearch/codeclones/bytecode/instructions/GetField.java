package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.ClassCache;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.FieldCache;

public class GetField extends Instruction{

	private final String classid;
	private final String field;
	
	private GetField(String classid, String field, int label) {
		super(label, TYPE.GET_FIELD);
		this.classid = classid;
		this.field = field;
	}
	
	public static GetField from(InstructionCreateParam p) {
		return forField(p.target, p.arg, p.matcher);
	}
	
	public static GetField forField(ClassUnderTransform target, String field, Matcher m){
		int label = getLabelNumber(m);
		String c = ClassCache.getCachedId(m.group(RegularExpressionUtil.GFCLASS_CG_NAME));
		String clazzName = c == null ? target.getFullyQualifiedName() : c;
		String fieldName = m.group(RegularExpressionUtil.GFIELD_CG_NAME);
//		String f = FieldCache.getCachedId(c, fieldName);
		
		return new GetField(clazzName, fieldName, label);
	}
	
	public String toString(){
		return classid + '#' + field;
	} 
	
	public boolean stackPush(){
		return true;
	}
	
	public String getLocalVar() {
		return field;
	}
}

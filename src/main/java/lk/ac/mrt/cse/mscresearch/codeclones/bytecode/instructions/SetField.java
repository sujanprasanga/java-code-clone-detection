package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.ClassCache;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.caches.FieldCache;

public class SetField extends Instruction{

	private final String classid;
	private final String field;
	
	private SetField(String classid, String field, int label) {
		super(label, TYPE.GET_FIELD);
		this.classid = classid;
		this.field = field;
	}

	public static SetField from(InstructionCreateParam p) {
		return forField(p.target, p.arg, p.matcher);
	}
	
	public static SetField forField(ClassUnderTransform target, String field, Matcher m){
		String c = ClassCache.getCachedId(m.group(RegularExpressionUtil.PFCLASS_CG_NAME));
		String clazz = c == null ? target.getFullyQualifiedName() : c;
		String fieldName = m.group(RegularExpressionUtil.PFIELD_CG_NAME);
//		String f = FieldCache.getCachedId(c, fieldName);
		int label = getLabelNumber(m);
		return new SetField(clazz, fieldName, label);
	}
	
	public String toString(){
		return classid + '#' + field;
	} 
}

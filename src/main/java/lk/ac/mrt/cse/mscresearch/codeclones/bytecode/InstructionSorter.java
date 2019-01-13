package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.Category;
import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InstructionSorter {

	private static final Map<InstrcutionMatcherFactory, Mapper> sorters;
	
	private static final Pattern switchTargets;

	public static final Map<String, AtomicInteger> typeCounter; 
	
	static {
		sorters = new LinkedHashMap<>();
		PropertyUtil propertyUtil = new PropertyUtil();
		
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForPrimitiveOperations()), InstructionSorter::mapPrimitiveOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForOtherOperations()), InstructionSorter::mapOtherOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForInvoke()), InstructionSorter::mapInvokeOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForConditionalOperations()), InstructionSorter::mapConditionalOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForFieldOperations()), InstructionSorter::mapFieldOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForReturnOp()), InstructionSorter::mapReturnOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForInstanceOp()), InstructionSorter::mapInstanceOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForInvokeDynamic()), InstructionSorter::mapInvokeDynamicOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForNewArrayOp()), InstructionSorter::mapNewArrayOp);
		sorters.put(new InstrcutionMatcherFactory(propertyUtil.getRegExForSwitch()), InstructionSorter::mapSwitchOp);
		
		switchTargets = Pattern.compile(propertyUtil.getRegExForSwitchTargets());
		
		typeCounter = new LinkedHashMap<>();
		
		typeCounter.put("mapPrimitiveOp"      ,new AtomicInteger());
		typeCounter.put("mapOtherOp",          new AtomicInteger()   );
		typeCounter.put("mapInvokeOp",         new AtomicInteger() );
		typeCounter.put("mapConditionalOp",    new AtomicInteger());
		typeCounter.put("mapFieldOp",          new AtomicInteger());
		typeCounter.put("mapInstanceOp",       new AtomicInteger()  );
		typeCounter.put("mapReturnOp",         new AtomicInteger()   );
		typeCounter.put("mapNewArrayOp",       new AtomicInteger() );
		typeCounter.put("mapSwitchOp",         new AtomicInteger()  );
		typeCounter.put("mapInvokeDynamicOp",  new AtomicInteger() );
		typeCounter.put("none",         new AtomicInteger()  );
	}
	
	public static OpCode decode(String s) {
		for(Entry<InstrcutionMatcherFactory, Mapper> e : sorters.entrySet()) {
			InstrcutionMatcher matcher = e.getKey().create(s);
			if(matcher.matches()) {
				return e.getValue().map(matcher);
			}
		}
		
		typeCounter.get("none").incrementAndGet();
		return null;
	}
	
	private static OpCode mapPrimitiveOp(InstrcutionMatcher m) {
		typeCounter.get("mapPrimitiveOp").incrementAndGet();
		m.setCategory(Category.PRIMITIVE_OP);
		m.extractLabel();
		m.extractType();
		m.extractOp();
		m.extractArrayOp();
		return m.op;
	}
	
	private static OpCode mapConditionalOp(InstrcutionMatcher m) {
		typeCounter.get("mapConditionalOp").incrementAndGet();
		m.setCategory(Category.CONDITIONAL);
		m.extractLabel();
		m.extractTarget();
		m.extractOp();
		return m.op;
	}
	
	private static OpCode mapFieldOp(InstrcutionMatcher m) {
		typeCounter.get("mapFieldOp").incrementAndGet();
		m.setCategory(Category.FIELD);
		m.extractLabel();
		m.extractOp();
		m.extractFieldName();
		return m.op;
	}
	
	private static OpCode mapInstanceOp(InstrcutionMatcher m) {
		typeCounter.get("mapInstanceOp").incrementAndGet();
		m.setCategory(Category.TYPE_CHECK);
		m.extractLabel();
		m.extractType();
		m.extractOp();
		return m.op;
	}
	
	private static OpCode mapInvokeOp(InstrcutionMatcher m) {
		typeCounter.get("mapInvokeOp").incrementAndGet();
		m.setCategory(Category.INVOKE);
		m.extractOp();
		m.extractLabel();
		m.extractType();
		m.extractMethodSignature();
		return m.op;
	}
	
	private static OpCode mapInvokeDynamicOp(InstrcutionMatcher m) {
		typeCounter.get("mapInvokeDynamicOp").incrementAndGet();
		m.setCategory(Category.INVOKE_DYNAMIC);
		m.extractLabel();
		return m.op;
	}
	
	private static OpCode mapNewArrayOp(InstrcutionMatcher m) {
		typeCounter.get("mapNewArrayOp").incrementAndGet();
		m.setCategory(Category.NEW_ARRAY);
		m.extractLabel();
		m.extractType();
		OpCode op = m.op;
		if(op.getTargetClass() == null) {
			op.setTargetClass(m.m.group("primitivetype"));
		}
		return op;
	}

	private static OpCode mapOtherOp(InstrcutionMatcher m) {
		typeCounter.get("mapOtherOp").incrementAndGet();
		m.setCategory(Category.OTHER);
		m.extractLabel();
		m.extractOp();
		return m.op;
	}
	
	private static OpCode mapReturnOp(InstrcutionMatcher m) {
		typeCounter.get("mapReturnOp").incrementAndGet();
		m.setCategory(Category.RETURN);
		m.extractOp();
		m.extractLabel();
		return m.op;
	}
	
	private static OpCode mapSwitchOp(InstrcutionMatcher m) {
		typeCounter.get("mapSwitchOp").incrementAndGet();
		m.setCategory(Category.SWITCH);
		m.extractLabel();
		m.extractTargets();
		return m.op;
	}
	
	public static interface Mapper{
		public OpCode map(InstrcutionMatcher m);
	}
	
	public static class InstrcutionMatcherFactory{
		
		private final Pattern p;
		
		public InstrcutionMatcherFactory(String pattern){
			this.p = Pattern.compile(pattern);
		}
		
		public InstrcutionMatcher create(String s) {
			return new InstrcutionMatcher(p.matcher(s));
		}
	}
	
	public static class InstrcutionMatcher {
		
		protected final Matcher m;
		private OpCode op;
		
		protected InstrcutionMatcher( Matcher m) {
			this.m = m;
		}
		
		public void setCategory(Category category) {
			op.setCategory(category);
		}
		
		public boolean matches() {
			boolean found = m.find();
			if(found) {
				op = new OpCode();
			}
			return found;
		}
		
		public void extractLabel() {
			op.setLabel(Integer.parseInt(m.group("label")));
		}
		
		public void extractTarget() {
			op.setTargetInstructions(new int[] { Integer.parseInt(m.group("target"))});
		}
		
		public void extractOp() {
			op.setCode(m.group("op"));
		}
		public void extractType() {
			op.setTargetClass(m.group("type"));
		}
		public void extractMethodSignature() {
			op.setTargetMethod(m.group("method"));
		}
		public void extractFieldName() {
			op.setTargetField(m.group("fieldSignature"));
		}
		public void extractArrayOp() {
			op.setArrayOp((!m.group("arrayOp").isEmpty() || op.getTargetClass() == "a") && !"const_null".equals(op.getCode()));
		}
		public void extractTargets(){
			Matcher targets = switchTargets.matcher(m.group("targets"));
			List<Integer> l = new ArrayList<>();
			while(targets.find()) {
				l.add(Integer.parseInt(targets.group("target")));
			}
			int[] a = new int[l.size()];
			for(int i=0; i<l.size(); i++) {
				a[i] = l.get(i);
			}
			op.setTargetInstructions(a);
		}
	}
}

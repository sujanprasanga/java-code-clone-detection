package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.extractors;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.OpCodeBuilder;
import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InstructionExtractor {

	private static final List<Pattern> patterns;
	
	private static final Set<String> nonMatchedCodes = new HashSet<>();
	
	static {
		PropertyUtil propertyUtil = new PropertyUtil();
		patterns = new LinkedList<>();
		patterns.add(Pattern.compile(propertyUtil.getRegExForConditionalOperations()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForConstructorInvoke()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForFieldOperations()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForInstanceOp()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForInvoke()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForInvokeDynamic()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForNewArrayOp()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForOtherOperations()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForPrimitiveOperations()));
		patterns.add(Pattern.compile(propertyUtil.getRegExForReturnOp()));
	}
	
//	public OpCode extract(String)
	
	private OpCodeBuilder createBuilder(Matcher matcher) {
		OpCodeBuilder builder = new OpCodeBuilder();
		builder.setLabel(Integer.parseInt(matcher.group("label")));
		return builder;
	}
}

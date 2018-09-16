package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public class ClassParser {

	public void parse(ClassUnderTransform target){
		extractMethods(target);
	}

	private void extractMethods(ClassUnderTransform target) {
		String disassembledCode = target.getDisassembledCode();
		Matcher m = RegularExpressionUtil.getMethodMatcher(disassembledCode);
		while(m.find()){
			extractMethod(target, disassembledCode.substring(m.start(), m.end()));
		}
	}

	private void extractMethod(ClassUnderTransform target, String substring) {
		new MethodParser(target).parse(substring);
	}

}

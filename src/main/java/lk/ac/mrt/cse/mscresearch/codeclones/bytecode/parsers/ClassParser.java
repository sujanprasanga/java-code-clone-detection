package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public class ClassParser {

	public void parse(ClassUnderTransform target){
		extractMethods(target.getDisassembledCode());
	}

	private void extractMethods(String disassembledCode) {
		Matcher m = RegularExpressionUtil.getMethodMatcher(disassembledCode);
		while(m.find()){
			extractMethod(disassembledCode.substring(m.start(), m.end()));
		}
	}

	private void extractMethod(String substring) {
		new MethodParser().parse(substring);
	}

}

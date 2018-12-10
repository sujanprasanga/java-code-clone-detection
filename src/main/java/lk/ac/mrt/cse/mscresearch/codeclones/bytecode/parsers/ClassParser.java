package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.sound.sampled.Line;

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
			Map<Integer, Integer> lineNumberMapping = extractLineNumbers(m.group(RegularExpressionUtil.LINE_NUMBER_TABLE_CG_NAME));
			extractMethod(target, m.group(RegularExpressionUtil.METHOD_CG_NAME));
		}
	}

	private static Map<Integer, Integer> extractLineNumbers(String s) {
		List<Integer> lines = new ArrayList<>();
		List<Integer> labels = new ArrayList<>();
			Matcher m = RegularExpressionUtil.LINE_NUMBER_TABLE_ENTRY.matcher(s);
			while(m.find()) {
				lines.add(Integer.parseInt(m.group(RegularExpressionUtil.LINE_NUMBER_CG_NAME)));
				labels.add(Integer.parseInt(m.group(RegularExpressionUtil.LABEL_NUMBER_CG_NAME)));
			}
		Map<Integer, Integer> labelToLineNumberMapping = new HashMap<>();
		int prev = labels.get(0);
		for(int i=0; i<lines.size(); i++) {
			int current = labels.get(i);
			for(int j=prev; j<current; j++) {
				labelToLineNumberMapping.put(j, lines.get(i-1));
			}
			labelToLineNumberMapping.put(current, lines.get(i));
		}
		return labelToLineNumberMapping;
	}
	public static void main(String[] args)
	{
		String s = "      line 36: 0\r\n" + 
				"      line 37: 9";
		System.out.println(extractLineNumbers(s));
	}
	
	private void extractMethod(ClassUnderTransform target, String substring) {
		new MethodParser(target).parse(substring);
	}

}

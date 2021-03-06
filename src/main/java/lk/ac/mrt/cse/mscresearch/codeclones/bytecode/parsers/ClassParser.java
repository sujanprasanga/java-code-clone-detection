package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.persistance.entities.ClassIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class ClassParser {

	private static final Map<Integer, Integer> defaultLabelToLineNumberMapping = new HashMap<>();
	static {
		defaultLabelToLineNumberMapping.put(Integer.MAX_VALUE, -1);
	}
	
	private final ClassUnderTransform target;
	private final String className;
	private final String md5Hash;
	
	public ClassParser(ClassUnderTransform target, String className, String md5Hash){
		this.target = target;
		this.className = className;
		this.md5Hash = md5Hash;
	}
	
	private final Set<MethodIndex> methods = new HashSet<>();
	
	public void extractMethods() {
		String disassembledCode = target.getDisassembledCode();
		Matcher m = RegularExpressionUtil.getMethodMatcher(disassembledCode);
		while(m.find()){
			Map<Integer, Integer> lineNumberMapping = extractLineNumbers(m);
			extractMethod(target, m.group(RegularExpressionUtil.METHOD_CG_NAME), lineNumberMapping);
		}
	}

	private static Map<Integer, Integer> extractLineNumbers(Matcher matcher) {
		String s = matcher.group(RegularExpressionUtil.LINE_NUMBER_TABLE_CG_NAME);
		if(s == null || s.isEmpty()) {
			return noLineNUmbers();
		}
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
			prev = current;
		}
		labelToLineNumberMapping.put(Integer.MAX_VALUE, lines.get(lines.size()-1));
		return labelToLineNumberMapping;
	}
	
	private static Map<Integer, Integer> noLineNUmbers() {
		return defaultLabelToLineNumberMapping;
	}

	private void extractMethod(ClassUnderTransform target, String substring, Map<Integer, Integer> lineNumberMapping) {
		MethodParser methodParser = new MethodParser(target, lineNumberMapping);
		methodParser.parse(substring);
		synchronized (this) {
			methods.addAll(methodParser.getMethods());
		}
	}
	
	public ClassIndex getClassIndex(){
		ClassIndex classIndex = new ClassIndex();
		classIndex.setClassName(className);
		classIndex.setClassHash(md5Hash);
		classIndex.setMethods(methods);
		return classIndex;
	}

}

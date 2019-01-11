package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.AccessibleMethodIdentifier;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.MethodTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCodeTransformer;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class ClassParser {

	private MethodSplitter splitter = new MethodSplitter();
	private final AccessibleMethodIdentifier accessibleMethodIdentifier = new AccessibleMethodIdentifier();
	private final OpCodeTransformer opCodeTransformer = new OpCodeTransformer();

	public Set<MethodDTO> extractMethods(String target, String className) {
		final Set<MethodDTO> methods = new HashSet<>();
		MethodTokenizer methodTokenizer = MethodTokenizer.getMethodTokenizer(target);
		while(methodTokenizer.hasNext()){
			String method = methodTokenizer.getNext(); 
			Map<Integer, Integer> lineNumberMapping = extractLineNumbers(method);
			methods.addAll(extractMethod(method, lineNumberMapping, className));
		}
		return methods;
	}

	private static Map<Integer, Integer> extractLineNumbers(String method) {
		String s = null;
		if(s == null || s.isEmpty()) {
			return Collections.emptyMap();
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
	
	private Set<MethodDTO> extractMethod(String method, Map<Integer, Integer> lineNumberMapping, String className) {
	    String methodName = accessibleMethodIdentifier.extractMethodSignature(method);
	    if(methodName == null) {
	    	return Collections.emptySet();
	    }
		List<OpCode> opcodes = splitter.extractMethod(method, lineNumberMapping, className);
		return toMethodDTO(methodName, opcodes,method.length());
	}
	
	private Set<MethodDTO> toMethodDTO(String signature, List<OpCode> opcodes, int size) {
		return opCodeTransformer.transform(signature, opcodes, size);
	}
}

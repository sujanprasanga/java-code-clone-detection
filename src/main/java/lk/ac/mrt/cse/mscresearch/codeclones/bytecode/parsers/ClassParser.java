package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionSorter;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.MethodTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.OpCodeBuilder;
import lk.ac.mrt.cse.mscresearch.persistance.entities.ClassIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class ClassParser {

	public static final Set<String> unmappedCodes = new HashSet<>();
	
	public void extractMethods(String target, String className, String md5Hash) {
		final Set<MethodIndex> methods = new HashSet<>();
		MethodTokenizer methodTokenizer = MethodTokenizer.getMethodTokenizer(target);
		while(methodTokenizer.hasNext()){
			String method = methodTokenizer.getNext(); 
			Map<Integer, Integer> lineNumberMapping = extractLineNumbers(method);
			MethodIndex methodIndex = extractMethod(method, lineNumberMapping, className);
		}
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
	
	private MethodIndex extractMethod(String method, Map<Integer, Integer> lineNumberMapping, String className) {
		InstructionTokenizer instructionTokenizer = InstructionTokenizer.getInstructionTokenizer(method);  
	String methodName = null;
		List<OpCodeBuilder> opcodes = new LinkedList<>();
		if(lineNumberMapping.isEmpty()) {
			while(instructionTokenizer.hasNext()) {
				String next = instructionTokenizer.getNext();
				OpCodeBuilder opcode = toOpcode(next);
				if(opcode == null) {
					noDecoderFound(next, className);
				}
				opcodes.add(opcode);
			}
		} else {
			while(instructionTokenizer.hasNext()) {
				String next = instructionTokenizer.getNext();
				OpCodeBuilder opcode = toOpcode(next, lineNumberMapping);
				if(opcode == null) {
					noDecoderFound(next, className);
				}
				opcodes.add(opcode);
			}
		}
		
		return toMethodIndex(methodName, opcodes);
	}
	
	private static synchronized void noDecoderFound(String next, String className) {
		unmappedCodes.add(className+"#" + next);
	}

	private OpCodeBuilder toOpcode(String next, Map<Integer, Integer> lineNumberMapping) {
		OpCodeBuilder builder = toOpcode(next);
		return builder;
	}

	private OpCodeBuilder toOpcode(String next) {
		return InstructionSorter.decode(next);
	}

	private MethodIndex toMethodIndex(String methodName, List<OpCodeBuilder> opcodes) {
		// TODO Auto-generated method stub
		return null;
	}


	public ClassIndex getClassIndex(){
		ClassIndex classIndex = new ClassIndex();
//		classIndex.setClassName(className);
//		classIndex.setClassHash(md5Hash);
//		classIndex.setMethods(methods);
		return classIndex;
	}

}

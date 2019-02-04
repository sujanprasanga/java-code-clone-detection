package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionSorter;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;

public class MethodSplitter {
	
	public static final Set<String> unmappedCodes = new HashSet<>();
	
	public List<OpCode> extractMethod(String method, Map<Integer, Integer> lineNumberMapping, String className){
		return extractMethod(method, className, s->toOpcode(s, lineNumberMapping));
	}
	
	public List<OpCode> extractMethod(String method, String className){
		return extractMethod(method, className, this::toOpcode);
	}
	
	private List<OpCode> extractMethod(String method, String className, Function<String, OpCode> opCodeFactory){
		InstructionTokenizer instructionTokenizer = InstructionTokenizer.getInstructionTokenizer(method);  
		List<OpCode> opcodes = new LinkedList<>();
		while(instructionTokenizer.hasNext()) {
			String next = instructionTokenizer.getNext();
			OpCode opcode = opCodeFactory.apply(next);
			if(opcode == null) {
				noDecoderFound(next, className);
			}
			opcodes.add(opcode);
		}
		return opcodes;
	}
	
	private static synchronized void noDecoderFound(String next, String className) {
		unmappedCodes.add(className+"#" + next);
	}

	private OpCode toOpcode(String next, Map<Integer, Integer> lineNumberMapping) {
		OpCode builder = toOpcode(next);
		int label = builder.getLabel();
		if(lineNumberMapping.containsKey(label)) {
			builder.setLineNumber(lineNumberMapping.get(label));
		}else {
			builder.setLineNumber(lineNumberMapping.get(Integer.MAX_VALUE));
		}
		return builder;
	}

	private OpCode toOpcode(String next) {
		return InstructionSorter.decode(next);
	}
}
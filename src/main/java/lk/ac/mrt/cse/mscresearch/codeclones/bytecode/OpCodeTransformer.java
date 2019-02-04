package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.DefaultTransformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.Transformer;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class OpCodeTransformer {

	private static final List<Transformer> transformers = new LinkedList<>();
	
	static {
		transformers.add(new DefaultTransformer());
	}
	
	public Set<MethodDTO> transform(String signature, List<OpCode> opcodes, int size){
		return transformers.stream().map(t->t.transform(signature, opcodes, size)).collect(Collectors.toSet());
	}

	public Set<MethodDTO> transformForLocal(String signature, List<OpCode> opcodes, int size) {
		return transformers.stream().map(t->t.transformForLocal(signature, opcodes, size)).collect(Collectors.toSet());
	}
}

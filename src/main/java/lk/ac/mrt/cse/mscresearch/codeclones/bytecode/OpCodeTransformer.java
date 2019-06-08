package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.ConditionalTypeLeniantTransformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.DefaultTransformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.FilterPrimitivesTransformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.InstructionCountBasedTransformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.Transformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers.TypeLeniantTransformer;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class OpCodeTransformer {

	private static final List<Transformer> transformers = new LinkedList<>();
	private static final List<Transformer> transformers_local_segment = new LinkedList<>();
	
	static {
		transformers.add(new DefaultTransformer());
		transformers.add(new TypeLeniantTransformer());
		transformers.add(new FilterPrimitivesTransformer());
		transformers.add(new InstructionCountBasedTransformer());
		transformers.add(new ConditionalTypeLeniantTransformer());
	}
	
	static {
		transformers_local_segment.add(new DefaultTransformer());
		transformers_local_segment.add(new TypeLeniantTransformer());
		transformers_local_segment.add(new FilterPrimitivesTransformer());
		transformers_local_segment.add(new ConditionalTypeLeniantTransformer());
	}
	
	public Set<MethodDTO> transform(String signature, List<OpCode> opcodes, int size){
		return transformers.stream().map(t->t.transform(signature, opcodes, size)).collect(Collectors.toSet());
	}

	public Set<MethodDTO> transformForLocal(String signature, List<OpCode> opcodes, int size) {
		return transformers.stream().map(t->t.transformForLocal(signature, opcodes, size)).collect(Collectors.toSet());
	}

	public Set<MethodDTO> transformForLocalSegment(String signature, List<OpCode> opcodes, int size) {
		return transformers_local_segment.stream().map(t->t.transformForLocalSegment(signature, opcodes, size)).collect(Collectors.toSet());
	}
}

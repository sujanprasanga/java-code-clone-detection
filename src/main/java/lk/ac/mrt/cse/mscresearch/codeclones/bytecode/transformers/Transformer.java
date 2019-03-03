package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.Category;
import lk.ac.mrt.cse.mscresearch.localindex.LocalMethodDTO;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.Hashing;

public interface Transformer {

	default MethodDTO transform(String signature, List<OpCode> opcodes, int size) {
		StringBuilder sb = new StringBuilder();
		opcodes.stream().filter(getFilter()).map(this::transformOpCode).forEach(sb::append);
//		for(OpCode o : opcodes.stream().filter(getFilter()).collect(Collectors.toList())) {
//			sb.append(transformOpCode(o));
//		}
		return toMethodDTO(signature, size, sb);
	}

	default MethodDTO toMethodDTO(String signature, int size, StringBuilder sb) {
		MethodDTO m = new MethodDTO();
		m.setSignature(signature);
		m.setBody(sb.toString());
		m.setBodyhash(Hashing.hash(m.getBody()));
		m.setSize(size);
		m.setPluginid(getPluginId());
		return m;
	}
	
	default String transformOpCode(OpCode o) {
		switch(o.getCategory()) {
		case PRIMITIVE_OP: return transformPrimitiveOp(o);
		case OTHER: return transformOtherOp(o);
		case INVOKE: return transformInvoke(o);
		case CONDITIONAL: return transformConditional(o);
		case FIELD: return transformField(o);
		case TYPE_CHECK: return transformTypeCheck(o);
		case RETURN: return transformReturn(o);
		case NEW_ARRAY: return transformNewArray(o);
		case SWITCH: return transformSwitch(o);
		case INVOKE_DYNAMIC: return transformInvokeDynamic(o);
		}
		return null;
	}

	String transformInvokeDynamic(OpCode o);

	String transformSwitch(OpCode o);

	String transformNewArray(OpCode o);

	String transformReturn(OpCode o);

	String transformTypeCheck(OpCode o);

	String transformField(OpCode o);

	String transformConditional(OpCode o);

	String transformInvoke(OpCode o);

	String transformOtherOp(OpCode o);

	String transformPrimitiveOp(OpCode o);
	
	default String seperator() {
		return "|";
	}
	
	default String newline() {
		return "\n";
	}
	
	int getPluginId();
	
	default Predicate<OpCode> getFilter() {
		return o->o.getCategory() != Category.RETURN;
	}

	default MethodDTO transformForLocal(String signature, List<OpCode> opcodes, int size) {
		StringBuilder sb = new StringBuilder();
		opcodes.stream().filter(getFilter()).map(this::transformOpCode).forEach(sb::append);
		List<Integer> ln = opcodes.stream().filter(getFilter()).map(OpCode::getLineNumber).collect(Collectors.toList());
//		for(OpCode o : opcodes.stream().filter(getFilter()).collect(Collectors.toList())) {
//			sb.append(transformOpCode(o));
//		}
		return toLocalMethodDTO(signature, size, sb, toIntArray(ln));
	};
	
	default int[] toIntArray(List<Integer> ln) {
		int[] l = new int[ln.size()];
		for(int i=0; i<l.length; i++) {
			l[i] = ln.get(i);
		}
		return l;
	}
	
	default LocalMethodDTO toLocalMethodDTO(String signature, int size, StringBuilder sb, int[] l) {
		LocalMethodDTO m = new LocalMethodDTO();
		m.setSignature(signature);
		m.setBody(sb.toString());
		m.setBodyhash(Hashing.hash(m.getBody()));
		m.setSize(size);
		m.setPluginid(getPluginId());
		m.setLineNumbers(l);
		return m;
	}
}

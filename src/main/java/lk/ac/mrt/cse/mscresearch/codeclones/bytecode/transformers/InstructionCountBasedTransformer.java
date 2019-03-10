package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.Category;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class InstructionCountBasedTransformer implements Transformer {

	private static final int pluginId = 4;
	private final Map<Category, Integer> categoryCounts = new EnumMap<>(Category.class);
	
	@Override
	public MethodDTO transform(String signature, List<OpCode> opcodes, int size) {
		StringBuilder sb = groupAndTransform(opcodes);
		return toMethodDTO(signature, size, sb);
	}

	protected StringBuilder groupAndTransform(List<OpCode> opcodes) {
		StringBuilder sb = new StringBuilder();
		Map<Category, List<OpCode>> grouped = opcodes.stream().filter(getFilter()).collect(Collectors.groupingBy(OpCode::getCategory));
		Stream.of(Category.values()).forEach(c->{
			int i = grouped.containsKey(c) ? grouped.get(c).size() : 1;
			categoryCounts.put(c, i);
		});
		Stream.of(Category.values()).filter(categoryCounts::containsKey).forEach(c->{
			sb.append(c.toString()).append(":").append(categoryCounts.get(c)).append(newline());
		});
		return sb;
	}
	
	@Override
	public String transformPrimitiveOp(OpCode o) {
		return "";
	}

	@Override
	public String transformOtherOp(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public String transformInvoke(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public String transformConditional(OpCode o) {
		return "if" + newline();
	}

	@Override
	public String transformField(OpCode o) {
		return o.getCode() + newline();
	}
	
	@Override
	public String transformTypeCheck(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public String transformReturn(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public String transformNewArray(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public String transformSwitch(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public String transformInvokeDynamic(OpCode o) {
		return o.getCode() + newline();
	}

	@Override
	public int getPluginId() {
		return pluginId;
	}

	@Override
	public MethodDTO transformForLocal(String signature, List<OpCode> opcodes, int size) {
		StringBuilder sb = groupAndTransform(opcodes);
		int[] lineNumberRange = {
				opcodes.stream().map(OpCode::getLineNumber).min((x, y)->x-y).get(),
				opcodes.stream().map(OpCode::getLineNumber).max((x, y)->x-y).get()
		};
//		for(OpCode o : opcodes.stream().filter(getFilter()).collect(Collectors.toList())) {
//			sb.append(transformOpCode(o));
//		}
		return toLocalMethodDTO(signature, size, sb, lineNumberRange);
	};
}

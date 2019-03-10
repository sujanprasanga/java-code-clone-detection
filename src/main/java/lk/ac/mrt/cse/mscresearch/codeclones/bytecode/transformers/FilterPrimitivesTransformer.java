package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers;

import java.util.function.Predicate;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.Category;

public class FilterPrimitivesTransformer implements Transformer {

	private static final int pluginId = 3; 
	
	@Override
	public String transformPrimitiveOp(OpCode o) {
		throw new RuntimeException("primitives should be filtered");
	}

	@Override
	public Predicate<OpCode> getFilter() {
		return o-> o.getCategory() != Category.RETURN && 
				   o.getCategory() != Category.PRIMITIVE_OP;
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
		return o.getCode() + newline();
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

}

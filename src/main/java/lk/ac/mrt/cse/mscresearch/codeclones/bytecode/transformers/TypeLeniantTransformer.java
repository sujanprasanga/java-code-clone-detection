package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.transformers;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;

public class TypeLeniantTransformer implements Transformer {

	private static final int pluginId = 2; 
	
	@Override
	public String transformPrimitiveOp(OpCode o) {
		String arrayop = o.isArrayOp() ? seperator() + "A":"";
		return o.getCode() + arrayop + newline();
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

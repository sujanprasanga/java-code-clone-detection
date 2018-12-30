package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.List;
import java.util.Set;

public class OpCode {

	private final int label;
	private final int lineNumber;
	private final String code;
	private final int[] targetInstructions;
	private final String targetClass;
	private final String targetMethod;
	private final String targetField;
	private final boolean optionallyExecuted;
	private final boolean looped;
	private final boolean isLooped;
	private final boolean arrayOp;

	private OpCode(OpCodeBuilder builder) {
		label = builder.label;
		lineNumber = builder.lineNumber;
		code = builder.code;
		targetClass = builder.targetClass;
		targetMethod = builder.targetMethod;
		targetField = builder.targetField;
		optionallyExecuted = builder.optionallyExecuted;
		looped = builder.looped;
		targetInstructions = builder.targetInstructions;
		isLooped = builder.isLooped;
		arrayOp = builder.arrayOp;
	}

	public int getLabel() {
		return label;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getCode() {
		return code;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public String getTargetField() {
		return targetField;
	}

	public boolean isOptionallyExecuted() {
		return optionallyExecuted;
	}

	public boolean isLooped() {
		return looped;
	}

	public int[] getTargetInstructions() {
		return targetInstructions;
	}

	public boolean isArrayOp() {
		return arrayOp;
	}

	public static class OpCodeBuilder {
		public boolean isLooped;
		private int label;
		private int lineNumber;
		private String code;
		private String targetClass;
		private String targetMethod;
		private String targetField;
		private boolean optionallyExecuted;
		private boolean looped;
		private boolean arrayOp;
		private int[] targetInstructions;

		public OpCodeBuilder setLabel(int label) {
			this.label = label;
			return this;
		}

		public OpCodeBuilder setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
			return this;
		}
		
		public OpCodeBuilder setTargetInstructions(int[] targetInstructions) {
			this.targetInstructions = targetInstructions;
			return this;
		}

		public OpCodeBuilder setCode(String code) {
			this.code = code;
			return this;
		}

		public OpCodeBuilder setTargetClass(String targetClass) {
			this.targetClass = targetClass;
			return this;
		}

		public OpCodeBuilder setTargetMethod(String targetMethod) {
			this.targetMethod = targetMethod;
			return this;
		}

		public OpCodeBuilder setTargetField(String targetField) {
			this.targetField = targetField;
			return this;
		}

		public OpCodeBuilder setOptionallyExecuted(boolean optionallyExecuted) {
			this.optionallyExecuted = optionallyExecuted;
			return this;
		}

		public OpCodeBuilder setLooped(boolean looped) {
			this.looped = looped;
			return this;
		}
		
		public OpCodeBuilder setArrayOp(boolean arrayOp) {
			this.arrayOp = arrayOp;
			return this;
		}

		public String getTargetClass() {
			return targetClass;
		}

		public String getCode() {
			return code;
		}

		public OpCode build() {
			return new OpCode(this);
		}
	}
}

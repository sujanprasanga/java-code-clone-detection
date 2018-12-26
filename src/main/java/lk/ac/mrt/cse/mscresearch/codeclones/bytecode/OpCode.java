package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

public class OpCode {

	private final int label;
	private final int lineNumber;
	private final String code;
	private final String targetClass;
	private final String targetMethod;
	private final String targetField;
	private final boolean optionallyExecuted;
	private final boolean looped;

	private OpCode(OpCodeBuilder builder) {
		label = builder.label;
		lineNumber = builder.lineNumber;
		code = builder.code;
		targetClass = builder.targetClass;
		targetMethod = builder.targetMethod;
		targetField = builder.targetField;
		optionallyExecuted = builder.optionallyExecuted;
		looped = builder.looped;
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

	public static class OpCodeBuilder {
		private int label;
		private int lineNumber;
		private String code;
		private String targetClass;
		private String targetMethod;
		private String targetField;
		private boolean optionallyExecuted;
		private boolean looped;

		public void setLabel(int label) {
			this.label = label;
		}

		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
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

	}
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

public class CodeLine {

	private int lineNumber;
	private String instruction;
	private String operands;
	
	public int getLineNumber() {
		return lineNumber;
	}
	public CodeLine setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
		return this;
	}
	public String getInstruction() {
		return instruction;
	}
	public CodeLine setInstruction(String instruction) {
		this.instruction = instruction;
		return this;
	}
	public String getOperands() {
		return operands;
	}
	public CodeLine setOperands(String operands) {
		this.operands = operands;
		return this;
	}
	
	public boolean equals(CodeLine c){
		return CodeLineEquivalentChecker.isEqual(this, c);
	}
}

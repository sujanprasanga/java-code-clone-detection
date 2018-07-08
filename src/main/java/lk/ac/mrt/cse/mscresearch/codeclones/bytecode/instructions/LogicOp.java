package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class LogicOp extends Instruction {

	public LogicOp(int label) {
		super(label, TYPE.LOGIC_OP);
	}

	public static LogicOp from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new LogicOp(label);
	}

    public String toString(){
		return "logicalOperation";
	}
}

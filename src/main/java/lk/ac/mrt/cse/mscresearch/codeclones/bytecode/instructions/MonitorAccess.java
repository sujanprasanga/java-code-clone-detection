package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public class MonitorAccess extends Instruction {

	public MonitorAccess(int label) {
		super(label, TYPE.MONITOR_ACCESS);
	}

	public static Instruction from(InstructionCreateParam p) {
		return from(p.arg, p.matcher);
	}
	
	public static MonitorAccess from(String field, Matcher m){
		int label = getLabelNumber(m);
		return new MonitorAccess(label);
	}

    public String toString(){
		return "monitorAccess";
	}
}

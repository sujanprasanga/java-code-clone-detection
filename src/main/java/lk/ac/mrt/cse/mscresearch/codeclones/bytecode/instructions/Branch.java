package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Branch extends Instruction{

	private final List<Instruction> branchIns = new ArrayList<>();
	
	private Branch(int label) {
		super(label, TYPE.BRANCH);
	}
	
	public static Instruction forIf(String arg, Matcher matcher) {
		return new Branch(getLabelNumber(matcher));
	}
	
	public void add(Instruction i){
		branchIns.add(i);
	}
	
	public String toString(){
		return "Branches" + branchIns.toString();
	}
	
	public boolean isBranching(){
		return true;
	}
	
	public List<Instruction> getBranchInstructions() {
		return branchIns;
	}
}

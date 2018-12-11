package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Branch extends Instruction{

	private final List<Instruction> branchIns = new ArrayList<>();
	
	private Branch(int label) {
		super(label, TYPE.BRANCH);
	}
	
	public static Branch from(InstructionCreateParam p) {
		return forIf(p.arg, p.matcher);
	}
	
	public static Branch forIf(String arg, Matcher matcher) {
		return new Branch(getLabelNumber(matcher));
	}
	
	public void add(Instruction i){
		branchIns.add(i);
	}
	
	public String toString(){
		return "Branch" + branchIns.toString();
	}
	
	public boolean isBranching(){
		return true;
	}
	
	public List<Instruction> getBranchInstructions() {
		return branchIns;
	}
	
	@Override
	public void setLinNumber(Map<Integer, Integer> lineNumberMapping) {
		super.setLinNumber(lineNumberMapping);
		branchIns.forEach(i -> i.setLinNumber(lineNumberMapping));
	}
}

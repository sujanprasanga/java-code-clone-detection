package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.analysers;

import java.util.List;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;

public class GeneralAnalyser {

	private final int branchDepth;
	private final int loopDepth;
	
	private final List<OpCode> opcodes;
	private OpCode analysing;
	
	public GeneralAnalyser(List<OpCode> opcodes, OpCode analysing, int branchDepth, int loopDepth) {
		this.opcodes = opcodes;
		this.analysing = analysing;
		this.branchDepth = branchDepth;
		this.loopDepth = loopDepth;
	}
	
	public void analyse() {
		int current = opcodes.indexOf(analysing);
		for(int i=current; i<opcodes.size(); i++) {
			
		}
	}
}

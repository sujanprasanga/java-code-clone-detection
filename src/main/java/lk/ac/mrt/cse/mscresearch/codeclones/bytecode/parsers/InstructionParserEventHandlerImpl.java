package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import static lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionFactories.forInstruction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Branch;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.LoopMarker;

public class InstructionParserEventHandlerImpl implements InstructionParserEventHandler {
	
	private static final Logger log = Logger.getLogger(InstructionParserEventHandlerImpl.class);
	
	private List<Instruction> instructions = new ArrayList<>();
	private Deque<InstructionDest> branchStack = new ArrayDeque<>();

	public InstructionParserEventHandlerImpl(String[] params) {
		branchStack.push((Instruction i)-> instructions.add(i));
	}

	@Override
	public void notifyLoop(int loopStartLabel, int loopEndLabel) {
		addInstruction(LoopMarker.from(loopStartLabel, loopEndLabel));
	}

	@Override
	public void notifyBranchStart() {
	}

	@Override
	public void notifyBranchEnd() {
		branchStack.pop();
	}

	@Override
	public void notifyMatch(String key, Matcher matcher) {
		log.debug("------------------------------------------");
		Instruction i = forInstruction(key).create(key, matcher, null);
		addInstruction(i);
		
		if(i.isBranching()){
			final Branch b = (Branch)i;
			branchStack.push((Instruction ii)-> b.add(ii));
		}
		log.debug(i);
	}

	@Override
	public List<Instruction> get() {
		return instructions;
	}
	
	private void addInstruction(Instruction i) {
		InstructionDest peek = branchStack.peek();
		peek.add(i);
	}
	
	@FunctionalInterface
	static interface InstructionDest{
		void add(Instruction i);
	}

	@Override
	public void notifyEnd() {
		// TODO Auto-generated method stub
//		add bipush
	}

}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import static lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionFactories.forInstruction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Branch;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction.TYPE;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionCreateParam;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Loop;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.LoopMarker;

public class InstructionParserEventHandlerImpl implements InstructionParserEventHandler {
	
	private static final Logger log = Logger.getLogger(InstructionParserEventHandlerImpl.class);
	private final ClassUnderTransform target;
	private List<Instruction> instructions = new ArrayList<>();
	private Deque<InstructionDest> branchStack = new ArrayDeque<>();
	

	public InstructionParserEventHandlerImpl(ClassUnderTransform target, String[] params) {
		this.target = target;
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
		Instruction i = forInstruction(key).create(new InstructionCreateParam(target, key, matcher, null));
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
		replaceLoopMarkers(get());
	}

	protected void replaceLoopMarkers(List<Instruction> all) {
		List<LoopMarker> loops = new ArrayList<>();
		for(Instruction i : all){
			if(i instanceof LoopMarker){
				loops.add((LoopMarker)i);
			}
			if(i instanceof Branch){
				replaceLoopMarkers(((Branch) i).getBranchInstructions());
			}
			
		}
		for(LoopMarker marker : loops){
			replaceLoop(all, marker);
		}
	}

	private void replaceLoop(List<Instruction> all, LoopMarker marker) {
		int start = marker.getStart();
		int end = marker.getEnd();
		boolean startFound = false;
		List<Instruction> loopInstructions = new ArrayList<>();
		for(Instruction i : all){
			if(start == i.getLabel()){
				startFound = true;
			}
			if(startFound){
				loopInstructions.add(i);
				if(end == i.getLabel()){
					break;
				}
			}
			
		}
		try{
			int markerIndex = all.indexOf(loopInstructions.get(0));
			all.removeAll(loopInstructions);
			loopInstructions.remove(marker);
			all.add(markerIndex, new Loop(start, loopInstructions.toArray(new Instruction[0])));
		}catch(Exception e){
			e.getMessage();
		}
	}

}

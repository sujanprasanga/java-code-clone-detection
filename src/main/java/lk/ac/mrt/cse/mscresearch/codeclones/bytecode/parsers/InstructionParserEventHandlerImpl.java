package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import static lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionFactories.forInstruction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Branch;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;

public class InstructionParserEventHandlerImpl implements InstructionParserEventHandler {
	
	private static final Logger log = Logger.getLogger(InstructionParserEventHandlerImpl.class);
	
	private List<Instruction> instructions = new ArrayList<>();
	private Map<Integer, String> localVarTable = new HashMap<>();
	private Deque<String> stack = new ArrayDeque<>();
	private Deque<InstructionDest> branchStack = new ArrayDeque<>();

	public InstructionParserEventHandlerImpl(String[] params) {
		for(int i=0; i<params.length; i++){
			localVarTable.put(i, params[i].trim());
		}
		branchStack.push((Instruction i)-> instructions.add(i));
	}

	@Override
	public void notifyLoop(int i, int j) {
		// TODO Auto-generated method stub

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
		log.debug(stack);
		Instruction i = forInstruction(key).create(key, matcher, stack.peek());
		if(i.localVarTableModifier()){
			localVarTable.put(i.getLocalVarIndex() - 1, stack.peek());
		}
		if(i.stackPop()){
			stack.pop();
		}
		if(i.localVarLoader()){
			stack.push(localVarTable.get(i.getLocalVarIndex() - 1));
		}
		if(i.stackPush()){
			stack.push(i.getLocalVar());
		}
		if(i.duplicateStack()){
			final int dupIndex = i.getDupIndex();
			String[] tmp = new String[dupIndex];
			for(int j=0; j<dupIndex; j++){
				tmp[j] = stack.pop();
			}
			String toDup = stack.peek();
			for(int j=dupIndex - 1; j>0; j--){
				stack.push(tmp[j]);
			}
			stack.push(toDup);
		}
		
		addInstruction(i);
		
		if(i.isBranching()){
			final Branch b = (Branch)i;
			branchStack.push((Instruction ii)-> b.add(ii));
		}
		log.debug(i);
		log.debug(localVarTable);
		log.debug(stack);
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
}

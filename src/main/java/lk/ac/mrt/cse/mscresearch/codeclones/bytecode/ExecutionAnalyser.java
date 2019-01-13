package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.Category;

public class ExecutionAnalyser {

	private final List<OpCode> opcodes;
	private final Map<Integer, OpCode> labelOpCodeMap;
	private final Set<OpCode> optionals = new HashSet<>();
	private final Set<OpCode> looped = new HashSet<>();
	
	public ExecutionAnalyser(List<OpCode> opcodes) {
		this.opcodes = opcodes;
		this.labelOpCodeMap = opcodes.stream().collect(Collectors.toMap(OpCode::getLabel, Function.identity()));
	}
	
	public void analyseExecutions() {
		analyse(0, opcodes.size(), 0, 0);
	}

	private void analyse(int start, int end, int conditionalDepth, int loopDepth) {
		for(int i=start; i<end; i++) {
			OpCode o = opcodes.get(i); 
			o.setOptionalDepth(conditionalDepth);
			o.setLoopDepth(loopDepth);
			if(!isControl(i)) {
				continue;
			}
			int[] targets = o.getTargetInstructions();
			if(targets.length == 1) {
				int label = o.getLabel();
				OpCode targetOp = labelOpCodeMap.get(targets[0]);
				int t = opcodes.indexOf(targetOp);
				if(label < targetOp.getLabel()) {
					if(o.getCode().equalsIgnoreCase("goto")) {
						analyse(t, end, conditionalDepth, loopDepth);
					} else {
						analyse(i+1, t, conditionalDepth + 1, loopDepth);
						analyse(t, end, conditionalDepth + 1, loopDepth);
					}
					break;
				} else {
					o.setLoopDepth(loopDepth + 1);
					analyse(t, i, conditionalDepth, loopDepth + 1);
				}
			}
		}
		
	}
	
	private boolean isControl(int i) {
		return isControl(opcodes.get(i));
	}

	private boolean isControl(OpCode opcode) {
		Category category = opcode.getCategory();
		return category == Category.CONDITIONAL || category == Category.SWITCH;
	}

	private void analyseTargets(OpCode opcode, boolean optional, boolean looped) {
		// TODO Auto-generated method stub
		
	}
}

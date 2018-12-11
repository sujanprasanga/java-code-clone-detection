package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Branch;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;

public class BranchParser extends AbstractInstructionParser{

	private int index;
	private int nextIndex;
	private final Branch branch;
	private final String nextBranchLabel;
	
	public BranchParser(ClassUnderTransform target, String[] body, int startIndex , Matcher matcher) {
		super(target, body, new String[0], startIndex);
		index = startIndex;
		nextIndex = startIndex;
		this.branch = (Branch)createInstruction(matcher, RegularExpressionUtil.IF);
		nextBranchLabel = matcher.group(RegularExpressionUtil.BRANCH_DEST);
	}

	public int getNextIndex() {
		return nextIndex;
	}

	@Override
	protected List<Instruction> parse() {
		while(index < body.length)
		{
			doParse();
		}
		return branch.getBranchInstructions();
	}

	@Override
	protected boolean handleMatch(String key, Matcher matcher) {
		if(nextBranchLabel.equals(matcher.group(RegularExpressionUtil.INSTRUCTION_LABEL_CG_NAME))){
			handleBranch(matcher, key);
			return true;
		}
		else{
			return false;
		}
	}

	private void handleBranch(Matcher matcher, String key) {
		branch.add(instructions);
		instructions = new ArrayList<>();
		extractInstruction(matcher, key);
	}
}

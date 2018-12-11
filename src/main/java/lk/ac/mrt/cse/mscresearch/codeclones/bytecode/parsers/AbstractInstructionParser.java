package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionCreateParam;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionFactories;

public abstract class AbstractInstructionParser {

	protected final static Map<String, Pattern>  instructionPatterns = RegularExpressionUtil.getInstructionPatterns();
	protected final ClassUnderTransform target;
	protected List<Instruction> instructions = new ArrayList<>();
	protected final String[] body;
	protected final int startIndex;
	protected int index;
	protected int nextIndex;
	protected String[] params; 
	
	public AbstractInstructionParser(ClassUnderTransform target, String[] body, String[] params, int startIndex) {
		this.target = target;
		this.body = body;
		this.startIndex = startIndex;
		this.params = params;
	}
	
	protected void doParse() {
		for(Entry<String, Pattern> entry : instructionPatterns.entrySet()){
			String i = body[index];
			Matcher matcher = entry.getValue().matcher(i);
			if(matcher.find()){
				String key = entry.getKey();
				if(!handleMatch(key, matcher)){
					extractInstruction(matcher, key);
				}
			}
		}
		index++;
	}

	protected boolean handleMatch(String key, Matcher matcher) {
		return false;
	}

	protected void extractInstruction(Matcher matcher, String key) {
		if(isBranchOrLoop(key)){
			extractBranchOrLoop(matcher, key);
		} else {
			extractSingle(matcher, key);
		}
	}

	protected boolean isBranchOrLoop(String key) {
		return RegularExpressionUtil.isBranchOrLoop(key);
	}

	protected void extractBranchOrLoop(Matcher matcher, String key) {
		if(isBranch(matcher, key)){
//			parseBranch(matcher);
		}
	}

	protected Instruction createInstruction(Matcher matcher, String key) {
		return InstructionFactories.forInstruction(key).create(new InstructionCreateParam(target, body[index], matcher, null));
	}

	protected boolean isBranch(Matcher matcher, String key) {
		// TODO Auto-generated method stub
		return key.equals("if");
	}

	protected void parseBranch(Matcher matcher) {
		BranchParser parser = new BranchParser(target, body, index, matcher);
		parser.parse();
		index = parser.getNextIndex();
	}
	
	protected void extractSingle(Matcher matcher, String key) {
		Instruction i = InstructionFactories.forInstruction(key).create(new InstructionCreateParam(target, body[index], matcher, null));
		i.resolvePreConditions(body, index);
		instructions.add(i);
	}
	
	protected abstract List<Instruction> parse();
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.List;
import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;

public interface InstructionParserEventHandler {

	void notifyLoop(int i, int j);
	void notifyBranchStart();
	void notifyBranchEnd();
	void notifyMatch(String key, Matcher matcher);
	List<Instruction> get();
}

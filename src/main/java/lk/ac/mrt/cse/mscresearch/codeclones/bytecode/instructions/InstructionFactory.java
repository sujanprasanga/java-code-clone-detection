package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

public interface InstructionFactory<E extends Instruction> {
	E create(String arg, Matcher matcher, String topOfStack);
}

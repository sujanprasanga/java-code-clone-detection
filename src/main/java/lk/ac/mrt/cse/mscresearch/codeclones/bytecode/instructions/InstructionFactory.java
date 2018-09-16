package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

public interface InstructionFactory<E extends Instruction> {
	E create(InstructionCreateParam parameters);
}

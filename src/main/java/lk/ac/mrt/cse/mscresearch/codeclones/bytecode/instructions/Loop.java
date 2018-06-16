package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

public class Loop extends Instruction{

	private final Instruction[] loop;
	
	public Loop(Instruction[] loop) {
		super(0, TYPE.LOOP);
		this.loop = loop;
	}
}

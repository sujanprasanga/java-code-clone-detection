package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

public class LoopMarker extends Instruction{

	private final int start;
	private final int end;
	
	private LoopMarker(int start, int end) {
		super(end, TYPE.LOOP);
		this.start = start;
		this.end = end;
	}

	public static Instruction from(int start, int end) {
		return new LoopMarker(start, end);
	}
	
	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
	
	public String toString(){
		return "loop from " + start + " to " + end;
	}
}

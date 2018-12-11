package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.Map;
import java.util.stream.Stream;

public class Loop extends Instruction{

	private final Instruction[] loop;
	
	public Loop(int start, Instruction[] loop) {
		super(start, TYPE.LOOP);
		this.loop = loop;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("loop [");
		for(int i=0; i<loop.length-1; i++){
			sb.append(loop[i].toString()).append(',');
		}
		sb.append(loop[loop.length - 1].toString()).append(']');
		return sb.toString();
	}
	
	@Override
	public void setLinNumber(Map<Integer, Integer> lineNumberMapping) {
		super.setLinNumber(lineNumberMapping);
		Stream.of(loop).forEach(i -> i.setLinNumber(lineNumberMapping));
	}
}
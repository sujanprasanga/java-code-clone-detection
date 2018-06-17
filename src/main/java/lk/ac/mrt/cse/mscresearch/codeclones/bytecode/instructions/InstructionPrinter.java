package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.List;

import org.apache.log4j.Logger;

public class InstructionPrinter {

	private static final Logger log = Logger.getLogger(InstructionPrinter.class);
	
	private final List<Instruction> instructions;
    private int tabCount = 0;
    private final StringBuilder sb = new StringBuilder();
	
	public InstructionPrinter(List<Instruction> instructions){
		this.instructions = instructions;
	}
	
	public void print(){
		print(instructions);
		log.debug(instructions);
		log.debug(sb.toString());
	}
	
	public void print(List<Instruction> instructions){
		for(Instruction i : instructions){
			print(i);
		}
	}

	private void print(Instruction i) {
		if(i.isBranching()){
			tabCount++;
			for(Instruction l : ((Branch)i).getBranchInstructions()){
				print(l);
			}
			tabCount--;
		} else {
			sb.append('\n');
			for(int j=0; j<tabCount; j++){
				sb.append("--");
			}
			sb.append(i.getType()).append(i.getLabel()).append(i.toString());
		}
		
	}
}

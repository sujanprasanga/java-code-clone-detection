package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.List;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.Instruction;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionPrinter;

public class MethodBodyParser extends AbstractInstructionParser{

	public MethodBodyParser(ClassUnderTransform target, String[] body, String[] params) {
		super(target, body, params, 2);
	}

	public List<Instruction> parse() {
//		while(index < body.length)
//		{
//			log.debug("parsing: " + body[index]);
//			doParse();
//		}
		return new InstructionParser(target, body, params, 2).parse();
//		new InstructionPrinter(instructions).print();
	}

	

}

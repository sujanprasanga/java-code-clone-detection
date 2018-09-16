package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionPrinter;

public class MethodBodyParser extends AbstractInstructionParser{

	public MethodBodyParser(ClassUnderTransform target, String[] body, String[] params) {
		super(target, body, params, 2);
	}

	public void parse() {
//		while(index < body.length)
//		{
//			log.debug("parsing: " + body[index]);
//			doParse();
//		}
		new InstructionParser(target, body, params, 2).parse();
		new InstructionPrinter(instructions).print();
	}

	

}

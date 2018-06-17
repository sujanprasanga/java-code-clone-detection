package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions.InstructionPrinter;

public class MethodBodyParser extends AbstractInstructionParser{

	public MethodBodyParser(String[] body, String[] params) {
		super(body, params, 2);
	}

	public void parse() {
//		while(index < body.length)
//		{
//			log.debug("parsing: " + body[index]);
//			doParse();
//		}
		new InstructionParser(body, params, 2).parse();
		new InstructionPrinter(instructions).print();
	}

	

}

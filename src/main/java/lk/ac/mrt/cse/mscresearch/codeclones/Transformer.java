package lk.ac.mrt.cse.mscresearch.codeclones;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform.STAGE;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.ClassParser;
import lk.ac.mrt.cse.mscresearch.util.CommandLineUtil;

public class Transformer {

	private static final Logger log = Logger.getLogger(Transformer.class);
	
	private final CommandLineUtil commandLineUtil = new CommandLineUtil();

	public void disassemble(ClassUnderTransform target)
	{
		String disassembledClass = commandLineUtil.disassembleClass(target.getFullyQualifiedName(), target.getClassPath());
		target.setDisassembledCode(disassembledClass);
		target.setStage(STAGE.DISASSEMBLED);
	}
	
	public void removeCommentsAndLVTable(ClassUnderTransform target)
	{
		target.setDisassembledCode(target.getDisassembledCode());
	}
	
	public void splitMethods(ClassUnderTransform target)
	{
		
	}
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("log4j.properties");
		ClassUnderTransform c = new ClassUnderTransform();
		c.setClassPath("D:\\workspace\\personal\\msc-research\\java-code-clone-detection\\target\\classes");
		String fullyQualifiedClassName = "lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.InstructionParserEventHandlerImpl";
		c.setFullyQualifiedName(fullyQualifiedClassName);
		Transformer t = new Transformer();
		t.disassemble(c);
		log.debug(c.getDisassembledCode());
		t.removeCommentsAndLVTable(c);
		new ClassParser().parse(c);
	}
}

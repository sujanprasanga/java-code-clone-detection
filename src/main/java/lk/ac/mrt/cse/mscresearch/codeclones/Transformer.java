package lk.ac.mrt.cse.mscresearch.codeclones;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform.STAGE;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.ClassParser;
import lk.ac.mrt.cse.mscresearch.util.IOUtil;

public class Transformer {

	private static final Logger log = Logger.getLogger(Transformer.class);
	
	private final IOUtil commandLineUtil = new IOUtil();

	public void disassemble(ClassUnderTransform target)
	{
		String disassembledClass = commandLineUtil.disassembleClass(target.getFullyQualifiedName(), target.getClassPath());
		target.setDisassembledCode(disassembledClass);
		target.setStage(STAGE.DISASSEMBLED);
	}
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("log4j.properties");
		ClassUnderTransform c = new ClassUnderTransform();
		c.setClassPath("D:\\development\\msc-research\\Temp\\7A9E4B32BD67E7E16240D8B169298B8E");
		c.setFullyQualifiedName("com.sun.security.sasl.digest.DigestMD5Server");
//		c.setClassPath("D:\\workspace\\personal\\msc-research\\java-code-clone-detection\\target\\classes");
//		String fullyQualifiedClassName = "lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.InstructionParserEventHandlerImpl";
//		c.setFullyQualifiedName(fullyQualifiedClassName);
		Transformer t = new Transformer();
		t.disassemble(c);
		log.debug(c.getDisassembledCode());
//		new ClassParser(c,"name", "hash").extractMethods();
	}
}

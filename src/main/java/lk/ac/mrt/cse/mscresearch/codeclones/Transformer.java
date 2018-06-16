package lk.ac.mrt.cse.mscresearch.codeclones;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform.STAGE;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.ClassParser;
import lk.ac.mrt.cse.mscresearch.util.CommandLineUtil;

public class Transformer {

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
		ClassUnderTransform c = new ClassUnderTransform();
		c.setClassPath("D:\\development\\msc-research\\ExampleClasses\\bin");
		c.setFullyQualifiedName("lk.clones.SingleM");
		Transformer t = new Transformer();
		t.disassemble(c);
		System.out.println(c.getDisassembledCode());
		t.removeCommentsAndLVTable(c);
		new ClassParser().parse(c);
	}
}

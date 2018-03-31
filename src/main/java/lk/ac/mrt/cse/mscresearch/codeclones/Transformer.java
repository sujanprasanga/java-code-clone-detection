package lk.ac.mrt.cse.mscresearch.codeclones;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform.STAGE;
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
		target.setDisassembledCode(RegularExpressionUtil.filterNonEssential(target.getDisassembledCode()));
//		String[] lines = target.getDisassembledCode().split("\n");
//		for(int i=0; i<lines.length; i ++)
//		{
//			String tmp = lines[i];
//			int k = tmp.indexOf("//");
//			if(k>=0)
//			{
//				lines[i] = tmp.substring(0, k);
//			}
//		}
//		StringBuilder sb = new StringBuilder();
//		boolean isLVTable = false;
//		for(String l : lines)
//		{
//			if(l.trim().startsWith("LocalVariableTable:"))
//			{
//				isLVTable = true;
//			}
//			if(l.startsWith("\n"))
//			{
//				isLVTable = false;
//			}
//			if(!isLVTable)
//			{
//				sb.append(l.trim()).append('\n');
//			}
//		}
//		if(sb.length() > 0)
//		{
//			sb.setLength(sb.length() - 1);
//		}
//		target.setDisassembledCode(sb.toString());
//		target.setStage(STAGE.FILTERED);
	}
	
	public void splitMethods(ClassUnderTransform target)
	{
		
	}
	
	public static void main(String[] args) {
		ClassUnderTransform c = new ClassUnderTransform();
		c.setClassPath("D:\\development\\msc-research\\ExampleClasses\\bin");
		c.setFullyQualifiedName("lk.clones.Example");
		Transformer t = new Transformer();
		t.disassemble(c);
		System.out.println(c.getDisassembledCode());
		t.removeCommentsAndLVTable(c);
//		System.out.println(c.getDisassembledCode());
	}
}

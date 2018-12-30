package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.codeclones.Util;

public class InstructionTokenizerTest {

	private String method = " public byte[] encode(byte[]);\r\n" + 
			"    Code:\r\n" + 
			"       0: aload_1\r\n" + 
			"       1: invokestatic  #2                  // Method toAsciiBytes:([B)[B\r\n" + 
			"       4: areturn\r\n";
	

	@Test
	public void testinvoke() throws IOException{
		InstructionTokenizer instructionTokenizer = InstructionTokenizer.getInstructionTokenizer(Util.readFile("mixOfinstructions"));
		int i =0;
		while(instructionTokenizer.hasNext()) {
			System.out.println(instructionTokenizer.getNext());
			i++;
		}
		assertEquals(i, 10);
	}
	
	@Test
	public void testinvoke_infile() throws IOException{
		InstructionTokenizer instructionTokenizer = InstructionTokenizer.getInstructionTokenizer(method);
		int i =0;
		while(instructionTokenizer.hasNext()) {
			System.out.println(instructionTokenizer.getNext());
			i++;
		}
		assertEquals(i, 3);
	}
	
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.codeclones.Util;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.MethodSplitter;

public class ExecutionAnalyserTest {

	@Test(dataProvider="exec")
	public void test(String file, int[] optionalDepth, int[] loopDepth) throws IOException {
		String s = Util.readFile(file);
		List<OpCode> code = new MethodSplitter().extractMethod(s, "method");
		assertEquals(code.size(), optionalDepth.length);
		new ExecutionAnalyser(code).analyseExecutions();
		String actualOptD="";
		String actualLoopD="";
		for(int i=0; i<code.size(); i++) {
			OpCode o = code.get(i);
			actualOptD += o.getOptionalDepth()+",";
			actualLoopD += o.getLoopDepth()+",";
		}
		assertEquals(actualOptD, toString(optionalDepth));
		assertEquals(actualLoopD, toString(loopDepth));
	}
	
	private String toString(int[] a) {
		String s="";
		for(int i:a) {
			s+=i+",";
		}
		return s;
	}
	
	
	@DataProvider(name="exec")
	public Object[][] data(){
		return new Object[][] {
//			{"ExecutionAnalyser-if-no-else", new int[]{0,0,1,1,1,1}, 
//                new int[]{0,0,0,0,0,0}},
//			{"ExecutionAnalyser-cascaded-if", new int[]{0,0,0,1,1,1,2,2,2,2,1,1,1,2,2,2,2,2,2,2,2,2,2}, 
//                                              new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}},
			{"ExecutionAnalyser-loop-for", new int[]{0,0,0,1,1,1,1,1,1,1,1,0,0}, 
                                           new int[]{0,0,0,1,1,1,1,1,1,1,1,0,0}},
		};
	}
}

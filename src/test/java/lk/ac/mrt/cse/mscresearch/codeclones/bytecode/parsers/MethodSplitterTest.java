package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.codeclones.Util;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;

public class MethodSplitterTest {
	
	private MethodSplitter underTest = new MethodSplitter();
	
	//{label}{lineNumber}{code}{targetInstructions}{targetClass}{targetMethod}{targetField}{optionallyExecuted}{looped}{arrayOp}
	
	@Test
	public void test() throws IOException {
		String method = Util.readFile("MethodSplitterTestData-1");
		List<OpCode> extracted = underTest.extractMethod(method, "class-name");
		Object[][] expected = resultForTestData1();
		assertEquals(extracted.size(), expected.length);
		for(int i=0; i<extracted.size(); i++) {
			OpCode o = extracted.get(i);
			assertLabel(o, expected[i][0]);
			assertCode(o, expected[i][1]);
			assertTargetClass(o, expected[i][2]);
			assertTargetField(o, expected[i][3]);
			assertTargetMethod(o, expected[i][4]);
		}
	}
	
	private void assertTargetMethod(OpCode o, Object object) {
		assertEquals(o.getTargetMethod(), object);
	}

	private void assertTargetField(OpCode o, Object object) {
		assertEquals(o.getTargetField(), object);
	}

	private void assertTargetClass(OpCode o, Object object) {
		assertEquals(o.getTargetClass(), object);
	}

	private void assertCode(OpCode o, Object object) {
		assertEquals(o.getCode(), object.toString().trim());
	}

	private void assertLabel(OpCode o, Object object) {
		assertEquals(o.getLabel(), (int)object);
	}

	private Object[][] resultForTestData1(){
		return new Object[][] {
		    {   0, "load      "    ,"a"                                                                                   ,   null, null},
		    {   1, "load      "    ,"a"                                                                                   ,   null, null},
		    {   2, "get       "    ,null                                                                                  ,   "I", null},
		    {   5, "put       "    ,null                                                                                   ,   "I", null},
		    {   8, "load      "    ,"a"                                                                                   ,   null, null},
		    {   9, "load      "    ,"i"                                                                                   ,   null, null},
		    {  10, "put       "    ,null                                                                                   ,   "I", null},
		    {  13, "new          " , "com/sun/java_cup/internal/runtime/Symbol"                                            ,   null, null},
		    {  16, "dup          " , null                                                                                 ,   null, null},
		    {  17, "load      "    ,"i"                                                                                   ,   null, null},
		    {  18, "load      "    ,"a"                                                                                   ,   null, null},
		    {  19, "invoke    "    ,"com/sun/java_cup/internal/runtime/Symbol"                                            ,   null, "\"<init>\":(ILjava/lang/Object;)V"},
		    {  22, "return		 " ,null                                                                                  ,   null  ,   null, null},
		};
	}
}

package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionSorter;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InvokeDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForInvoke());
	
	@Test(dataProvider="invokeDataSet")
	public void testinvoke(String code, int label, String clazz, String method){
		OpCode opCode = InstructionSorter.decode(code).build();
		assertEquals(opCode.getTargetMethod(), method);
		assertEquals(opCode.getTargetClass(), clazz);
		assertEquals(opCode.getLabel(), label);
	}
	
	@DataProvider(name = "invokeDataSet")
	  public static Object[][] methods() {
	        return new Object[][] { 
{"  1: invokevirtual #19                 // Method java/lang/String.length:()I                                                         ",1  ,"java/lang/String","length:()I"},
{"  7: invokevirtual #25                 // Method java/lang/String.substring:(I)Ljava/lang/String;                                    ",7  ,"java/lang/String","substring:(I)Ljava/lang/String;"},
{"  31: invokevirtual #30                 // Method java/lang/Object.hashCode:()I		                                               ",31 ,"java/lang/Object","hashCode:()I"},
{"  39: invokevirtual #39                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V                                  ",39 ,"java/io/PrintStream","println:(Ljava/lang/Object;)V"},
{"  59: invokestatic  #53                 // Method java/util/ServiceLoader.load:(Ljava/lang/Class;)Ljava/util/ServiceLoader;          ",59 ,"java/util/ServiceLoader","load:(Ljava/lang/Class;)Ljava/util/ServiceLoader;"},
{"  85: invokeinterface #71,  2           // InterfaceMethod java/util/function/Consumer.accept:(Ljava/lang/Object;)V                  ",85 ,"java/util/function/Consumer","accept:(Ljava/lang/Object;)V"},
{"  100: invokevirtual #78                 // Method java/lang/String.toCharArray:()[C                                                 ",100,"java/lang/String","toCharArray:()[C"},
{"  103: invokestatic  #82                 // InterfaceMethod java/util/stream/Stream.of:(Ljava/lang/Object;)Ljava/util/stream/Stream; ",103,"java/util/stream/Stream","of:(Ljava/lang/Object;)Ljava/util/stream/Stream;"},
{"  113: invokeinterface #91,  2           // InterfaceMethod java/util/stream/Stream.forEach:(Ljava/util/function/Consumer;)V	       ",113,"java/util/stream/Stream","forEach:(Ljava/util/function/Consumer;)V"},
{"  75: invokevirtual #67                 // Method foo:(Ljava/lang/String;)V", 75, null,"foo:(Ljava/lang/String;)V"},
{"  155: invokevirtual #111                // Method lk/clones/Invoke$A.foo$:(Llk/clones/Invoke$A;)Llk/clones/Invoke$A;", 155, "lk/clones/Invoke$A",  "foo$:(Llk/clones/Invoke$A;)Llk/clones/Invoke$A;"},
{"    5: invokestatic  #3                  // Method org/apache/commons/codec/digest/Sha2Crypt.sha512Crypt:([B)Ljava/lang/String;", 5, "org/apache/commons/codec/digest/Sha2Crypt", "sha512Crypt:([B)Ljava/lang/String;"},
	        };
	  }
	
	@Test(dataProvider = NonMatchingDataProvider.DATA_PROVIDER_NAME)
	public void test_other_code_is_not_matched(String code) {
		Matcher matcher = p.matcher(code);
		assertFalse(matcher.find());
	}
	
	@DataProvider(name = NonMatchingDataProvider.DATA_PROVIDER_NAME)
	public Object[][] nonMatchingDataSet(){
		return new NonMatchingDataProvider(getClass()).getCode();
	}
}

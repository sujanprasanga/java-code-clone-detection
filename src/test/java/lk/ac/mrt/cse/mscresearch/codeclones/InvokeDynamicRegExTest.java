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

public class InvokeDynamicRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForInvokeDynamic());
	
	@Test(dataProvider="invokeDataSet")
	public void testinvoke(String code, int label){
		OpCode opCode = InstructionSorter.decode(code).build();
		assertEquals(opCode.getLabel(), label);
	}
	
	@DataProvider(name = "invokeDataSet")
	  public static Object[][] methods() {
	        return new Object[][] { 
{"  75: invokedynamic #70,  0             // InvokeDynamic #0:accept:()Ljava/util/function/Consumer;                           " ,75},
{"  111: invokedynamic #92,  0             // InvokeDynamic #1:accept:(Ljava/lang/StringBuilder;)Ljava/util/function/Consumer; ", 111},
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

package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InvokeDynamicRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForInvokeDynamic());
	
	@Test(dataProvider="invokeDataSet")
	public void testinvoke(String code, int label){
		Matcher matcher = p.matcher(code);
		assertTrue(matcher.find());
		assertEquals(Integer.parseInt(matcher.group("label")), label);
	}
	
	@DataProvider(name = "invokeDataSet")
	  public static Object[][] methods() {
	        return new Object[][] { 
{"  75: invokedynamic #70,  0             // InvokeDynamic #0:accept:()Ljava/util/function/Consumer;                           " ,75},
{"  111: invokedynamic #92,  0             // InvokeDynamic #1:accept:(Ljava/lang/StringBuilder;)Ljava/util/function/Consumer; ", 111},
	        };
	  }
}
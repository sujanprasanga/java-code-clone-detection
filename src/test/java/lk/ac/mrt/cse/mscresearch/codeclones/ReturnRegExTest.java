package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class ReturnRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForReturnOp());
	
	@Test(dataProvider="bytecode")
	public void testinvoke(String code, int label){
		Matcher matcher = p.matcher(code);
		assertTrue(matcher.find());
		assertEquals(Integer.parseInt(matcher.group("label")), label);
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
	            {"       4: return     ", 4},
	            {"       4: ireturn    ", 4},
	            {"       4: lreturn    ", 4},
	            {"       4: freturn    ", 4},
	            {"       4: dreturn    ", 4},
	            {"       4: areturn    ", 4},
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
    
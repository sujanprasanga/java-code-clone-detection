package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InstanceRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForInstanceOp());
	
	@Test(dataProvider="bytecode")
	public void testinvoke(String code, int label, String op, String type){
		Matcher matcher = p.matcher(code);
		assertTrue(matcher.find());
		assertEquals(Integer.parseInt(matcher.group("label")), label);
		assertEquals(matcher.group("type"), type);
		assertEquals(matcher.group("op"), op);
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
{"      7: checkcast     #16                 // class lk/clones/Other$A$         ", 7 , "checkcast", "lk/clones/Other$A$"},
{"     12: instanceof    #16                 // class lk/clones/Other$A$         ",12 , "instanceof", "lk/clones/Other$A$"},
{"      7: checkcast     #16                 // class java/lang/String           ", 7 , "checkcast", "java/lang/String"},
{"     12: instanceof    #16                 // class java/lang/String           ",12 , "instanceof", "java/lang/String"},
{"     64: new           #34                 // class java/lang/RuntimeException ",64 , "new", "java/lang/RuntimeException"},
{"     54: new           #29                 // class lk/clones/Other$A$         ",54 , "new", "lk/clones/Other$A$"},
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
    
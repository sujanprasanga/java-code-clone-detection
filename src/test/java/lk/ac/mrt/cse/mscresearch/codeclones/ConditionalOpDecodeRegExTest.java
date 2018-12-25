package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class ConditionalOpDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForConditionalOperations());
	
	@Test(dataProvider="bytecode")
	public void testinvoke(String code, int label, int target, String op){
		Matcher matcher = p.matcher(code);
		assertTrue(matcher.find());
		assertEquals(matcher.group("op"), op );
		assertEquals(Integer.parseInt(matcher.group("target")), target);
		assertEquals(Integer.parseInt(matcher.group("label")), label);
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
{"	            1: ifle          5       ",   1 , 5  , "ifle"},
{"	            1: ifeq          5       ",   1 , 5  , "ifeq"},
{"	           21: ifne          29      ",  21 , 29 , "ifne"},
{"	            1: ifnonnull     5       ",   1 , 5  , "ifnonnull"},
{"	            5: goto          10      ",   5 , 10 , "goto"},
{"	            5: goto_w        10      ",   5 , 10 , "goto_w"},
{"	            3: iflt          7       ",   3 , 7  , "iflt"},
{"	           15: ifge          19      ",  15 , 19 , "ifge"},
{"	     	    2: if_acmpne     10      ",   2 , 10 , "if_acmpne"},
{"	            3: if_icmpne     11      ",   3 , 11 , "if_icmpne"},
{"	     	    1: ifnull        9       ",   1 , 9  , "ifnull"},
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
    
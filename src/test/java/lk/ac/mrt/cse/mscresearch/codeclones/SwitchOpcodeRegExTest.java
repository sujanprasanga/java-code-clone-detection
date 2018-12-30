package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class SwitchOpcodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForSwitch());
	
	@Test
	public void testinvoke() throws IOException{
		String s = Util.readFile("switchTestWithOtherInstructions.txt");
		Matcher matcher = p.matcher(s);
		int i =0;
		while(matcher.find()) {
			i++;
			System.out.println(s.substring(matcher.start(), matcher.end()));
		}
		assertEquals(i, 6);
		
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
	        	{"       8: tableswitch   { // 1 to 3\r\n" + 
	        			"\r\n" + 
	        			"                     1: 36\r\n" + 
	        			"\r\n" + 
	        			"                     2: 46\r\n" + 
	        			"\r\n" + 
	        			"                     3: 53\r\n" + 
	        			"               default: 60\r\n" + 
	        			"          }"}
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
    
package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class ConstructorDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForConstructorInvoke());
	
	@Test(dataProvider="invokeDataSet")
	public void testinvoke(String code, int label, String clazz, String constructorSignature){
		Matcher matcher = p.matcher(code);
		assertTrue(matcher.find());
		assertEquals(Integer.parseInt(matcher.group("label")), label);
		assertEquals(matcher.group("class"), clazz );
		assertEquals(matcher.group("constructorSignature"), constructorSignature );
	}
	
	@DataProvider(name = "invokeDataSet")
	  public static Object[][] methods() {
	        return new Object[][] { 
{"  20: invokespecial #29                 // Method \"<init>\":()V                                                           ",20 ,null,"()V"},
{"  28: invokespecial #8                  // Method java/lang/Object.\"<init>\":()V                                          ",28 ,"java/lang/Object","()V"},
{"  47: invokespecial #47                 // Method java/io/FileOutputStream.\"<init>\":(Ljava/io/File;)V			         ",47 ,"java/io/FileOutputStream","(Ljava/io/File;)V"},
{"  154: invokespecial #110                // Method lk/clones/Invoke$A.\"<init>\":(Llk/clones/Invoke;Llk/clones/Invoke$A;)V ",154,"lk/clones/Invoke$A","(Llk/clones/Invoke;Llk/clones/Invoke$A;)V"},
	        };
	  }
}

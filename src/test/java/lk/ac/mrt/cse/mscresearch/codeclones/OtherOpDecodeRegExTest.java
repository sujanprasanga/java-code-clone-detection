package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.InstructionSorter;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode.Category;
import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class OtherOpDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForOtherOperations());
	
	@Test(dataProvider="bytecode")
	public void testinvoke(String code, int label, String op){
		OpCode opCode = InstructionSorter.decode(code.trim());
		assertEquals(opCode.getLabel(), label);
		assertEquals(opCode.getCode(), op);
		assertEquals(opCode.getCategory(), Category.OTHER);
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
	  {"        17: dup", 17  ,  "dup"},
	  {"        17: dup_x1             ", 17  ,  "dup_x1"},
	  {"        17: dup_x2             ", 17  ,  "dup_x2"},
	  {"        17: dup2               ", 17  ,  "dup2"},
	  {"        17: dup2_x1            ", 17  ,  "dup2_x1"},
	  {"        17: dup2_x2            ", 17  ,  "dup2_x2"},
	  {"        20: monitorenter       ", 20  ,  "monitorenter"},
	  {"        29: iinc               ", 29  ,  "iinc"},
	  {"        39: arraylength        ", 39  ,  "arraylength"},
	  {"        44: monitorexit        ", 44  ,  "monitorexit"},
	  {"        51: athrow             ", 51  ,  "athrow"},
	  {"  		52: jsr                ", 52  ,  "jsr"},
	  {"  		52: jsr_w              ", 52  ,  "jsr_w"},
	  {"  		52: iinc               ", 52  ,  "iinc"},
	  {"  		52: ldc                ", 52  ,  "ldc"},
	  {"  		52: ldc_w              ", 52  ,  "ldc_w"},
	  {"  		52: ldc2_w             ", 52  ,  "ldc2_w"},
	  {"  		52: pop                ", 52  ,  "pop"},
	  {"  		52: pop2               ", 52  ,  "pop2"},
	  {"  		52: nop                ", 52  ,  "nop"},
	  {"  		52: swap               ", 52  ,  "swap"},
	  {"  		52: wide               ", 52  ,  "wide"},
	  {"  		52: bipush             ", 52  ,  "bipush"},
	  {"  		52: sipush             ", 52  ,  "sipush"},
	  {"       334: ret           7    ", 334 ,  "ret "},
	  {"     271: ldc           #13                 // String DIGEST-hashValue: Invalid block size for cipher", 271, "ldc"}
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
    
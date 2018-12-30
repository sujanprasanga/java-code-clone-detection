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

public class FieldOpDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForFieldOperations());
	
	@Test(dataProvider="fieldDataSet")
	public void testinvoke(String code, int label, String op, String fieldSignature){
		OpCode opCode = InstructionSorter.decode(code).build();
		assertEquals(opCode.getCode(), op);
		assertEquals(opCode.getTargetField(), fieldSignature );
		assertEquals(opCode.getLabel(), label);
	}
	
	@DataProvider(name = "fieldDataSet")
	  public static Object[][] fields() {
	        return new Object[][] { 
{"   7: putstatic     #19                 // Field f:Llk/clones/Field;      ",   7 , "put", "Llk/clones/Field;"},
{"  17: putstatic     #24                 // Field a:Llk/clones/Field$A2$;  ",  17 , "put", "Llk/clones/Field$A2$;"},
{"  12: putfield      #29                 // Field fi:Llk/clones/Field;     ",  12 , "put", "Llk/clones/Field;"},
{"  24: putfield      #36                 // Field a$:Llk/clones/Field$A$;  ",  24 , "put", "Llk/clones/Field$A$;"},
{"  36: putfield      #38                 // Field a$2:Llk/clones/Field$A$; ",  36 , "put", "Llk/clones/Field$A$;"},
{"   0: getstatic     #19                 // Field f:Llk/clones/Field;      ",   0 , "get", "Llk/clones/Field;"},
{"   7: getstatic     #24                 // Field a:Llk/clones/Field$A2$;  ",   7 , "get", "Llk/clones/Field$A2$;"},
{"  15: getfield      #29                 // Field fi:Llk/clones/Field;     ",  15 , "get", "Llk/clones/Field;"},
{"  23: getfield      #36                 // Field a$:Llk/clones/Field$A$;  ",  23 , "get", "Llk/clones/Field$A$;"},
{"  39: putfield      #38                 // Field a$2:Llk/clones/Field$A$; ",  39 , "put", "Llk/clones/Field$A$;"},
{"  44: putfield      #44                 // Field ar1:[Ljava/lang/Object;  ",  44 , "put", "[Ljava/lang/Object;"},
{"  52: putfield      #47                 // Field ar2:[[Ljava/lang/Object; ",  52 , "put", "[[Ljava/lang/Object;"},
	        };
	  }
	
	@Test(dataProvider="nonMatching")
	public void test_other_code_is_not_matched(String code) {
		Matcher matcher = p.matcher(code);
		assertFalse(matcher.find());
	}
	
	@DataProvider(name = "nonMatching")
	public Object[][] nonMatchingDataSet(){
		return new NonMatchingDataProvider(getClass()).getCode();
	}
}

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

public class NewArrayDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForNewArrayOp());
	
	@Test(dataProvider="bytecode")
	public void testinvoke(String code, int label, String arrayType){
		OpCode opCode = InstructionSorter.decode(code);
		assertEquals(opCode.getLabel(), label);
		assertEquals(opCode.getTargetClass(), arrayType);
		assertEquals(opCode.getCategory(), Category.NEW_ARRAY);
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
{"	   1: anewarray     #3                  // class java/lang/Object     "  ,  1, "java/lang/Object"   },
{"     6: anewarray     #15                 // class \"[Ljava/lang/Object;\"",  6, "[Ljava/lang/Object;"},
{"	   1: newarray       short                                            "  ,  1, "short"              },
{"     5: anewarray     #21                 // class \"[S\"                 ",  5, "[S"                 },
{"	   1: newarray       byte                                             "  ,  1, "byte"              },
{"     5: anewarray     #25                 // class \"[B\"                 ",  5, "[B"                 },
{"	   1: newarray       int                                              "  ,  1, "int"              },
{"     5: anewarray     #29                 // class \"[I\"                 ",  5, "[I"                 },
{"	   1: newarray       long                                             "  ,  1, "long"              },
{"     5: anewarray     #33                 // class \"[J\"                 ",  5, "[J"                 },
{"	   1: newarray       float                                            "  ,  1, "float"              },
{"     5: anewarray     #37                 // class \"[F\"                 ",  5, "[F"                 },
{"     1: newarray       char                                             "  ,  1, "char"              },
{"     5: anewarray     #41                 // class \"[C\"                 ",  5, "[C"                 },
{"     1: newarray       double                                           "  ,  1, "double"              },
{"     5: anewarray     #45                 // class \"[D\"                 ",  5, "[D"                 },
{"     1: newarray       boolean                                          "  ,  1, "boolean"              },
{"     5: anewarray     #49                 // class \"[Z\"                 ",  5, "[Z"                 },
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
    
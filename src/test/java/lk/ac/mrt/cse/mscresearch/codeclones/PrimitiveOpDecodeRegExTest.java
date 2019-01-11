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

public class PrimitiveOpDecodeRegExTest {

	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern p = Pattern.compile(propertyUtil.getRegExForPrimitiveOperations());
	
	@Test(dataProvider="bytecode")
	public void testinvoke(String code, int label, char type, boolean arrayOp, String op){
		OpCode opCode = InstructionSorter.decode(code);
		assertEquals(opCode.getLabel(), label);
		assertEquals(opCode.getCode(), op);
		assertEquals(opCode.getTargetClass(), String.valueOf(type));
		assertEquals(opCode.isArrayOp(), arrayOp);
		assertEquals(opCode.getCategory(), Category.PRIMITIVE_OP);
	}
	
	@DataProvider(name = "bytecode")
	  public static Object[][] fields() {
	        return new Object[][] { 
	        	{"   0: iconst_1                ",   0, 'i', false, "const"},
	            {"   1: istore_1                ",   1, 'i', false, "store"},
	            {"   4: iload_1                 ",   4, 'i', false, "load"},
	            {"   6: iadd                    ",   6, 'i', false, "add"},
	            {"   7: i2b                     ",   7, 'i', false, "2b"},
	            {"  11: imul                    ",  11, 'i', false, "mul"},
	            {"  16: idiv                    ",  16, 'i', false, "div"},
	            {"  21: isub                    ",  21, 'i', false, "sub"},
	            {"  26: iand                    ",  26, 'i', false, "and"},
	            {"  31: ior                     ",  31, 'i', false, "or"},
	            {"  36: ishr                    ",  36, 'i', false, "shr"},
	            {"  41: ishl                    ",  41, 'i', false, "shl"},
	            {"  45: istore        4         ",  45, 'i', false, "store"},
	            {"  55: i2s                     ",  55, 'i', false, "2s"},
	            {"  58: iload         4         ",  58, 'i', false, "load"},
	            {"   0: lconst_1                ",   0, 'l', false, "const"},
	            {"   1: lstore_1                ",   1, 'l', false, "store"},
	            {"   4: lload_1                 ",   4, 'l', false, "load"},
	            {"   6: ladd                    ",   6, 'l', false, "add"},
	            {"  11: lmul                    ",  11, 'l', false, "mul"},
	            {"  16: ldiv                    ",  16, 'l', false, "div"},
	            {"  21: lsub                    ",  21, 'l', false, "sub"},
	            {"  26: land                    ",  26, 'l', false, "and"},
	            {"  31: lor                     ",  31, 'l', false, "or"},
	            {"  36: l2i                     ",  36, 'l', false, "2i"},
	            {"  37: lshl                    ",  37, 'l', false, "shl"},
	            {"  43: lshr                    ",  43, 'l', false, "shr"},
	            {"   0: fconst_1                ",   0, 'f', false, "const"},
	            {"   1: fstore_1                ",   1, 'f', false, "store"},
	            {"   4: fload_1                 ",   4, 'f', false, "load"},
	            {"   6: fadd                    ",   6, 'f', false, "add"},
	            {"  10: fmul                    ",  10, 'f', false, "mul"},
	            {"  14: fdiv                    ",  14, 'f', false, "div"},
	            {"  18: fsub                    ",  18, 'f', false, "sub"},
	            {"   1: dstore_1                ",   1, 'd', false, "store"},
	            {"   2: dconst_1                ",   2, 'd', false, "const"},
	            {"   4: dload_1                 ",   4, 'd', false, "load"},
	            {"   6: dadd                    ",   6, 'd', false, "add"},
	            {"   7: dstore        5         ",   7, 'd', false, "store"},
	            {"  11: dmul                    ",  11, 'd', false, "mul"},
	            {"  16: ddiv                    ",  16, 'd', false, "div"},
	            {"  21: dsub                    ",  21, 'd', false, "sub"},
	            {"  27: dload         5         ",  27, 'd', false, "load"},
	            {"   7: i2c                     ",   7, 'i', false, "2c"},
	            {"  10: ineg                    ",  10, 'i', false, "neg" },
	    		{"  15: irem                    ",  15, 'i', false, "rem" },
	    		{"  10: lneg                    ",  10, 'l', false, "neg" },
	    		{"  15: lrem                    ",  15, 'l', false, "rem" },
	    		{"  13: frem                    ",  13, 'f', false, "rem" },
	    		{"   9: fneg                    ",   9, 'f', false, "neg" },
	    		{"  10: dneg                    ",  10, 'd', false, "neg" },
	    		{"  15: drem                    ",  15, 'd', false, "rem" },
	    	    {"    9: astore_2               ",   9, 'a', false, "store"},
	    	    {"   10: aload_2                ",  10, 'a', false, "load"},
	    	    {"  13: aastore                 ",  13, 'a', true, "store"},
	    	    {"  16: aaload                  ",  16, 'a', true, "load"},
	    	    {"  19: saload                  ",  19, 's', true, "load"},
	    	    {"  20: sastore                 ",  20, 's', true, "store"},
	    	    {"  19: baload                  ",  19, 'b', true, "load"},
	    	    {"  20: bastore                 ",  20, 'b', true, "store"},
	    	    {"  19: iaload                  ",  19, 'i', true, "load"},
	    	    {"  20: iastore                 ",  20, 'i', true, "store"},
	    	    {"  19: laload                  ",  19, 'l', true, "load"},
	    	    {"  20: lastore                 ",  20, 'l', true, "store"},
	    	    {"  19: faload                  ",  19, 'f', true, "load"},
	    	    {"  20: fastore                 ",  20, 'f', true, "store"},
	    	    {"  19: caload                  ",  19, 'c', true, "load"},
	    	    {"  20: castore                 ",  20, 'c', true, "store"},
	    	    {"  19: daload                  ",  19, 'd', true, "load"},
	    	    {"  20: dastore                 ",  20, 'd', true, "store"},
	    	    {"  19: baload                  ",  19, 'b', true, "load"},
	    	    {"  20: bastore                 ",  20, 'b', true, "store"},
	    	    {"  27: lcmp                    ",  27, 'l', false,"cmp"},
	  	        {"  38: lcmp                    ",  38, 'l', false,"cmp"},
	  	        {"  27: fcmpl                   ",  27, 'f', false,"cmpl"},
	  	        {"  38: fcmpg                   ",  38, 'f', false,"cmpg"},
	  	        {"  27: dcmpl                   ",  27, 'd', false,"cmpl"},
	  	        {"  38: dcmpg                   ",  38, 'd', false,"cmpg"},
	  		    {"	52: aconst_null             ",  52, 'a', false,"const_null"},
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
    
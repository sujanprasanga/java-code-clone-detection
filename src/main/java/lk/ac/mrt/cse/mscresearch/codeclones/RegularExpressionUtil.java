package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class RegularExpressionUtil {

	private static final Logger log = Logger.getLogger(RegularExpressionUtil.class);
	
	public static final String RETURN = "return";
	public static final String STORE = "store";
	public static final String ARRAY_STORE = "arraystore";
	public static final String LOAD = "load";
	public static final String ARRAY_LOAD = "arrayload";
	public static final String IF = "if";
	public static final String PUT = "put";
	public static final String GET = "get";
	public static final String CHECKCAST = "checkcast";
	public static final String THROW = "throw";
	public static final String GOTO = "goto";
	public static final String INVOKE = "invoke";
	public static final String BRANCH_DEST = "branchDest";
	public static final String POP = "pop";
	public static final String NEW = "new";
	public static final String DUP = "dup";
	public static final String CONST = "const";
	public static final String PUSH_NULL = "aconst_null";
	public static final String NEW_ARRAY = "anewarray";
	public static final String ARRAY_LENGTH = "arraylength";
	public static final String PRIMITIVE_CONVERSION = "primetiveConversion";
	public static final String ADD = "add";
	public static final String PRIMITIVE_COMPARISON = "cmp";
	public static final String DIV = "div";
	public static final String MUL = "mul";
	public static final String NEG = "neg";
	public static final String REM = "rem";
	public static final String SUB = "sub";
	public static final String INC = "inc";
	public static final String TYPE_CHECK = "instanceOf";
	public static final String LOGIC_OP = "logicOperation";
	public static final String JSR = "jumpSubroutine";
	public static final String LDC = "ldc";
	public static final String MONITOR_ACCESS = "monitorAccess";
	public static final String NOP = "nop";
	public static final String SWAP = "swap";
	public static final String WIDE = "wide";
	
	public static final String ICLASS_CG_NAME = "InvokedClass";
	public static final String ICLASS_CG = "(?<" + ICLASS_CG_NAME	+ ">([\\w$]+[/\\w]*))";
	public static final String METHOD_CG_NAME = "InvokedMethod";
	public static final String METHOD_CG = "(?<" + METHOD_CG_NAME + ">((\\\"<)?[\\w$]+(>\\\")?))";
	public static final String IRETURN_CG_NAME = "methodReturnType";
	public static final String IRETURN_CG = "(?<" + IRETURN_CG_NAME + ">([\\w$]+[/\\w]*))";
	public static final String GFCLASS_CG_NAME = "getFieldClass";
	public static final String GFCLASS_CG = "(?<" + GFCLASS_CG_NAME	+ ">([\\w$]+[/\\w]*))";
	public static final String GFIELD_CG_NAME = "gfield";
	public static final String GFIELD_CG = "(?<" + GFIELD_CG_NAME + ">((\\\"<)?[\\w$]+(>\\\")?))";
	public static final String PFCLASS_CG_NAME = "putFieldClass";
	public static final String PFCLASS_CG = "(?<" + PFCLASS_CG_NAME	+ ">([\\w$]+[/\\w]*))";
	public static final String PFIELD_CG_NAME = "pfield";
	public static final String PFIELD_CG = "(?<" + PFIELD_CG_NAME + ">((\\\"<)?[\\w$]+(>\\\")?))";
	public static final String BRANCH_DESTINATION = "[ ]+(?<"+ BRANCH_DEST +">[\\d]*[0123456789]+)";
	public static final String STORE_CG_NAME = "astoreIndex";
	public static final String STORE_CG = "(?<" + STORE_CG_NAME	+ ">[\\d]*[0123456789]+)";
	public static final String LOAD_CG_NAME = "aloadIndex";
	public static final String LOAD_CG = "(?<" + LOAD_CG_NAME	+ ">[\\d]*[0123456789]+)";
	public static final String NEW_CLASS_CG_NAME = "createdClass";
	public static final String NEW_CLASS_CG = "(?<" + NEW_CLASS_CG_NAME	+ ">([\\w$]+[/\\w]*))";
	public static final String DUP_CG_NAME = "dupIndex";
	public static final String DUP_CG = "(?<" + DUP_CG_NAME	+ ">([\\d]*))";
	
	public static final String INSTRUCTION_LABEL_CG_NAME = "label";
	public static final String PARAMETER_CG_NAME = "params";
	public final static String METHOD_DEFINITION_REG_EX = "public .+ .+\\((?<" + PARAMETER_CG_NAME + ">.*)\\) ?(throws .+)?;";
	private final static Pattern METHOD_DEFINITION_REG_EX_PATTERN = Pattern.compile(METHOD_DEFINITION_REG_EX);
	private final static String INSTRUCTION_START_REG_X = "[ ]*(?<" + INSTRUCTION_LABEL_CG_NAME + ">([\\d]*[0123456789]+)): ";
	private final static String PRIMITIVE_REG_X = "[dfil]";
	private final static String MATCH_TILL_NEW_LINE_REG_EX = ".*";
	private final static String NEW_LINE = "\\r?\\n";
	public static final String LINE_NUMBER_TABLE_CG_NAME = "lineNumberTable";
	public static final String LINE_NUMBER_CG_NAME = "line";
	public static final String LABEL_NUMBER_CG_NAME = "lable";
//	private final static String LINE_NUMBER_TABLE_ENTRY_REG_EX = " +(line +(?<" + LINE_NUMBER_CG_NAME + ">(\\d*[0123456789]+): +(?<" + LABEL_NUMBER_CG_NAME + ">(\\d*[0123456789]+)";
	private final static String LINE_NUMBER_TABLE_ENTRY_REG_EX = "line +(?<" + LINE_NUMBER_CG_NAME + ">(\\d+)): +(?<" + LABEL_NUMBER_CG_NAME + ">(\\d+))";
	public final static Pattern LINE_NUMBER_TABLE_ENTRY = Pattern.compile(LINE_NUMBER_TABLE_ENTRY_REG_EX);
	private final static String LINE_NUMBER_TABLE_REG_EX = "( +LineNumberTable:\\r?\\n(?<" + LINE_NUMBER_TABLE_CG_NAME +">( +line.+\\r?\\n)+))?";
//	private final static String LINE_NUMBER_TABLE_REG_EX = "[ ]*LineNumberTable:.*(" + LINE_NUMBER_TABLE_ENTRY + ")+";
	private final static String PUBLIC_METHOD_BODY_REG_EX_STRING = "(?<" + METHOD_CG_NAME + ">(" + METHOD_DEFINITION_REG_EX + NEW_LINE + ".*" + NEW_LINE + "("+ INSTRUCTION_START_REG_X +"(.+)|(lookupswitch  \\{((.+)?\\r?\\n)+.+\\})" + NEW_LINE + ")+))" + LINE_NUMBER_TABLE_REG_EX;
	public final static Pattern PUBLIC_METHOD_BODY_REG_EX = Pattern.compile(PUBLIC_METHOD_BODY_REG_EX_STRING);
	private final static Map<String, String> INSTRUCTION_REG_X = createInstructionSet();
	private final static Pattern FILTER_REG_EX = createFinalRegEx();
	private static final Map<String, Pattern> INSTRUCTION_PATTERNS = createInstructionPatterns();

	private static Map<String, String> createInstructionSet() {
		
//		Pattern.compile(LINE_NUMBER_TABLE_ENTRY);
//		Pattern.compile(LINE_NUMBER_TABLE_REG_EX);
		Map<String, String> instructions = new HashMap<>();
		
//		instructions.put(INVOKE, "invoke((special)|(dynamic)|(interface)|(static)|(virtual))[ ]+#\\d+[ ]+// Method (" + ICLASS_CG + "\\.)?" + METHOD_CG	+ ":\\(.*\\)" + IRETURN_CG);
//		instructions.put(GOTO, "goto(_w)?");
//		instructions.put(JSR, "jsr(_w)?");
//		instructions.put(THROW, "athrow");
//		instructions.put(CHECKCAST, CHECKCAST);
//		instructions.put(ADD, PRIMITIVE_REG_X + "add");
//		instructions.put(DIV, PRIMITIVE_REG_X + "div");
//		instructions.put(MUL, PRIMITIVE_REG_X + "mul");
//		instructions.put(NEG, PRIMITIVE_REG_X + "neg");
//		instructions.put(REM, PRIMITIVE_REG_X + "rem");
//		instructions.put(SUB, PRIMITIVE_REG_X + "sub");
//		instructions.put(GET, "get((field)|(static))[ ]+#\\d+[ ]+// Field (" + GFCLASS_CG + "\\.)?" + GFIELD_CG	+ ":");
//		instructions.put(PUT, "put((field)|(static))[ ]+#\\d+[ ]+// Field (" + PFCLASS_CG + "\\.)?" + PFIELD_CG	+ ":");
//		instructions.put(IF, "if((_[ai]cmp((eq)|(gt)|(lt)|(ne)|(ge)|(le)))|((eq)|(ge)|(gt)|(le)|(lt)|(ne)|(nonnull)|(null)))");
//		instructions.put(LOAD, ".load");
//		instructions.put(STORE, ".store[ _]+" + STORE_CG);
//		instructions.put(ARRAY_STORE, "a[abcdfils]store");
//		instructions.put(LOAD, "[abcdfils]load[ _]+" + LOAD_CG);
//		instructions.put(ARRAY_LOAD, "a[abcdfils]load");
//		instructions.put(MONITOR_ACCESS, "monitor((enter)|(exit))");
//		instructions.put(RETURN, ".return");
//		instructions.put(CONST, ".const");
//		instructions.put(INC, "iinc");
//		instructions.put(LDC, "ldc2?(_w)?");
//		instructions.put(PUSH_NULL, "aconst_null");
//		instructions.put(NEW_ARRAY, "(multi)?a?newarray");
//		instructions.put(PRIMITIVE_COMPARISON, PRIMITIVE_REG_X + "cmp[gl]");
//		instructions.put(PRIMITIVE_CONVERSION, PRIMITIVE_REG_X + "2[fildbcs]");
//		instructions.put(POP, "pop2?");
//		instructions.put(NOP, "nop");
//		instructions.put(SWAP, "swap");
//		instructions.put(WIDE, "wide");
//		instructions.put(ARRAY_LENGTH, "arraylength");
//		instructions.put(TYPE_CHECK, "instanceof");
//		instructions.put(LOGIC_OP, "[il]((x?or)|(u?sh[lr])|(and))");
//		instructions.put(NEW, "new [ ]+#\\d+[ ]+// class " + NEW_CLASS_CG);
//		instructions.put(DUP, "dup[_2]?_?x?" + DUP_CG);
//		instructions.put("push", "[bs][i]push");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html
//icmp
//ldc
//swap
//aconst_null
//type conversions i2b i2c **
//
//switches
//tableswitch 
//lookupswitch

		log.debug(instructions);
		return instructions;
	}
	
	private static Map<String, Pattern> createInstructionPatterns() {
		Map<String, Pattern> patterns = new HashMap<>();
		for(Entry<String, String> ins : INSTRUCTION_REG_X.entrySet()){
			String key = ins.getKey();
			if(isBranchOrLoop(key)){
				patterns.put(key, Pattern.compile(INSTRUCTION_START_REG_X + ins.getValue() + BRANCH_DESTINATION));
			} 
			else {
				patterns.put(key, Pattern.compile(INSTRUCTION_START_REG_X + ins.getValue()));
			}
		}
		return patterns;
	}

	public static boolean isBranchOrLoop(String key) {
		return IF.equals(key) || GOTO.equals(key);
	}

	private static Pattern createFinalRegEx() {
		StringBuilder sb = new StringBuilder();
		sb.append('(').append(METHOD_DEFINITION_REG_EX).append(')').append('|').append('(');
		sb.append(INSTRUCTION_START_REG_X).append('(');
		for(Entry<String, String> regEx : INSTRUCTION_REG_X.entrySet())
		{
		  sb.append('(').append(regEx.getValue()).append(')').append('|');
		}
		sb.setLength(sb.length() - 1);
		sb.append("))").append(MATCH_TILL_NEW_LINE_REG_EX);
		log.debug(sb.toString());
		return Pattern.compile(sb.toString());
	}

	public static String  filterNonEssential(String classByteCode)
	{
		return removeNonMatching(classByteCode, FILTER_REG_EX, getMatchingIndexes(classByteCode, PUBLIC_METHOD_BODY_REG_EX));
	}
	
	public static Set<int[]> getMatchingIndexes(String in, Pattern p)
	{
		Matcher m = p.matcher(in);
		Set<int[]>  set = new HashSet<>();
		while(m.find()){
		  set.add(new int[]{m.start(), m.end()});
		}
		return set;
	}
	
	private static String removeNonMatching(String in, Pattern p, Set<int[]> allowedIndexRanges){
		Matcher m = p.matcher(in);
		StringBuilder sb = new StringBuilder();
		while(m.find()){
			if(isAllowedRange(m.start(), m.end(), allowedIndexRanges)){
				String substring = in.substring(m.start(), m.end());
				sb.append(substring).append('\n');
			}
		}
		return sb.toString();
	}

	private static boolean isAllowedRange(int start, int end, Set<int[]> allowedIndexRanges) {
		for(int[] range : allowedIndexRanges){
			if(start >= range[0] && end <= range[1]){
				return true;
			}
		}
		return false;
	}
	
	public static Matcher getMethodMatcher(String in){
		return PUBLIC_METHOD_BODY_REG_EX.matcher(in);
	}
	
	public static Matcher getMethodsignatureMatcher(String in){
		return METHOD_DEFINITION_REG_EX_PATTERN.matcher(in);
	}
	
	public static Map<String, Pattern> getInstructionPatterns(){
		return INSTRUCTION_PATTERNS;
	}
	
}

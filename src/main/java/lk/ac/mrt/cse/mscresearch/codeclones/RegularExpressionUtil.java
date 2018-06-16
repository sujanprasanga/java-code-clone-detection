package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtil {

	public static final String RETURN = "return";
	public static final String STORE = "store";
	public static final String LOAD = "load";
	public static final String IF = "if";
	public static final String PUT = "put";
	public static final String GET = "get";
	public static final String CHECKCAST = "checkcast";
	public static final String THROW = "throw";
	public static final String GOTO = "goto";
	public static final String INVOKE = "invoke";
	public static final String BRANCH_DEST = "branchDest";
	public static final String POP = "pop";
	
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
	
	public static final String INSTRUCTION_LABEL_CG_NAME = "label";
	public static final String PARAMETER_CG_NAME = "params";
	private final static String METHOD_DEFINITION_REG_EX = "public .+ .+\\((?<" + PARAMETER_CG_NAME + ">.*)\\) ?(throws .+)?;";
	private final static Pattern METHOD_DEFINITION_REG_EX_PATTERN = Pattern.compile(METHOD_DEFINITION_REG_EX);
	private final static String INSTRUCTION_START_REG_X = "[ ]*(?<" + INSTRUCTION_LABEL_CG_NAME + ">([\\d]*[0123456789]+)): ";
//	private final static String PRIMITIVE_REG_X = "[dfil]";
	private final static String MATCH_TILL_NEW_LINE_REG_EX = ".*";
	private final static String NEW_LINE = "\\r?\\n";
	private final static String PUBLIC_METHOD_BODY_REG_EX_STRING = METHOD_DEFINITION_REG_EX + NEW_LINE + ".*" + NEW_LINE + "("+ INSTRUCTION_START_REG_X +".+" + NEW_LINE + ")+";
	private final static Pattern PUBLIC_METHOD_BODY_REG_EX = Pattern.compile(PUBLIC_METHOD_BODY_REG_EX_STRING);
	private final static Map<String, String> INSTRUCTION_REG_X = createInstructionSet();
	private final static Pattern FILTER_REG_EX = createFinalRegEx();
	private static final Map<String, Pattern> INSTRUCTION_PATTERNS = createInstructionPatterns();
	
	private static Map<String, String> createInstructionSet() {
		
		Map<String, String> instructions = new HashMap<>();
		
		instructions.put(INVOKE, "invoke((special)|(dynamic)|(interface)|(static)|(virtual)) #\\d+[ ]+// Method " + ICLASS_CG + "." + METHOD_CG	+ ":(.*)" + IRETURN_CG);
		instructions.put(GOTO, "goto(_w)?");
		instructions.put(THROW, "athrow");
		instructions.put(CHECKCAST, CHECKCAST);
//		instructions.put("add", PRIMITIVE_REG_X+"add");
		instructions.put(GET, "get((field)|(static))[ ]+#\\d+[ ]+// Field " + GFCLASS_CG + "." + GFIELD_CG	+ ":");
		instructions.put(PUT, "put((field)|(static))[ ]+#\\d+[ ]+// Field " + PFCLASS_CG + "." + PFIELD_CG	+ ":");
		instructions.put(IF, "if((_[ai]cmp((eq)|(gt)|(lt)|(ne)|(ge)|(le)))|((eq)|(ge)|(gt)|(le)|(lt)|(ne)|(nonnull)|(null)))");
		instructions.put(LOAD, ".load");
		instructions.put(STORE, ".store[ _]+" + STORE_CG);
		instructions.put(LOAD, ".load[ _]+" + LOAD_CG);
		instructions.put(RETURN, ".return");
		instructions.put("const", ".const");
		instructions.put(POP, "pop");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");

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
		System.out.println(sb.toString());
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

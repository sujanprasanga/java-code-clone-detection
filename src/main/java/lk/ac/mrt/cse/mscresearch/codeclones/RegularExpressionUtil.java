package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtil {

	private final static String METHOD_DEFINITION_REG_EX = "public .+ .+\\(.*\\);";
	private final static String INSTRUCTION_START_REG_X = "[ ]*[\\d]*[0123456789]+: ";
	private final static String PRIMITIVE_REG_X = "[dfil]";
	private final static String MATCH_TILL_NEW_LINE_REG_EX = ".*";
	private final static String NEW_LINE = "\\r?\\n";
	private final static String PUBLIC_METHOD_BODY_REG_EX_STRING = METHOD_DEFINITION_REG_EX + NEW_LINE + ".*" + NEW_LINE + "("+ INSTRUCTION_START_REG_X +".+" + NEW_LINE + ")+";
	private final static Pattern PUBLIC_METHOD_BODY_REG_EX = Pattern.compile(PUBLIC_METHOD_BODY_REG_EX_STRING);
	private final static Map<String, String> INSTRUCTION_REG_X = createInstructionSet();
	private final static Pattern FILTER_REG_EX = createFinalRegEx();
	
	private static Map<String, String> createInstructionSet() {
		
		Map<String, String> instructions = new HashMap<String, String>();
		
		instructions.put("invoke", "invoke((special)|(dynamic)|(interface)|(static)|(virtual))");
		instructions.put("goto", "goto(_w)?");
		instructions.put("throw", "athrow");
		instructions.put("checkcast", "checkcast");
		instructions.put("add", PRIMITIVE_REG_X+"add");
		instructions.put("get", "get((field)|(static))");
		instructions.put("put", "put((field)|(static))");
		instructions.put("if", "if((_[ai]cmp((eq)|(gt)|(lt)|(ne)|(ge)|(le)))|((eq)|(ge)|(gt)|(le)|(lt)|(ne)|(nonnull)|(null)))");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");
//		instructions.put("", "");

		return instructions;
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
		return Pattern.compile(sb.toString());
//		return Pattern.compile("public");
	}

	public static String removeNonPublicMethodCode(String classByteCode)
	{
		return removeNonMatching(classByteCode, PUBLIC_METHOD_BODY_REG_EX.matcher(classByteCode));
	}
	
	public static String  filterNonEssential(String classByteCode)
	{
		return removeNonMatching(classByteCode, FILTER_REG_EX.matcher(classByteCode));
	}
	
	private static String removeNonMatching(String in, Matcher m){
		StringBuilder sb = new StringBuilder();
		while(m.find())
		{
			String substring = in.substring(m.start(), m.end());
			sb.append(substring).append('\n');
		}
		return sb.toString();
	}
}

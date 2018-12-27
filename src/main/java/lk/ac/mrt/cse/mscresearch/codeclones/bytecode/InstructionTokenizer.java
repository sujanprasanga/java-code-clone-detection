package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InstructionTokenizer {

	private static final Pattern INSTRUCTION_SPLIT;
	static {
		PropertyUtil propertyUtil = new PropertyUtil();
		INSTRUCTION_SPLIT = Pattern.compile(propertyUtil.getRegExForInstructionSplitter());
	}
	
	private final Matcher matcher;
	private final String source;
	
	private InstructionTokenizer(String source, Pattern pattern) {
		this.source = source;
		this.matcher = pattern.matcher(source);
	}
	
	public boolean hasNext() {
		return matcher.find();
	}
	
	public String getNext() {
		return source.substring(matcher.start(), matcher.end());
	}
	
    public static InstructionTokenizer getInstructionTokenizer(String source) {
    	return new InstructionTokenizer(source, INSTRUCTION_SPLIT);
	}
}

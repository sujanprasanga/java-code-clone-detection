package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class InstructionTokenizer {

	private static final Pattern INSTRUCTION_SPLIT;
	private static final Pattern ACCESSIBLE;
	private static final Pattern ABSTRACTM;
	private static final Pattern NATIVEM;
	private static final InstructionTokenizer NULL_TOKENIZER;
	static {
		PropertyUtil propertyUtil = new PropertyUtil();
		INSTRUCTION_SPLIT = Pattern.compile(propertyUtil.getRegExForInstructionSplitter());
		ACCESSIBLE = Pattern.compile(propertyUtil.getAccessibleMethodRegEx());
		ABSTRACTM = Pattern.compile(propertyUtil.getAbstractMethodRegEx());
		NATIVEM=Pattern.compile(propertyUtil.getNativeMethodRegEx());
		NULL_TOKENIZER = new InstructionTokenizer() {public boolean hasNext() {return false;}};
	}
	
	private final Matcher matcher;
	private final String source;
	
	private InstructionTokenizer(String source) {
		this.source = source;
		this.matcher = INSTRUCTION_SPLIT.matcher(source);
	}
	
	private InstructionTokenizer() {
		this.source = null;
		this.matcher = null;
	}
	
	public boolean hasNext() {
		return matcher.find();
	}
	
	public String getNext() {
		try{
			return source.substring(matcher.start(), matcher.end());
		}catch(Exception e) {
			throw new RuntimeException(source, e);
		}
	}
	
    public static InstructionTokenizer getInstructionTokenizer(String source) {
    	if(isAccessible(source))
    	{
    		return new InstructionTokenizer(source);
    	}else {
    		return NULL_TOKENIZER;
    	}
	}

	private static boolean isAccessible(String source) {
		 Matcher a = ACCESSIBLE.matcher(source);
		 Matcher abs = ABSTRACTM.matcher(source);
		 Matcher n = NATIVEM.matcher(source);
		 boolean isAccessible = a.find();
		 boolean isAbstract = abs.find();
		 boolean isNative = n.find();
		 
		return isAccessible&& !isAbstract&& !isNative;
	}
}

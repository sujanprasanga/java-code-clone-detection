package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import static lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil.BRANCH_DEST;
import static lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil.IF;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil;

public class InstructionParser {

	private static final Logger log = Logger.getLogger(InstructionParser.class);
	
	protected final static Map<String, Pattern>  instructionPatterns = RegularExpressionUtil.getInstructionPatterns();
	protected final String[] body;
	protected final int startIndex;
	
	private final InstructionParserEventHandler handler;
	
	public InstructionParser(String[] body, String[] params, int startIndex) {
		this.body = body;
		this.startIndex = startIndex;
		handler = new InstructionParserEventHandlerImpl(params);
	}
	
	public void parse() {
			doParse(startIndex, body.length - 1);
			handler.notifyEnd();
			log.debug("Dump start");
			log.debug(handler.get());
	}
	
	protected void doParse(int start, int end) {
		for(int j=start; j<=end;)
		{
			String i = body[j];
			boolean matchFound = false;
			int currentLabel = getLabel(i);
			for(Entry<String, Pattern> entry : instructionPatterns.entrySet()){
				Matcher matcher = entry.getValue().matcher(i);
				if(matcher.find()){
					log.debug(i.trim());
					j = handleMatch(j, entry.getKey(), matcher, currentLabel);
					matchFound = true;
				}
			}
			if(!matchFound){
				j++;
			}
		}
	}

	private int getLabel(String i) {
		int label = Integer.parseInt(i.substring(0, i.indexOf(':')).trim());
		return label;
	}

	private int handleMatch(int index, String key, Matcher matcher, int currentLabel) {
		if(IF.equals(key)){
			int dest = Integer.parseInt(matcher.group(BRANCH_DEST));
			if(dest > currentLabel)
			{
				notifyMatch(key, matcher);
				int branchEndIndex = findLabel(index, dest+ ":");
				branch(index + 1, branchEndIndex);
				return branchEndIndex;
			}
			else
			{
				createLoop(dest, currentLabel);
				return index + 1;
			}
		}
		else{
			notifyMatch(key, matcher);
		}
		return index+1;
		
	}

	private void createLoop(int loopStartLabel, int loopEndLabel) {
		notifyLoop(loopStartLabel, loopEndLabel);
	}

	private void notifyLoop(int loopStartLabel, int loopEndLabel) {
		handler.notifyLoop(loopStartLabel, loopEndLabel);
	}

	private void branch(int start, int end) {
		String u = UUID.randomUUID().toString();
		log.debug("branch start " + u);
		notifyBranchStart();
		doParse(start, end);
		notifyBranchEnd();
		log.debug("branch end " + u);
	}

	private void notifyBranchEnd() {
		handler.notifyBranchEnd();
	}

	private void notifyBranchStart() {
		handler.notifyBranchStart();
	}

	private int findLabel(int index, String group){
		for(int i = index+1; i<body.length; i++){
			if(body[i].trim().startsWith(group)){
				return i;
			}
		}
		throw new RuntimeException("label not found :" + group);//TODO proper exception
	}
	
	private void notifyMatch(String key, Matcher matcher) {
		handler.notifyMatch(key, matcher);
	}
}

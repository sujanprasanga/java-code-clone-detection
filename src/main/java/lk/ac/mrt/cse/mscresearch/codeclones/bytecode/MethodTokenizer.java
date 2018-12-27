package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class MethodTokenizer {

	private static final Pattern METHOD_SPLIT;
	static {
		PropertyUtil propertyUtil = new PropertyUtil();
		METHOD_SPLIT = Pattern.compile(propertyUtil.getMethodSignatureRegEx());
	}
	
	private final Queue<Integer> que;
	private final String source;
	
	private MethodTokenizer(String source, Queue<Integer> que) {
		this.source = source;
		this.que = que;
	}
	
	public boolean hasNext() {
		return que.size() > 1;
	}
	
	public String getNext() {
		return source.substring(que.poll(), que.peek()).trim();
	}
	
	public static MethodTokenizer getMethodTokenizer(String source) {
		 Matcher m = METHOD_SPLIT.matcher(source);
		 Queue<Integer> que = new LinkedList<>();
		 while(m.find()){
			 que.add(m.start());
		 }
		 que.add(source.length()-2);
		return new MethodTokenizer(source, que);
	}
}

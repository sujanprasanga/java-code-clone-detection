package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.AccessibleMethodIdentifier;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.MethodTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCodeTransformer;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class LocalClassParser {

	private static final Logger log = Logger.getLogger(LocalClassParser.class);
	
	private static final Pattern LINE_NUMBER_TABLE_START;
	private static final Pattern LINE_NUMBER_TABLE_ENTRY;
	
	static {
		PropertyUtil p = new PropertyUtil();
		LINE_NUMBER_TABLE_START = Pattern.compile(p.getLineNumberTableStartRegEx());
		LINE_NUMBER_TABLE_ENTRY = Pattern.compile(p.getLineNumberTableEntryRegEx());
	}
	
	private MethodSplitter splitter = new MethodSplitter();
	private final AccessibleMethodIdentifier accessibleMethodIdentifier = new AccessibleMethodIdentifier();
	private final OpCodeTransformer opCodeTransformer = new OpCodeTransformer();
	private final MethodPartitioner methodPartitioner = new MethodPartitioner();
	
	public Set<MethodDTO> extractMethods(String target, String className) {
		final Set<MethodDTO> methods = new HashSet<>();
		MethodTokenizer methodTokenizer = MethodTokenizer.getMethodTokenizer(target);
		while(methodTokenizer.hasNext()){
			String method = methodTokenizer.getNext(); 
			Matcher ltm = LINE_NUMBER_TABLE_START.matcher(method);
			if(ltm.find()) {
				Map<Integer, Integer> lineNumberMapping = extractLineNumbers(method.substring(ltm.start()));
				methods.addAll(extractMethod(method.substring(0, ltm.start()), lineNumberMapping, className));
			}
			else {
				System.out.println("no line number tabel:" + method);
			}
		}
		return methods;
	}
	
	private static Map<Integer, Integer> extractLineNumbers(String method) {
		List<Integer> lines = new ArrayList<>();
		List<Integer> labels = new ArrayList<>();
			Matcher m = LINE_NUMBER_TABLE_ENTRY.matcher(method);
			while(m.find()) {
				lines.add(Integer.parseInt(m.group("line")));
				labels.add(Integer.parseInt(m.group("label")));
			}
		Map<Integer, Integer> labelToLineNumberMapping = new HashMap<>();
		int prev = labels.get(0);
		for(int i=0; i<lines.size(); i++) {
			int current = labels.get(i);
			for(int j=prev; j<current; j++) {
				labelToLineNumberMapping.put(j, lines.get(i-1));
			}
			labelToLineNumberMapping.put(current, lines.get(i));
			prev = current;
		}
		labelToLineNumberMapping.put(Integer.MAX_VALUE, lines.get(lines.size()-1));
		return labelToLineNumberMapping;
	}
	
	private Set<MethodDTO> extractMethod(String method, Map<Integer, Integer> lineNumberMapping, String className) {
	    String methodName = accessibleMethodIdentifier.extractLocalWSMethodSignature(method);
	    if(methodName == null) {
	    	return Collections.emptySet();
	    }
	    int[] lineNumberRange = getLNRange(lineNumberMapping);
		List<OpCode> opcodes = splitter.extractMethod(method, lineNumberMapping, className);
		return toMethodDTO(methodName, opcodes, method.length(), lineNumberRange);
	}
	
	private int[] getLNRange(Map<Integer, Integer> lineNumberMapping) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i : lineNumberMapping.values())
		{
			min = i < min ? i : min ;
			max = i > max ? i : max;
		}
		return new int[] {min, max};
	}

	private Set<MethodDTO> toMethodDTO(String signature, List<OpCode> opcodes, int size, int[] lineNumberRange) {
		Set<MethodDTO> transformForLocal = new HashSet<>(opCodeTransformer.transformForLocalWithCompleteLineNumberRange(signature, opcodes, size, lineNumberRange));
//		transformForLocal.addAll(methodPartitioner.partition(signature, opcodes, size));
//		if(log.isDebugEnabled()) {
//			log.debug("transform for:" + signature);
//			transformForLocal.forEach(t->{
//				log.debug("plugin-id:" + t.getPluginid());
//				log.debug(t.getBody());
//			});
//		}
		return transformForLocal;
	}
}

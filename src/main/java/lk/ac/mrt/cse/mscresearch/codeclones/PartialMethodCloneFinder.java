package lk.ac.mrt.cse.mscresearch.codeclones;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone.CloneType;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.AccessibleMethodIdentifier;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.MethodTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCodeTransformer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers.MethodSplitter;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndex;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndexEntry;
import lk.ac.mrt.cse.mscresearch.remoting.CloneFinderAdaptor;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;
import lk.ac.mrt.cse.mscresearch.util.Hashing;
import lk.ac.mrt.cse.mscresearch.util.IOUtil;
import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class PartialMethodCloneFinder {

	private static final Logger log = Logger.getLogger(PartialMethodCloneFinder.class);
	
	private static final Pattern LINE_NUMBER_TABLE_START;
	private static final Pattern LINE_NUMBER_TABLE_ENTRY;
	
	static {
		PropertyUtil p = new PropertyUtil();
		LINE_NUMBER_TABLE_START = Pattern.compile(p.getLineNumberTableStartRegEx());
		LINE_NUMBER_TABLE_ENTRY = Pattern.compile(p.getLineNumberTableEntryRegEx());
	}
	
	private final AccessibleMethodIdentifier accessibleMethodIdentifier = new AccessibleMethodIdentifier();
	private final OpCodeTransformer opCodeTransformer = new OpCodeTransformer();
	private final MethodSplitter splitter = new MethodSplitter();
	private final IOUtil ioUtil = new IOUtil();
	private final String project;
	private final String bin;
	private File file;
	private final String clazz;
	private String clazzHash;
	private final int lineStart;
	private final int lineEnd;

	private List<Clone> collect;
	
	public PartialMethodCloneFinder(String project, String bin, String clazz, int lineStart, int lineEnd)
	{
		this.project = project;
		this.bin = bin;
		this.clazz = clazz;
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
	}
	
	
	
	public void find() {
		collect = new ArrayList<>();
		String disassembleClass = ioUtil.disassembleLocalClass(clazz, bin);
		file = ioUtil.getFile(bin, clazz);
		clazzHash = Hashing.hash(file);
		MethodTokenizer methodTokenizer = MethodTokenizer.getMethodTokenizer(disassembleClass);
		while(methodTokenizer.hasNext()){
			String method = methodTokenizer.getNext(); 
			Matcher ltm = LINE_NUMBER_TABLE_START.matcher(method);
			if(ltm.find()) {
				Map<Integer, Integer> lineNumberMapping = extractLineNumbers(method.substring(ltm.start()));
				if(isSelectedMethod(lineNumberMapping, lineStart, lineEnd)) {
					find(extractMethod(method.substring(0, ltm.start()), clazz, lineNumberMapping, lineStart, lineEnd));
				}
			}
		}
		CloneModel.getSegementCloneModel().clear();
		CloneModel.getSegementCloneModel().addAll(removeDuplicates(collect));
		EventManager.get().fireUpdateView();
	}
	
	private boolean isSelectedMethod(Map<Integer, Integer> lineNumberMapping, int lineStart, int lineEnd) {
		return lineNumberMapping.values().stream().filter(i-> i > lineStart && i < lineEnd).count() > 0;
	}

	private void find(Set<MethodDTO> methods) {
		
		methods.forEach(m->{
			log.debug("plugin: " + m.getPluginid() + " boady:\n" + m.getBody());
		});
		
		methods.forEach(m->{
			Set<String> hashes = methods.stream().map(MethodDTO::getBodyhash).collect(Collectors.toSet());
			List<Clone> clones = LocalIndex.getLocalIndexes().stream()
					                       .filter(e->hashes.contains(e.getMethodHash()))
					                       .map(e->toClone(m, e))
					                       .filter(PartialMethodCloneFinder::isValidClone)
					                       .collect(Collectors.toList());
			collect.addAll(clones);
		});
		
		List<Clone> libClones = findLibClones(methods).stream()
				                               .filter(this::isValidLibClone)
                                               .collect(Collectors.toList());
		collect.addAll(libClones);
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
	
	private Set<MethodDTO> extractMethod(String method, String className, Map<Integer, Integer> lineNumberMapping, int lineStart, int lineEnd) {
	    String methodName = accessibleMethodIdentifier.extractLocalWSMethodSignature(method);
	    if(methodName == null) {
	    	return Collections.emptySet();
	    }
		List<OpCode> opcodes = splitter.extractMethod(method, lineNumberMapping, className)
				                       .stream()
				                       .filter(e->selected(e, lineStart, lineEnd))
				                       .collect(Collectors.toList());
		return toMethodDTO(methodName, opcodes, method.length());
	}
	
	private boolean selected(OpCode e, int start, int end) {
		int label = e.getLineNumber();
		return label == start ||
			   label == end ||
			   (label > start && label < end);
	}

	private Set<MethodDTO> toMethodDTO(String signature, List<OpCode> opcodes, int i) {
		return opCodeTransformer.transformForLocal(signature, opcodes, i);
	}
	
	private Clone toClone(MethodDTO m, LocalIndexEntry e2) {
		Clone c = new Clone();
		c.setProject(project);
		c.setClazz(clazz);
		c.setMethod(m.getSignature());
		c.setTargetArchive(e2.getProject());
		c.setTargetClass(e2.getClazz());
		c.setTargetMethod(e2.getMethodSignature());
		c.setType(CloneType.LOCAL);
		c.setPluginCode(m.getPluginid());
		return c;
	}
	
	private static boolean isValidClone(Clone c) {
		return LocalIndex.isValidDependency(c.getProject(), c.getTargetArchive());
	}
	
	private List<Clone> findLibClones(Set<MethodDTO> methods) {
		CloneFinderAdaptor a = new CloneFinderAdaptor();
		List<CodeFragmentData> codeFragments = methods.stream().map(this::toCodeFragment).collect(Collectors.toList());
		return a.find(codeFragments, LocalIndex.getDependencyMapping());
	}
	
	private CodeFragmentData toCodeFragment(MethodDTO m) {
		CodeFragmentData c = new CodeFragmentData();
		c.setProject(project);
		c.setClazz(clazz);
		c.setMethodHash(m.getBodyhash());
		c.setMethodSignature(m.getSignature());
		c.setLineRange(new int[] {lineStart, lineEnd});
		c.setTransformerType(m.getPluginid());
		c.setSegment(true);
		return c;
	}
	
	private boolean isValidLibClone(Clone c) {
		return LocalIndex.isValidDependency(c.getProject(), c.getLibMapping().getArchiveHash());
	}
	
	private List<Clone> removeDuplicates(List<Clone> collect) {
		Map<Integer, List<Clone>> grouped = collect.stream().collect(Collectors.groupingBy(Clone::getCloneUniqueHash));
		return grouped.entrySet().stream().map(this::flatten).collect(Collectors.toList());
	}

	private Clone flatten(Entry<Integer, List<Clone>> entry) {
		return entry.getValue().stream().sorted((x,y) -> x.getPluginCode()-y.getPluginCode()).findFirst().get();
	}
}

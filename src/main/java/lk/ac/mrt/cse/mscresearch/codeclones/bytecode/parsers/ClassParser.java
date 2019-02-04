package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.parsers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.AccessibleMethodIdentifier;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.MethodTokenizer;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCode;
import lk.ac.mrt.cse.mscresearch.codeclones.bytecode.OpCodeTransformer;
import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class ClassParser {

	private MethodSplitter splitter = new MethodSplitter();
	private final AccessibleMethodIdentifier accessibleMethodIdentifier = new AccessibleMethodIdentifier();
	private final OpCodeTransformer opCodeTransformer = new OpCodeTransformer();

	public Set<MethodDTO> extractMethods(String target, String className) {
		final Set<MethodDTO> methods = new HashSet<>();
		MethodTokenizer methodTokenizer = MethodTokenizer.getMethodTokenizer(target);
		while(methodTokenizer.hasNext()){
			String method = methodTokenizer.getNext(); 
			methods.addAll(extractMethod(method, className));
		}
		return methods;
	}

	private Set<MethodDTO> extractMethod(String method, String className) {
	    String methodName = accessibleMethodIdentifier.extractMethodSignature(method);
	    if(methodName == null) {
	    	return Collections.emptySet();
	    }
		List<OpCode> opcodes = splitter.extractMethod(method, className);
		return toMethodDTO(methodName, opcodes, method.length());
	}
	
	private Set<MethodDTO> toMethodDTO(String signature, List<OpCode> opcodes, int size) {
		return opCodeTransformer.transform(signature, opcodes, size);
	}
}

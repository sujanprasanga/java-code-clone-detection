package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.Iterator;
import java.util.List;

public class MethodIterator implements Iterator<MethodIterator.Method> {

	private final boolean extractLineNumbers;
	private int currentMethod;
	
	private final List<DisassembledMethod> methods;

	public MethodIterator(String disassembledCode, boolean extractLineNumbers, String className)
	{
		this.extractLineNumbers = extractLineNumbers;
		methods = ClassSplitter.split(className, disassembledCode);
	}

	@Override
	public boolean hasNext() {
		return currentMethod < methods.size();
	}

	@Override
	public Method next() {
		final DisassembledMethod m = methods.get(currentMethod);
		currentMethod++;
		return new Method() {
			
			@Override
			public String getSignature() {
				return m.getMethodSignature();
			}
			
			@Override
			public String getName() {
				return m.getMethodName();
			}
			
			@Override
			public String[] getLineNumberTable() {
				return extractLineNumbers ? m.getLineNumberTableEntries().toArray(new String[0]) : null;
			}
			
			@Override
			public String[] getBody() {
				return m.getCode().toArray(new String[0]);
			}
		};
	}
	
	interface Method{
		String getName();
		String getSignature();
		String[] getBody();
		String[] getLineNumberTable();
	}
}

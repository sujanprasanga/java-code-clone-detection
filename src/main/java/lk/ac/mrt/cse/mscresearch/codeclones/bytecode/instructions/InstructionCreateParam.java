package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.regex.Matcher;

import lk.ac.mrt.cse.mscresearch.codeclones.ClassUnderTransform;

public class InstructionCreateParam {
	public ClassUnderTransform target;
	public String arg;
	public Matcher matcher;
	public String topOfStack;

	public InstructionCreateParam(ClassUnderTransform target, String arg, Matcher matcher, String topOfStack) {
		this.target = target;
		this.arg = arg;
		this.matcher = matcher;
		this.topOfStack = topOfStack;
	}
}
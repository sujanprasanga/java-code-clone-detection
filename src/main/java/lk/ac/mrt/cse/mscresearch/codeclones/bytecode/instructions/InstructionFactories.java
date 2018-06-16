package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class InstructionFactories {

	public static final String RETURN = "return";
	public static final String STORE = "store";
	public static final String LOAD = "load";
	public static final String IF = "if";
	public static final String PUT = "put";
	public static final String GET = "get";
	public static final String CHECKCAST = "checkcast";
	public static final String THROW = "throw";
	public static final String GOTO = "goto";
	public static final String INVOKE = "invoke";
	public static final String POP = "pop";
	
	private final static Map<String, InstructionFactory<? extends Instruction>> factories = build();
	private static InstructionFactory<? extends Instruction> defualtFactory = (String arg, Matcher matcher, String tos)->{ return UnsortedInstruction.forInstruction(arg, matcher);};

	public static InstructionFactory<? extends Instruction> forInstruction(String instruction) {
		if(factories.containsKey(instruction)){
			return factories.get(instruction);
		} else {
			return defualtFactory ;
		}
	}

	private static Map<String, InstructionFactory<? extends Instruction>> build() {
		Map<String, InstructionFactory<? extends Instruction>> factories = new HashMap<>();
		factories.put(INVOKE, (String arg, Matcher matcher, String tos)->{ return MethodCall.forMethod(arg, matcher);});
		factories.put(PUT, (String arg, Matcher matcher, String tos)->{ return SetField.forField(arg, matcher);});
		factories.put(GET, (String arg, Matcher matcher, String tos)->{ return GetField.forField(arg, matcher);});
		factories.put(STORE, (String arg, Matcher matcher, String tos)->{ return Store.from(arg, matcher);});
		factories.put(LOAD, (String arg, Matcher matcher, String tos)->{ return Load.from(arg, matcher);});
		factories.put(IF, (String arg, Matcher matcher, String tos)->{ return Branch.forIf(arg, matcher);});
		factories.put(THROW, (String arg, Matcher matcher, String tos)->{ return Throw.forException(tos);});
		factories.put(POP, (String arg, Matcher matcher, String tos)->{ return Pop.from(matcher);});
		return factories;
	}

}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import static lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class InstructionFactories {

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
		factories.put(ARRAY_STORE, (String arg, Matcher matcher, String tos)->{ return ArrayStore.from(arg, matcher);});
		factories.put(PUSH_NULL, (String arg, Matcher matcher, String tos)->{ return PushNull.from(arg, matcher);});
		factories.put(ARRAY_LENGTH, (String arg, Matcher matcher, String tos)->{ return ArrayLength.from(arg, matcher);});
		factories.put(CHECKCAST, (String arg, Matcher matcher, String tos)->{ return CheckCast.from(arg, matcher);});
		factories.put(PRIMITIVE_CONVERSION, (String arg, Matcher matcher, String tos)->{ return PrimitiveConversion.from(arg, matcher);});
		factories.put(ADD, (String arg, Matcher matcher, String tos)->{ return Add.from(arg, matcher);});
		factories.put(DIV, (String arg, Matcher matcher, String tos)->{ return Div.from(arg, matcher);});
		factories.put(MUL, (String arg, Matcher matcher, String tos)->{ return Mul.from(arg, matcher);});
		factories.put(NEG, (String arg, Matcher matcher, String tos)->{ return Neg.from(arg, matcher);});
		factories.put(REM, (String arg, Matcher matcher, String tos)->{ return Rem.from(arg, matcher);});
		factories.put(SUB, (String arg, Matcher matcher, String tos)->{ return Sub.from(arg, matcher);});
		factories.put(INC, (String arg, Matcher matcher, String tos)->{ return Increment.from(arg, matcher);});
		factories.put(LDC, (String arg, Matcher matcher, String tos)->{ return Ldc.from(arg, matcher);});
		factories.put(JSR, (String arg, Matcher matcher, String tos)->{ return Jsr.from(arg, matcher);});
		factories.put(NOP, (String arg, Matcher matcher, String tos)->{ return Nop.from(arg, matcher);});
		factories.put(SWAP, (String arg, Matcher matcher, String tos)->{ return Swap.from(arg, matcher);});
		factories.put(WIDE, (String arg, Matcher matcher, String tos)->{ return Wide.from(arg, matcher);});
		
		factories.put(MONITOR_ACCESS, (String arg, Matcher matcher, String tos)->{ return MonitorAccess.from(arg, matcher);});
		factories.put(TYPE_CHECK, (String arg, Matcher matcher, String tos)->{ return TypeCheck.from(arg, matcher);});
		factories.put(PRIMITIVE_COMPARISON, (String arg, Matcher matcher, String tos)->{ return PrimitiveComparison.from(arg, matcher);});
		factories.put(RETURN, (String arg, Matcher matcher, String tos)->{ return Return.from(arg, matcher);});
		factories.put(NEW_ARRAY, (String arg, Matcher matcher, String tos)->{ return NewArray.from(arg, matcher);});
		factories.put(LOAD, (String arg, Matcher matcher, String tos)->{ return Load.from(arg, matcher);});
		factories.put(ARRAY_LOAD, (String arg, Matcher matcher, String tos)->{ return ArrayLoad.from(arg, matcher);});
		factories.put(IF, (String arg, Matcher matcher, String tos)->{ return Branch.forIf(arg, matcher);});
		factories.put(THROW, (String arg, Matcher matcher, String tos)->{ return Throw.forException(matcher, tos);});
		factories.put(POP, (String arg, Matcher matcher, String tos)->{ return Pop.from(matcher);});
		factories.put(NEW, (String arg, Matcher matcher, String tos)->{ return New.forClazz(arg, matcher);});
		factories.put(DUP, (String arg, Matcher matcher, String tos)->{ return Dup.from(arg, matcher);});
		factories.put(CONST, (String arg, Matcher matcher, String tos)->{ return Const.from(arg, matcher);});
		return factories;
	}

}

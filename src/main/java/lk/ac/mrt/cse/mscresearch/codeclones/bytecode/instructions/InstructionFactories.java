package lk.ac.mrt.cse.mscresearch.codeclones.bytecode.instructions;

import static lk.ac.mrt.cse.mscresearch.codeclones.RegularExpressionUtil.*;

import java.util.HashMap;
import java.util.Map;

public class InstructionFactories {

	private final static Map<String, InstructionFactory<? extends Instruction>> factories = build();
	private static InstructionFactory<? extends Instruction> defualtFactory = UnsortedInstruction::forInstruction;

	public static InstructionFactory<? extends Instruction> forInstruction(String instruction) {
		if(factories.containsKey(instruction)){
			return factories.get(instruction);
		} else {
			return defualtFactory ;
		}
	}

	private static Map<String, InstructionFactory<? extends Instruction>> build() {
		Map<String, InstructionFactory<? extends Instruction>> factories = new HashMap<>();
		factories.put(INVOKE, MethodCall::from);
		factories.put(PUT, SetField::from);
		factories.put(GET, GetField::from);
		factories.put(STORE, Store::from);
		factories.put(ARRAY_STORE, ArrayStore::from);
		factories.put(PUSH_NULL, PushNull::from);
		factories.put(ARRAY_LENGTH, ArrayLength::from);
		factories.put(CHECKCAST, CheckCast::from);
		factories.put(PRIMITIVE_CONVERSION, PrimitiveConversion::from);
		factories.put(ADD, Add::from);
		factories.put(DIV, Div::from);
		factories.put(MUL, Mul::from);
		factories.put(NEG, Neg::from);
		factories.put(REM, Rem::from);
		factories.put(SUB, Sub::from);
		factories.put(INC, Increment::from);
		factories.put(LDC, Ldc::from);
		factories.put(JSR, Jsr::from);
		factories.put(NOP, Nop::from);
		factories.put(SWAP, Swap::from);
		factories.put(WIDE, Wide::from);
		
		factories.put(MONITOR_ACCESS, MonitorAccess::from);
		factories.put(TYPE_CHECK, TypeCheck::from);
		factories.put(PRIMITIVE_COMPARISON, PrimitiveComparison::from);
		factories.put(RETURN, Return::from);
		factories.put(NEW_ARRAY, NewArray::from);
		factories.put(LOAD, Load::from);
		factories.put(ARRAY_LOAD, ArrayLoad::from);
		factories.put(IF, Branch::from);
		factories.put(THROW, Throw::from);
		factories.put(POP, Pop::from);
		factories.put(NEW, New::from);
		factories.put(DUP, Dup::from);
		factories.put(CONST, Const::from);
		return factories;
	}

}

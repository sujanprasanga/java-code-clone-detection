package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

public class CodeLineEquivalentChecker {

	public static boolean isEqual(CodeLine codeLine1, CodeLine codeLine2){
		return codeLine1.getInstruction().equals(codeLine2.getInstruction())
				&& codeLine1.getOperands().equals(codeLine2.getOperands());
	}
}

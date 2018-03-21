package lk.ac.mrt.cse.mscresearch.indexer.formatters;

public class LDCNumberRemover implements InstructionFormatter {

	@Override
	public String format(String codeLine) {
		if(codeLine.startsWith("ldc")){
			return removeNumber(codeLine);
		}
		return codeLine;
	}

	private String removeNumber(String codeLine) {
		int i = codeLine.indexOf('/');
		return "ldc" + codeLine.substring(i);
	}

}

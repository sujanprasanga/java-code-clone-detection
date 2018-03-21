package lk.ac.mrt.cse.mscresearch.indexer.formatters;

public class LineLableRemover implements InstructionFormatter {

	@Override
	public String format(String codeLine) {
		return codeLine.split(":")[1].trim();
	}

}

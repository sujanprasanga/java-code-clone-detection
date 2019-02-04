package lk.ac.mrt.cse.mscresearch.localindex;

import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class LocalMethodDTO extends MethodDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1245102929663775866L;
	
	
	private int[] lineNumbers;

	public int[] getLineNumbers() {
		return lineNumbers;
	}

	public void setLineNumbers(int[] lineNumbers) {
		this.lineNumbers = lineNumbers;
	}
}

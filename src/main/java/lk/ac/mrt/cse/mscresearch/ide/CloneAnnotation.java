package lk.ac.mrt.cse.mscresearch.ide;

import java.util.List;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;

public class CloneAnnotation {

	private final String lable;
	private final String type;
	private final int offset;
	private final List<Clone> clones;
	
	public CloneAnnotation(String type, int offset, String lable, List<Clone> clones) {
		this.lable = lable;
		this.type = type;
		this.offset = offset;
		this.clones = clones;
	}
	
	public String getLable() {
		return lable;
	}
	
	public String getType() {
		return type;
	}

	public int getOffset() {
		return offset;
	}

	public List<Clone> getClones() {
		return clones;
	}
	
}

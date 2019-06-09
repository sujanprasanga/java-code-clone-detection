package lk.ac.mrt.cse.mscresearch.ide;

public class CloneAnnotation {

	private final String lable;
	private final String type;
	private final int offset;	
	
	public CloneAnnotation(String type, int offset, String lable) {
		this.lable = lable;
		this.type = type;
		this.offset = offset;
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
	
}

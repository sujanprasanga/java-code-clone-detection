package lk.ac.mrt.cse.mscresearch.localindex;

public class LocalIndexEntry {

	private final String project;
	private final String clazz;
	private final String methodSignature;
	private final String methodHash;
	private final int type;
	
	public LocalIndexEntry(String project, String clazz, String methodSignature, String methodHash, int type) {
		super();
		this.project = project;
		this.clazz = clazz;
		this.methodSignature = methodSignature;
		this.methodHash = methodHash;
		this.type = type;
	}

	public String getProject() {
		return project;
	}

	public String getClazz() {
		return clazz;
	}

	public String getMethodSignature() {
		return methodSignature;
	}

	public String getMethodHash() {
		return methodHash;
	}

	public int getType() {
		return type;
	}
	
	
	
}

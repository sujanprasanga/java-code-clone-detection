package lk.ac.mrt.cse.mscresearch.localindex;

import java.io.File;

public class LocalIndexEntry {

	private final String project;
	private final File file;
	private final String clazz;
	private final String clazzHash;
	private final String methodSignature;
	private final String methodHash;
	private final int type;
	
	public LocalIndexEntry(String project, File file, String clazz, String clazzHash, String methodSignature, String methodHash, int type) {
		super();
		this.file = file;
		this.project = project;
		this.clazz = clazz;
		this.clazzHash = clazzHash;
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

	public File getFile() {
		return file;
	}

	public String getClazzHash() {
		return clazzHash;
	}

	public String getLineRange() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}

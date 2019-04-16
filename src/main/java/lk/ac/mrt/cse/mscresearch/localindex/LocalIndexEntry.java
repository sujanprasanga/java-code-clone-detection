package lk.ac.mrt.cse.mscresearch.localindex;

import java.io.File;

import lk.ac.mrt.cse.mscresearch.remoting.dto.MethodDTO;

public class LocalIndexEntry {

	private final String project;
	private final File file;
	private final String clazz;
	private final String clazzHash;
	private final String methodSignature;
	private final String methodHash;
	private final int type;
	private final boolean isSegment;
	private final int[] lineRange;
	
	public LocalIndexEntry(String project, File file, String clazz, String clazzHash, MethodDTO m) {
		LocalMethodDTO l = (LocalMethodDTO)m;
		this.file = file;
		this.project = project;
		this.clazz = clazz;
		this.clazzHash = clazzHash;
		this.methodSignature = m.getSignature();
		this.methodHash = m.getBodyhash();
		this.type = m.getPluginid();
		this.isSegment = l.isSegment();
		this.lineRange = l.getLineNumbers();
	}
	
//	public LocalIndexEntry(String project, File file, String clazz, String clazzHash, String methodSignature, String methodHash, int type, boolean isSegment) {
//		super();
//		this.file = file;
//		this.project = project;
//		this.clazz = clazz;
//		this.clazzHash = clazzHash;
//		this.methodSignature = methodSignature;
//		this.methodHash = methodHash;
//		this.type = type;
//		this.isSegment = isSegment;
//		this.lineRange = new int[0];
//	}

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

	public int[] getLineRange() {
		return lineRange;
	}

	public boolean isSegment() {
		return isSegment;
	}
	
	
	
}

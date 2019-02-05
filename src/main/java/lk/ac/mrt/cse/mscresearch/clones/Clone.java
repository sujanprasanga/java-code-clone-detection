package lk.ac.mrt.cse.mscresearch.clones;

public class Clone {

	enum CloneType{
		LOCAL,
		LIB
	}
	
	private String project;
	private String clazz;
	private String method;
	private String lineRange;
	private String targetArchive;
	private String targetClass;
	private String targetMethod;
	
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getLineRange() {
		return lineRange;
	}
	public void setLineRange(String lineRange) {
		this.lineRange = lineRange;
	}
	public String getTargetArchive() {
		return targetArchive;
	}
	public void setTargetArchive(String targetArchive) {
		this.targetArchive = targetArchive;
	}
	public String getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
	public String getTargetMethod() {
		return targetMethod;
	}
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
}

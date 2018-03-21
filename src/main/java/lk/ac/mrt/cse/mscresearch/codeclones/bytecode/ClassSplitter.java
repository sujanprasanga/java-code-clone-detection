package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class ClassSplitter {

	private final String className;
	private final List<DisassembledMethod> methods = new ArrayList<>();
	private final List<String> methodLines = new ArrayList<>();
	private final List<String> lineNumberTableEntries = new ArrayList<>();
	private final Deque<String> stack = new ArrayDeque<>();
	private int lineCount;
	
	private ClassSplitter(String className){
		this.className = className;
	}
	
	static String test = 
"Compiled from \"LogOutputStream.java\"" +
"public abstract class org.apache.commons.exec.LogOutputStream extends java.io.OutputStream {     \n"+
"  public org.apache.commons.exec.LogOutputStream();                                              \n"+
"    Code:                                                                                        \n"+
"       0: aload_0                                                                                \n"+
"       1: sipush        999                                                                      \n"+
"       4: invokespecial #1                  // Method \"<init>\":(I)V                            \n"+
"       7: return                                                                                 \n"+
"                                                                                                 \n"+
"  public org.apache.commons.exec.LogOutputStream(int);                                           \n"+
"    Code:                                                                                        \n"+
"       0: aload_0                                                                                \n"+
"       1: invokespecial #2                  // Method java/io/OutputStream.\"<init>\":()V        \n"+
"       4: aload_0                                                                                \n"+
"       5: new           #3                  // class java/io/ByteArrayOutputStream               \n"+
"       8: dup                                                                                    \n"+
"       9: sipush        132                                                                      \n"+
"      12: invokespecial #4                // Method java/io/ByteArrayOutputStream.\"<init>\":(I)V\n"+
"      15: putfield      #5                  // Field buffer:Ljava/io/ByteArrayOutputStream;      \n"+
"      18: aload_0                                                                                \n"+
"      19: iconst_0                                                                               \n"+
"      20: putfield      #6                  // Field skip:Z                                      \n"+
"      23: aload_0                                                                                \n"+
"      24: iload_1                                                                                \n"+
"      25: putfield      #7                  // Field level:I                                     \n"+
"      28: return                                                                                 \n"+
"                                                                                                 \n"+
"  public void write(int) throws java.io.IOException;                                             \n"+
"    Code:                                                                                        \n"+
"       0: iload_1                                                                                \n"+
"       1: i2b                                                                                    \n"+
"       2: istore_2                                                                               \n"+
"       3: iload_2                                                                                \n"+
"       4: bipush        10                                                                       \n"+
"       6: if_icmpeq     15                                                                       \n"+
"       9: iload_2                                                                                \n"+
"      10: bipush        13                                                                       \n"+
"      12: if_icmpne     29                                                                       \n"+
"      15: aload_0                                                                                \n"+
"      16: getfield      #6                  // Field skip:Z                                      \n"+
"      19: ifne          37                                                                       \n"+
"      22: aload_0                                                                                \n"+
"      23: invokevirtual #8                  // Method processBuffer:()V                          \n"+
"      26: goto          37                                                                       \n"+
"      29: aload_0                                                                                \n"+
"      30: getfield      #5                  // Field buffer:Ljava/io/ByteArrayOutputStream;      \n"+
"      33: iload_1                                                                                \n"+
"      34: invokevirtual #9                  // Method java/io/ByteArrayOutputStream.write:(I)V   \n"+
"      37: aload_0                                                                                \n"+
"      38: iload_2                                                                                \n"+
"      39: bipush        13                                                                       \n"+
"      41: if_icmpne     48                                                                       \n"+
"      44: iconst_1                                                                               \n"+
"      45: goto          49                                                                       \n"+
"      48: iconst_0                                                                               \n"+
"      49: putfield      #6                  // Field skip:Z                                      \n"+
"      52: return                                                                                 \n"+
"                                                                                                 \n"+
"  public void flush();                                                                           \n"+
"    Code:                                                                                        \n"+
"       0: aload_0                                                                                \n"+
"       1: getfield      #5                  // Field buffer:Ljava/io/ByteArrayOutputStream;      \n"+
"       4: invokevirtual #10                 // Method java/io/ByteArrayOutputStream.size:()I     \n"+
"       7: ifle          14                                                                       \n"+
"      10: aload_0                                                                                \n"+
"      11: invokevirtual #8                  // Method processBuffer:()V                          \n"+
"      14: return                                                                                 \n"+
"    protected abstract void processLine(java.lang.String, int);                                  \n"+
"                                                                                                 \n"+
"  public void close() throws java.io.IOException;                                                \n"+
"    Code:                                                                                        \n"+
"       0: aload_0                                                                                \n"+
"       1: getfield      #5                  // Field buffer:Ljava/io/ByteArrayOutputStream;      \n"+
"       4: invokevirtual #10                 // Method java/io/ByteArrayOutputStream.size:()I     \n"+
"       7: ifle          14                                                                       \n"+
"      10: aload_0                                                                                \n"+
"      11: invokevirtual #8                  // Method processBuffer:()V                          \n"+
"      14: aload_0                                                                                \n"+
"      15: invokespecial #11                 // Method java/io/OutputStream.close:()V             \n"+
"      18: return                                                                                 \n"+
"                                                                                                 \n"+
"    protected abstract void processLine(java.lang.String, int);                                  \n"+
"}";
	private boolean methodStartFound;
	private boolean methodEndFound;
	private String methodName;
	private boolean lineNumberTableFound;
	
	public static List<DisassembledMethod> split(String className, String methodBody){
		return new ClassSplitter(className).split(methodBody.split("\\n"));
	}

	private List<DisassembledMethod> split(String[] split) {
		for(String s : split){
			String trimmed = s.trim();
			findMethod(trimmed);
			findLineNumberEntry(trimmed);
		}
		return methods;
	}

	private void findMethod(String s) {
		stack.push(s);
		if(s.endsWith(");")){
			methodStartFound = true;
			lineCount = 0;
		}
		if(methodStartFound){
			lineCount ++;
		}
		methodEndFound = s.endsWith("return");
		if(methodStartFound && methodEndFound){
			extractMethod();
		}
	}

	private void extractMethod() {
		for(int i = 0; i < lineCount; i++){
			methodLines.add(0, stack.pop());
		}
		addMethod();
		clearAll();
	}
	
	private void addMethod() {
		if(isConstructor()){
			return;
		}
		methods.add(DisassembledMethod.create(className, methodName, methodLines, Collections.unmodifiableList(lineNumberTableEntries)));
	}

	private boolean isConstructor() {
		return className.equals(getMethodName(methodLines.get(0)));
	}

	private String getMethodName(String signature) {
		String signatureUpToMethodName = signature.substring(0, signature.indexOf('('));
		methodName = signatureUpToMethodName.substring(signatureUpToMethodName.lastIndexOf('.') + 1);
		return methodName;
	}

	private void clearAll() {
		methodLines.clear();
		methodStartFound = false;
		methodEndFound = false;
		lineNumberTableFound = false;
		lineNumberTableEntries.clear();
		methodName = "";
		lineCount = 0;
		stack.clear();
	}
	
	private void findLineNumberEntry(String trimmed) {
		if(trimmed.startsWith("LineNumberTable:")){
			lineNumberTableFound = true;
			return;
		}
		if(lineNumberTableFound){
			if(trimmed.startsWith("line")){
				lineNumberTableEntries.add(trimmed.substring(4));
			}
		}
	}
	
	public static void main(String[] args){
		ClassSplitter.split("LogOutputStream", test);
	}
}

package lk.ac.mrt.cse.mscresearch.codeclones.bytecode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MethodSplitter {

	private final static Logger log = Logger.getLogger(MethodSplitter.class);
	
	private final String className;
	private final String methodName;
	private final List<RefactorableEntity> refactorableEntities = new ArrayList<>();
	private int currentLine;
	private final int minimumInstructionCount;
	private final List<Integer> blockEndLocations = new ArrayList<>();
	private final List<Integer>  blockStartLocations = new ArrayList<>();
	private final List<Integer> lables;
	private final  Map<Integer, Integer> lineNumberTable;
	private final FormattedMethod method;
	
	public static void main(String[] a){
		PropertyConfigurator.configure("log4j.properties");
		List<String> code = new ArrayList<>();
		code.add("getstatic     #10                 // Field java/lang/System.out");
		code.add("ldc// String adding");
		code.add("invokevirtual #29                 // Method java/io/PrintStream.println");
		code.add("iload_1");
		code.add("iload_2");
		code.add("iadd");
		code.add("ireturn");
		List<Integer> labless = new ArrayList<>();
		labless.add(1);
		Map<Integer, Integer> lineNumberTables = Collections.emptyMap();
		//		2017-09-30 19:53:08 DEBUG PreProcessor:158 - error convrting to refactorable entity:public int sum(int, int);
//		getstatic     #10                 // Field java/lang/System.out
//		ldc// String adding
//		invokevirtual #29                 // Method java/io/PrintStream.println
//		iload_1
//		iload_2
//		iadd
//		ireturn
		FormattedMethod method = new FormattedMethod("class", "methodName", "methodSignature", code, labless, lineNumberTables);
//		System.out.println(new MethodSplitter(method, Integer.MAX_VALUE).split());
		System.out.println("converted: " + new MethodSplitter(method, Integer.MAX_VALUE).convertToRefactorableEntity());
	}
	
	public MethodSplitter(FormattedMethod method, int minimumBlockSize) {
		super();
		this.method = method;
		this.className = method.getClassName();
		this.methodName = method.getMethodName();
		this.minimumInstructionCount = minimumBlockSize;
		this.lables = method.getLables();
		this.lineNumberTable = method.getLineNumberTable();
		log.debug("Splitting :" + method.toString() + " with block size:" + minimumBlockSize);
	}
	
	public List<RefactorableEntity> split() {
		List<String> code = method.getCode();
		blockStartLocations.add(0);
		for(String s : code){
			findRefactorableBlocks(s.trim());
		}
		blockStartLocations.remove(blockStartLocations.size() - 1);
		blockEndLocations.add(currentLine);
		logBlockLocations(code);
		extractRefactorableEntities(code);
		if(code.size() < minimumInstructionCount)
		{
			convertToRefactorableEntity();
		}
		return refactorableEntities;
	}
	
	public RefactorableEntity convertToRefactorableEntity(){
		return extractRefactorableEntity(method.getCode(), 0, method.getCode().size() - 1);
	}

	private void logBlockLocations(List<String> code) {
		log.debug("Refactorable Blocks:");
		for(int i=0; i<blockStartLocations.size() ; i++){
			log.debug("start:" + blockStartLocations.get(i) + " code: " + code.get(blockStartLocations.get(i)));
			log.debug("end:" + blockEndLocations.get(i) + " code: " + code.get(blockEndLocations.get(i)));
		}
	}

	private void findRefactorableBlocks(String s) {
		if(isBlockEndInstruction(s)){
			blockEndLocations.add(currentLine);
			blockStartLocations.add(currentLine + 1);
		}
		currentLine++;
	}

	private boolean isBlockEndInstruction(String s) {
		String substring = s.substring(1);
		return substring.startsWith("add") || 
			   substring.startsWith("sub") ||
			   substring.startsWith("mul") || 
			   substring.startsWith("div") ||
			   substring.startsWith("store") ||
			   s.startsWith("invokevirtual");
	}


	private void extractRefactorableEntities(List<String> code) {
		for(int refactorableBlockEnd : blockEndLocations){
			for(int refactorableBlockStart : blockStartLocations){
				if(refactorableBlockEnd - refactorableBlockStart >= minimumInstructionCount){
					extractRefactorableEntity(code, refactorableBlockStart, refactorableBlockEnd);
				}
			}
		}
	}

	private RefactorableEntity extractRefactorableEntity(List<String> code, int refactorableBlockStart, int refactorableBlockEnd) {
		log.debug("extracting refactorableEntity starting at " + refactorableBlockStart +" for : " + code);
		log.debug(lables);
		log.debug(lineNumberTable);
		int stratingLine = getLineNumber(refactorableBlockStart);
		RefactorableEntity entity = new RefactorableEntity(methodName, className, stratingLine, 0, code.subList(refactorableBlockStart, refactorableBlockEnd));
		refactorableEntities.add(entity);
		return entity;
	}

	private int getLineNumber(int i) {
		int lable = lables.get(i);
		return findNearestLine(lable);
	}

	private int findNearestLine(int i) {
		for(int j=i; j<i+5; j++){
			if(lineNumberTable.get(j) != null){
				return lineNumberTable.get(j);
			}
		}
		return 0;
	}
}

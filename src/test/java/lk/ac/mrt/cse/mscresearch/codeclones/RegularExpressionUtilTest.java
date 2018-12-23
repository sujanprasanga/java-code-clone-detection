package lk.ac.mrt.cse.mscresearch.codeclones;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lk.ac.mrt.cse.mscresearch.util.PropertyUtil;

public class RegularExpressionUtilTest {
	
	PropertyUtil propertyUtil = new PropertyUtil();
	Pattern allM = 	Pattern.compile(propertyUtil.getMethodSignatureRegEx());
	Pattern accessible = Pattern.compile(propertyUtil.getAccessibleMethodRegEx());
	Pattern abstractM = Pattern.compile(propertyUtil.getAbstractMethodRegEx());
	Pattern nativeM=Pattern.compile(propertyUtil.getNativeMethodRegEx());
	
	protected String readFile(String file) throws IOException {
		List<String> decompiled = Files.readAllLines(Paths.get("src\\test\\java\\resources\\" + file));
		StringBuilder sb = new StringBuilder();
		decompiled.stream().forEach(s->sb.append(s).append('\n'));
		return sb.toString();
	}
	
	@Test(dataProvider="fileNames")
	public void testMethodExtrat(String file) throws Exception{
		String disassembledClass = readFile(file);
		Matcher m = allM.matcher(disassembledClass);
		int i=0;
		while(m.find()){
			i++;
			System.out.println(disassembledClass.substring(m.start(), m.end()));
		}
		assertEquals(i, 12);
	}
	
	 @DataProvider(name = "fileNames")
	  public static Object[][] files() {
	        return new Object[][] { { "AllKindsOfMethods-withLineNumbers.txt"},{ "AllKindsOfMethods.txt" }};
	  }

	 @Test(dataProvider="fileNames")
	 public void testSplitMethods(String file) throws IOException{
		 String disassembledClass = readFile(file);
		 Matcher m = allM.matcher(disassembledClass);
		 Queue<Integer> que = new LinkedList<>();
		 while(m.find()){
			 que.add(m.start());
		 }
		 que.add(disassembledClass.length()-2);
		 while(que.size() > 1){
			System.out.println("<<<<<<<");
			System.out.println(disassembledClass.substring(que.poll(), que.peek()).trim());
			System.out.println(">>>>>>>");
		 }
	 }
	 
	 @Test(dataProvider="methodSignatures")
	 public void test_byte_code_accessibleMethods(String signature, boolean codeAvailable)
	 {
		 Matcher a = accessible.matcher(signature);
		 Matcher abs = abstractM.matcher(signature);
		 Matcher n = nativeM.matcher(signature);
		 boolean isAccessible = a.find();
		 boolean isAbstract = abs.find();
		 boolean isNative = n.find();
		 
		 System.out.println(signature + " is" + (isAccessible ? " ":" not ") +"accssible");
		 System.out.println(signature + " is" + (isAbstract ? " ":" not ") +"abstract");
		 System.out.println(signature + " is" + (isNative ? " ":" not ") +"native");
		 
		assertEquals(isAccessible&& !isAbstract&& !isNative, codeAvailable);
	 }
	 
	 @DataProvider(name = "methodSignatures")
	  public static Object[][] methods() {
	        return new Object[][] { 
	        	{"public lk.clones.AllKindsOfMethods();" ,false},
	        	{"static void foo();" ,false},
	        	{"static int[] foo();",false},
	        	{"public static synchronized void fo_oS();" ,true},
	        	{"public final int fooFIA(java.util.List<java.lang.String>, int, int) throws java.lang.Exception;" ,true},
	        	{"protected native java.lang.Object fooN(int...) throws java.lang.Exception;",false},
	        	{"public native java.lang.Object fooN(int...) throws java.lang.Exception;",false},
	        	{"public abstract java.lang.Object fooA(int...) throws java.lang.Exception;",false},
	        	{"public <T> java.lang.Object fooG1(java.util.List<T>, int...) throws java.lang.Exception;" ,true},
	        	{"public synchronized <T, R> java.util.Map<T, R> fooGS1(java.util.List<T>, java.util.List<R>) throws java.lang.InterruptedException, java.io.IOException;" ,true},
	        	
	        };
	  }
}

package lk.ac.mrt.cse.mscresearch.util;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSourceBrowser {

	private final IOUtil io = new IOUtil();
	
	public static void main(String[] args) throws Exception {
		File f = new File("D:\\development\\runtime-EclipseApplication\\sampleProject\\src\\sampleProject\\a\\c\\a\\asdasd.java");
		String m = "public static <K, V> java.util.Map<java.lang.String, V> prefix(java.util.Map<K, V>, java.lang.String);";
		new JavaSourceBrowser().getLineNumber(m, f);
	}
	
	public int getLineNumber(String methodSignature, File source) throws Exception {
		List<String> lines = io.getStrings(source.getAbsolutePath());
		StringBuilder sb = new StringBuilder();
		lines.stream().forEach(s->sb.append(s).append('\n'));
		String s = sb.toString();
		s = s.replaceAll("(//[^\\n]*)", "").replaceAll("(?s)/\\*.*?\\*/", "");
		Pattern p = Pattern.compile("((public|private|protected|static|final|native|synchronized|abstract|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
		Matcher m = p.matcher(s);
		while(m.find()) {
			System.out.println(s.substring(m.start(), m.end()));
		}
//		System.out.println(s);
		
		return -1;
	}
	
}

package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtilTest {
	
	public static void main(String[] a){
		
		String s = "       9: getstatic     #20                 // Field java/lang/System.out:Ljava/io/PrintStream;";
		//#\\d+[ ]+// Field (?<class>([\\w$]+[/\\w]*)).(?<field>((\\\"<)?[\\w$]+(>\\\")?)):
		Matcher m = Pattern.compile("get((field)|(static))[ ]+#\\d+[ ]+// Field (?<class>([\\w$]+[/\\w]*)).(?<field>((\\\"<)?[\\w$]+(>\\\")?)):").matcher(s);
		m.find();
		System.out.println(s.substring(m.start(), m.end()));
		System.out.println(m.group("class"));
		System.out.println(m.group("field"));
	}

}

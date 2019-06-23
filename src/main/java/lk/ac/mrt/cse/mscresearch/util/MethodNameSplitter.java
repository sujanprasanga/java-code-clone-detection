package lk.ac.mrt.cse.mscresearch.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.ac.mrt.cse.mscresearch.ide.AnnotatedSegment;

public class MethodNameSplitter {

	private static final Pattern accessor = Pattern.compile("^((public )|(private )|(protected ))");
	private static final Pattern staticModifier = Pattern.compile("^static ");
	private static final Pattern finalModifier = Pattern.compile("^final ");
	private static final Pattern synchronizedModifier = Pattern.compile("^synchronized ");
	private static final Pattern namePattern = Pattern.compile(" (?<name>[a-zA-Z\\d_$]+)\\((?<args>.*)\\)( throws .+)?;");
	
	public static String formatMethodName(String n) {
		try {
			String s = stripAccessor(n);
			s = stripStatic(s);
			s = stripFinal(s);
			s = stripSynchronized(s);
			s = stripGeneric(s);
			String[] split = splitNameAndArgs(s);
			return split[1] + "(" + formatArguments(split[2]) + ")" + " : " + formatArguments(split[0]);
		} catch(Exception e) {
			e.printStackTrace();
			return n;
		}
	}

	public static String formatTypeDef(String s) {
		if(s.indexOf('.') < 0) {
			return s;
		}
		if(s.indexOf('<') < 0) {
			return formatClassName(s);
		}
		return fromatGeneric(s);
	}

	protected static String fromatGeneric(String s) {
		int genricStart = s.indexOf('<');
		String g = s.substring(0, genricStart);
		return formatClassName(g) + "<" + formatArguments(s.substring(genricStart + 1 , s.length() - 1)) + ">";
	}

	public static String formatArguments(String s) {
		return s.replaceAll("([a-zA-Z_$]+\\.)+", "");
	}

	public static String stripAccessor(String fullname) {
		Matcher m = accessor.matcher(fullname);
		if(m.find())
		{
			return fullname.substring(m.end());
		}
		return fullname;
	}
	
	public static String stripStatic(String name) {
		Matcher m = staticModifier.matcher(name);
		if(m.find())
		{
			return name.substring(m.end());
		}
		return name;
	}
	
	public static String stripFinal(String name) {
		Matcher m = finalModifier.matcher(name);
		if(m.find())
		{
			return name.substring(m.end());
		}
		return name;
	}
	
	public static String stripSynchronized(String name) {
		Matcher m = synchronizedModifier.matcher(name);
		if(m.find())
		{
			return name.substring(m.end());
		}
		return name;
	}
	
	public static String stripGeneric(String name) {
		if(name.startsWith("<"))
		{
			int openCount = 1;
			char[] c = name.toCharArray();
			for(int i = 1; i<c.length; i++) {
				if(c[i] == '<') {
					openCount++;
				}else if(c[i] == '>') {
					openCount--;
					if(openCount == 0) {
						return name.substring(i+2);
					}
				}
			}
		}
		return name;
	}
	
	public static String[] splitNameAndArgs(String name) {
		String[] r = new String[3];
		Matcher m = namePattern.matcher(name);
		m.find();
		r[0] = name.substring(0, m.start());
		r[1] = m.group("name");
		r[2] = m.group("args");
		return r;
	}
	
	public static String formatClassName(String n) {
		try {
			int i = n.lastIndexOf('.');
			if(i >= 0) {
				return n.substring(i+1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return n;
	}
}

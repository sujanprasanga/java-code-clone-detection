package lk.ac.mrt.cse.mscresearch.ide;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;
import lk.ac.mrt.cse.mscresearch.codeclones.LibMapping;

public class AnnotatedSegment {

	private static final AnnotatedSegment defaultAnnotatedSegment = new AnnotatedSegment(); 
	private static final Pattern accessor = Pattern.compile("^((public )|(private )|(protected ))");
	private static final Pattern staticModifier = Pattern.compile("^static ");
	private static final Pattern finalModifier = Pattern.compile("^final ");
	private static final Pattern synchronizedModifier = Pattern.compile("^synchronized ");
	private static final Pattern namePattern = Pattern.compile(" (?<name>[a-zA-Z\\d_$]+)\\((?<args>.*)\\)( throws .+)?;");
	
	private final Map<Integer, CloneAnnotation> annotations;
	private final int start;
	private final int end;
	
	private AnnotatedSegment(Map<Integer, CloneAnnotation> annotations) {
		this.annotations = annotations;
		start = annotations.keySet().stream().sorted().findFirst().get();
		end = annotations.keySet().stream().sorted((x,y)->y-x).findFirst().get();
	}
	
	private AnnotatedSegment() {
		this.annotations = Collections.emptyMap();
		start = 0;
		end = 0;
	}

	public int getStrat() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public CloneAnnotation get(int i) {
		return annotations.get(i);
	}

	public static AnnotatedSegment forAllClonesForAMethod(List<Clone> c) {
		if(c.isEmpty()) {
			return defaultAnnotatedSegment;
		}
		Map<Integer, CloneAnnotation> annotations = new HashMap<>();
		c.stream().collect(Collectors.groupingBy(e->e.getLineRange()[0])).forEach((i,clones)->addSameLineStarts(annotations, i, clones));
		return new AnnotatedSegment(annotations);
	}

	private static void addSameLineStarts(Map<Integer, CloneAnnotation> annotations, int start, List<Clone> clones) {
		clones.stream().collect(Collectors.groupingBy(e->getRangeEnd(e.getLineRange())))
		               .forEach((end,c)->addSameLineEnds(annotations, start, end, c));
	}
	
	private static int getRangeEnd(int[] a) {
		return a[a.length-1];
	}
	
	private static void addSameLineEnds(Map<Integer, CloneAnnotation> annotations2, int start, int end, List<Clone> clones) {
		StringBuilder sb = new StringBuilder();
		sb.append("Clones found!").append('\n');
		clones.stream().filter(c-> c.getLibMapping() != null)
		               .collect(Collectors.groupingBy(Clone::getLibMapping))
		               .forEach(
		            		   (l,c)-> {
		            			   c.stream().sorted((j,k)->j.getPluginCode()-k.getPluginCode())
	                            	 .findFirst()
	                            	 .ifPresent(y-> AnnotatedSegment.addLibCloneText(sb, y));
		            		   }
		            		   );
		clones.stream().filter(c-> c.getLibMapping() == null)
				       .collect(Collectors.groupingBy(c-> c.getTargetArchive()+c.getTargetClass()+c.getTargetMethod()))
		               .forEach(
		            		   (l,c)-> {
		            			   c.stream().sorted((j,k)->j.getPluginCode()-k.getPluginCode())
	                            	 .findFirst()
	                            	 .ifPresent(y-> AnnotatedSegment.addLocalCloneText(sb, y));
		            		   }
		            		   );
		annotations2.put(start, new CloneAnnotation("clone.annontation.start",0, sb.toString()));
		for(int i = start+1; i<end; i++)
		{
			annotations2.put(i, new CloneAnnotation("clone.annontation.body", 0, null));
		}
	}
	
	private static void addLocalCloneText(StringBuilder sb, Clone c) {
		sb.append(formatCloneText(c.getTargetMethod(), c.getTargetClass(), c.getTargetArchive())).append("\n");
	}

	private static void addLibCloneText(StringBuilder sb, Clone c) {
		LibMapping l = c.getLibMapping();
		sb.append(formatCloneText(c.getTargetMethod(), l.getClazz(), formatArchiveName(l.getArchiveName()))).append("\n");
	}
	
	private static String formatArchiveName(String archiveName) {
		if(archiveName.toLowerCase().endsWith(".jar")) {
			return archiveName.substring(0, archiveName.length() - 4);
		}
		return archiveName;
	}

	private static String formatCloneText(String method, String clazz, String archive) {
		return formatMethodName(method) + " in " + formatClassName(clazz) + " in " + archive;
	}
	
	private static String formatClassName(String n) {
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

	private static String formatArguments(String s) {
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
}

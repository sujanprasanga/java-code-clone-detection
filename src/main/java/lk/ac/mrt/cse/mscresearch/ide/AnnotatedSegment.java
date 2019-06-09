package lk.ac.mrt.cse.mscresearch.ide;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;
import lk.ac.mrt.cse.mscresearch.codeclones.LibMapping;

public class AnnotatedSegment {

	private static final AnnotatedSegment defaultAnnotatedSegment = new AnnotatedSegment(); 
	
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
		sb.append(c.getTargetClass()).append(".").append(c.getTargetMethod()).append(" in ").append(c.getTargetArchive()).append('\n');
	}

	private static void addLibCloneText(StringBuilder sb, Clone c) {
		LibMapping l = c.getLibMapping();
		sb.append(l.getClazz()).append(".").append(c.getTargetMethod()).append(" in ").append(l.getArchiveName()).append('\n');
	}
}

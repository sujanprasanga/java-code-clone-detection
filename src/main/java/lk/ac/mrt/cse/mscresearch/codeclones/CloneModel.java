package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.ide.AnnotatedSegment;

public class CloneModel {

	private static final List<Clone> clones = new ArrayList<>();
	private static final List<Clone> sclones = new ArrayList<>();
//	static {
//		Clone c = new Clone();
//		String project = UUID.randomUUID().toString();
//		c.setProject(project);
//		c.setClazz("c1");
//		c.setMethod("m1");
//		c.setLineRange("12-15");
//		c.setTargetArchive("t1");
//		c.setTargetClass("tc1");
//		c.setTargetMethod("tm1");
//		clones.add(c);
//		
//		c = new Clone();
//		c.setProject(project);
//		c.setClazz("c1");
//		c.setMethod("m1");
//		c.setLineRange("12-34");
//		c.setTargetArchive("t2");
//		c.setTargetClass("tc2");
//		c.setTargetMethod("tm2");
//		clones.add(c);
//		
//		c = new Clone();
//		c.setProject("p2");
//		c.setClazz("c1");
//		c.setMethod("m1");
//		c.setLineRange("12-34");
//		c.setTargetArchive("t2");
//		c.setTargetClass("tc2");
//		c.setTargetMethod("tm2");
//		clones.add(c);
//	}
	
	public static List<Clone> getModel(){
		return clones;
	}

	public static List<Clone> getSegementCloneModel() {
		return sclones;
	}

	public static Collection<Clone> getCompleteModel() {
		ArrayList<Clone> l = new ArrayList<>();
		l.addAll(clones);
		l.addAll(sclones);
		return l;
	}

}

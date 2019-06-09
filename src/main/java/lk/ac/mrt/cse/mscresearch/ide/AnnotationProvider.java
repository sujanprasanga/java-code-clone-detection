package lk.ac.mrt.cse.mscresearch.ide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone;
import lk.ac.mrt.cse.mscresearch.codeclones.CloneModel;

public class AnnotationProvider {

	private static final Map<String, Map<String, List<AnnotatedSegment>>> annotations = new HashMap<>();

	public static List<AnnotatedSegment> getAnnotations(String project, String fqn){
		return annotations.computeIfAbsent(project, e -> new HashMap<>() ).computeIfAbsent(fqn, e-> new ArrayList<>());
	}
	
	public static void updateAnnotations() {
		annotations.clear();
		Map<String, List<Clone>> projects = CloneModel.getCompleteModel().stream().collect(Collectors.groupingBy(Clone::getProject));
		projects.forEach(AnnotationProvider::updateProject);
		System.out.println("Annotations:" + annotations.toString());
	}
	
	public static void updateProject(String project, List<Clone> clones) {
		clones.stream().collect(Collectors.groupingBy(Clone::getClazz)).forEach((c, l)->updateClass(project, c, l));
	}
	
	public static void updateClass(String project, String clazz, List<Clone> clones) {
		clones.stream().collect(Collectors.groupingBy(Clone::getMethod)).forEach((c, l)->updateMethod(project, c, l));
	}
	
	private static void updateMethod(String project, String c, List<Clone> l) {
		if(l.isEmpty()) {
			return;
		}
		getAnnotations(project, l.get(0).getClazz()).add(AnnotatedSegment.forAllClonesForAMethod(l));
	}

}

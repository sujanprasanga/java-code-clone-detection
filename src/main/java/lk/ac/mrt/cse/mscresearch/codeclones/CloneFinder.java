package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone.CloneType;
import lk.ac.mrt.cse.mscresearch.ide.AnnotationProvider;
import lk.ac.mrt.cse.mscresearch.ide.CloneAnnotation;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndex;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndexEntry;
import lk.ac.mrt.cse.mscresearch.remoting.CloneFinderAdaptor;

public class CloneFinder {

	public static void find() {
		EventManager.get().setStatus("finding clones ...");
		Map<String, List<LocalIndexEntry>> grouped = LocalIndex.getLocalIndexes().stream().collect(Collectors.groupingBy(LocalIndexEntry::getMethodHash));
		List<List<LocalIndexEntry>> clones = grouped.values().stream().filter(l->l.size() > 1).collect(Collectors.toList());
		List<Clone> collect = clones.stream().map(CloneFinder::toClone)
				                             .flatMap(List::stream)
				                             .filter(CloneFinder::isValidClone)
				                             .collect(Collectors.toList());
		List<Clone> libClones = findLibClones().stream()
				                               .filter(CloneFinder::isValidLibClone)
                                               .collect(Collectors.toList());
		collect.addAll(libClones);
		CloneModel.getModel().clear();
		CloneModel.getModel().addAll(removeDuplicates(collect));
		
		AnnotationProvider.updateAnnotations();
		EventManager.get().fireUpdateView();
	}
	
	private static List<Clone> removeDuplicates(List<Clone> collect) {
		Map<Integer, List<Clone>> grouped = collect.stream().collect(Collectors.groupingBy(Clone::getCloneUniqueHash));
		return grouped.entrySet().stream().map(CloneFinder::flatten).collect(Collectors.toList());
	}

	private static Clone flatten(Entry<Integer, List<Clone>> entry) {
		return entry.getValue().stream().sorted((x,y) -> x.getPluginCode()-y.getPluginCode()).findFirst().get();
	}
	
	private static List<Clone> findLibClones() {
		CloneFinderAdaptor a = new CloneFinderAdaptor();
		List<CodeFragmentData> codeFragments = LocalIndex.getLocalIndexes().stream().map(CloneFinder::toCodeFragment).collect(Collectors.toList());
		return a.find(codeFragments, LocalIndex.getDependencyMapping());
	}

	private static CodeFragmentData toCodeFragment(LocalIndexEntry localIndexEntry) {
		CodeFragmentData c = new CodeFragmentData();
		c.setProject(localIndexEntry.getProject());
		c.setClazz(localIndexEntry.getClazz());
		c.setMethodHash(localIndexEntry.getMethodHash());
		c.setMethodSignature(localIndexEntry.getMethodSignature());
		c.setLineRange(localIndexEntry.getLineRange());
		c.setTransformerType(localIndexEntry.getType());
		c.setSegment(localIndexEntry.isSegment());
		return c;
	}
	
	private static boolean isValidClone(Clone c) {
		return LocalIndex.isValidDependency(c.getProject(), c.getTargetArchive());
	}
	
	private static boolean isValidLibClone(Clone c) {
		return LocalIndex.isValidDependency(c.getProject(), c.getLibMapping().getArchiveHash());
	}
	
	public static List<Clone> toClone(List<LocalIndexEntry> clones){
		List<Clone> c = new ArrayList<>();
		for(int i=0; i<clones.size(); i++) {
			for(int j=i+1; j < clones.size(); j++) {
				c.add(toClone(clones.get(i), clones.get(j)));
				c.add(toClone(clones.get(j), clones.get(i)));
			}
		}
		return c;
	}

	private static Clone toClone(LocalIndexEntry e1, LocalIndexEntry e2) {
		Clone c = new Clone();
		c.setProject(e1.getProject());
		c.setClazz(e1.getClazz());
		c.setMethod(e1.getMethodSignature());
		c.setTargetArchive(e2.getProject());
		c.setTargetClass(e2.getClazz());
		c.setTargetMethod(e2.getMethodSignature());
		c.setType(CloneType.LOCAL);
		c.setPluginCode(e1.getType());
		c.setLineRange(e1.getLineRange());
		return c;
	}
}

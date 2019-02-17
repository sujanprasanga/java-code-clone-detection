package lk.ac.mrt.cse.mscresearch.codeclones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.codeclones.Clone.CloneType;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndex;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndexEntry;
import lk.ac.mrt.cse.mscresearch.remoting.CloneFinderAdaptor;

public class CloneFinder {

	public static void find() {
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
		CloneModel.getModel().addAll(collect);
	}
	
	private static List<Clone> findLibClones() {
		CloneFinderAdaptor a = new CloneFinderAdaptor();
		List<CodeFragmentData> codeFragments = LocalIndex.getLocalIndexes().stream().map(CloneFinder::toCodeFragment).collect(Collectors.toList());
		return a.find(codeFragments);
	}

	private static CodeFragmentData toCodeFragment(LocalIndexEntry localIndexEntry) {
		CodeFragmentData c = new CodeFragmentData();
		c.setProject(localIndexEntry.getProject());
		c.setClazz(localIndexEntry.getClazz());
		c.setMethod(localIndexEntry.getMethodHash());
		c.setLineRange(localIndexEntry.getLineRange());
		c.setTransformerType(localIndexEntry.getType());
		return c;
	}
	
	private static boolean isValidClone(Clone c) {
		return LocalIndex.isValidDependency(c.getProject(), c.getTargetArchive());
	}
	
	private static boolean isValidLibClone(Clone c) {
		String project = c.getProject();
		for(LibMapping l : c.getLibMapping()) {
			if(LocalIndex.isValidDependency(project, l.getArchiveHash())) {
				return true;
			}
		}
		return false;
	}
	
	private static List<Clone> toClone(List<LocalIndexEntry> clones){
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
		return c;
	}
	
	public static void main(String[] args) {
		CloneFinderAdaptor a = new CloneFinderAdaptor();
		List<CodeFragmentData> codeFragment = new ArrayList<>();
		CodeFragmentData c1 = new CodeFragmentData();
		c1.setProject("ap");
		c1.setClazz("ac");
		c1.setMethod("am");
		c1.setLineRange("alr");
		c1.setMethod("ABFDC5B1BDA51CE2F4E7DDF3639A620A");
		CodeFragmentData c2 = new CodeFragmentData();
		c2.setProject("bp");
		c2.setClazz("bc");
		c2.setMethod("bm");
		c2.setLineRange("blr");
		c2.setMethod("ABFDC5B1BDA51CE2F4E7DDF3639A620A");
		CodeFragmentData c3 = new CodeFragmentData();
		c3.setProject("bp");
		c3.setClazz("bc");
		c3.setMethod("bm");
		c3.setLineRange("blr");
		c3.setMethod("F5D7F4F5749C751D1D3BFDDDD301D05D");
		codeFragment.add(c1);
		codeFragment.add(c2);
		codeFragment.add(c3);
		List<Clone> find = a.find(codeFragment);
		find.stream().forEach(c->{
			System.out.println(c.getProject() + "#" + c.getClazz() + "#" +c.getMethod() + "#" + c.getLineRange() +
					" => " + c.getLibMapping());
		});;
	}
}

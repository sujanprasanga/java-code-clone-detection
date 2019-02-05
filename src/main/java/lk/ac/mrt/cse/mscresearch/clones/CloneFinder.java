package lk.ac.mrt.cse.mscresearch.clones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lk.ac.mrt.cse.mscresearch.localindex.LocalIndex;
import lk.ac.mrt.cse.mscresearch.localindex.LocalIndexEntry;

public class CloneFinder {

	public static void find() {
		Map<String, List<LocalIndexEntry>> grouped = LocalIndex.getLocalIndexes().stream().collect(Collectors.groupingBy(LocalIndexEntry::getMethodHash));
		List<List<LocalIndexEntry>> clones = grouped.values().stream().filter(l->l.size() > 1).collect(Collectors.toList());
		List<Clone> collect = clones.stream().map(CloneFinder::toClone).flatMap(List::stream).collect(Collectors.toList());
		CloneModel.getModel().clear();
		CloneModel.getModel().addAll(collect);
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
		return c;
	}
}

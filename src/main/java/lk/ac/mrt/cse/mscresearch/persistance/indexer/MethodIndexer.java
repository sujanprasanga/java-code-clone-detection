package lk.ac.mrt.cse.mscresearch.persistance.indexer;

import java.util.Set;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.persistance.dao.MethodDAO;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class MethodIndexer {
	
	private final MethodDAO dao;
	
	public MethodIndexer(MethodDAO dao){
		this.dao = dao;
	}

//	public Set<MethodIndex> createMethodIndexes(Set<MethodIndex> methods) {
//		return methods.stream().map(dao::createIfNotExists).collect(Collectors.toSet());
//	}

}

package lk.ac.mrt.cse.mscresearch.persistance.indexer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.persistance.dao.ClassDAO;
import lk.ac.mrt.cse.mscresearch.persistance.dao.JarDAO;
import lk.ac.mrt.cse.mscresearch.persistance.entities.ClassIndex;
import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class ClassIndexer {

	private final ClassDAO dao;
	private final MethodIndexer methodIndexer;
	
	public ClassIndexer(ClassDAO dao, MethodIndexer methodIndexer){
		this.dao = dao;
		this.methodIndexer = methodIndexer;
	}
	
	public Set<ClassIndex> createClassIndexes(Set<ClassIndex> classes) {
//		Set<String> hashes = classes.stream().map(ClassIndex::getClassHash).collect(Collectors.toSet());
//		Map<String, ClassIndex> alreadyIndexed = hashes.stream()
//				                                       .map(dao::getByHashOf)
//				                                       .flatMap(List::stream)
//				                                       .collect(Collectors.toMap(ClassIndex::getClassHash, c->c, (a,b)->a));
//		classes.stream().filter(c->!alreadyIndexed.containsKey(c.getClassHash())).map(dao::)
		return classes.stream().map(this::createMethods).map(dao::createIfNotExists).collect(Collectors.toSet());
	}

	private ClassIndex createMethods(ClassIndex classIndex){
		classIndex.setMethods(methodIndexer.createMethodIndexes(classIndex.getMethods()));
		return classIndex;
	}
}

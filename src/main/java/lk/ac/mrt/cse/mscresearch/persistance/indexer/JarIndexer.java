package lk.ac.mrt.cse.mscresearch.persistance.indexer;

import java.util.Set;
import java.util.stream.Collectors;

import lk.ac.mrt.cse.mscresearch.persistance.dao.JarDAO;
import lk.ac.mrt.cse.mscresearch.persistance.entities.JarIndex;

public class JarIndexer {

	private final JarDAO dao;
	private final ClassIndexer classIndexer;
	
	public JarIndexer(JarDAO dao, ClassIndexer classIndexer){
		this.dao = dao;
		this.classIndexer = classIndexer;
	}
	
//	public Set<String> filterJarsHashesToIndex(Set<String> jars){
//		return jars.stream().filter(s->dao.getByHashOf(s) == null)
//				            .collect(Collectors.toSet());
//	}
//	
//	public void createIndexFor(JarIndex jar){
//		jar.setClasses(classIndexer.createClassIndexes(jar.getClasses()));
//		dao.createIfNotExists(jar);
//	}
}

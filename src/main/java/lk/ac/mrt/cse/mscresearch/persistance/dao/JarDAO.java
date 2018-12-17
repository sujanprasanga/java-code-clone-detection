package lk.ac.mrt.cse.mscresearch.persistance.dao;

import org.hibernate.Session;

import lk.ac.mrt.cse.mscresearch.persistance.entities.JarIndex;

public class JarDAO extends AbstractDAO<JarIndex> {

	public JarDAO(Session session) {
		super(session);
	}

	@Override
	public Class<JarIndex> getEntityClass() {
		return JarIndex.class;
	}

	@Override
	public String getHashQuarryValue(JarIndex entity) {
		return entity.getJarHash();
	}

	@Override
	public String getHashQuarryValueField() {
		return "jarHash";
	}

}

package lk.ac.mrt.cse.mscresearch.persistance.dao;

import org.hibernate.Session;

import lk.ac.mrt.cse.mscresearch.persistance.entities.ClassIndex;

public class ClassDAO extends AbstractDAO<ClassIndex> {

	public ClassDAO(Session session) {
		super(session);
	}

	@Override
	public Class<ClassIndex> getEntityClass() {
		return ClassIndex.class;
	}

	@Override
	protected String getHashQuarryValueField() {
		return "classHash";
	}

	@Override
	protected String getHashQuarryValue(ClassIndex entity) {
		return entity.getClassHash();
	}

}

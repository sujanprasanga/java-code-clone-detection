package lk.ac.mrt.cse.mscresearch.persistance.dao;

import org.hibernate.Session;

import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class MethodDAO extends AbstractDAO<MethodIndex> {

	public MethodDAO(Session session) {
		super(session);
	}

	@Override
	public Class<MethodIndex> getEntityClass() {
		return MethodIndex.class;
	}

	@Override
	protected String getHashQuarryValueField() {
		return "bodyhash";
	}

	@Override
	protected String getHashQuarryValue(MethodIndex entity) {
		return entity.getBodyhash();
	}

}

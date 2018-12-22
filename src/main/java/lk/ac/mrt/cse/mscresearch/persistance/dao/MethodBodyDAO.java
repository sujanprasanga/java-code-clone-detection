package lk.ac.mrt.cse.mscresearch.persistance.dao;

import org.hibernate.Session;

import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodBody;

public class MethodBodyDAO extends AbstractDAO<MethodBody> {

	protected MethodBodyDAO(Session session) {
		super(session);
	}

	@Override
	public Class<MethodBody> getEntityClass() {
		return MethodBody.class;
	}

	@Override
	protected String getHashQuarryValueField() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected String getHashQuarryValue(MethodBody entity) {
		throw new UnsupportedOperationException();
	}

}

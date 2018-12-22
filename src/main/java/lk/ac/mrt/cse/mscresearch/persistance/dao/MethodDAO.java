package lk.ac.mrt.cse.mscresearch.persistance.dao;

import java.util.List;

import org.hibernate.Session;

import lk.ac.mrt.cse.mscresearch.persistance.entities.MethodIndex;

public class MethodDAO extends AbstractDAO<MethodIndex> {

	private final MethodBodyDAO methodBodyDAO;
	
	public MethodDAO(Session session) {
		super(session);
		methodBodyDAO = new MethodBodyDAO(session);
	}

	@Override
	public Class<MethodIndex> getEntityClass() {
		return MethodIndex.class;
	}

	@Override
	protected String getHashQuarryValueField() {
		return "uniqueHash";
	}

	@Override
	protected String getHashQuarryValue(MethodIndex entity) {
		return entity.getUniqueHash();
	}

	@Override
	public MethodIndex createIfNotExists(MethodIndex entity) {
		List<MethodIndex> byHashOf = getByHashOf(getHashQuarryValue(entity));
		if(byHashOf.isEmpty()){
			methodBodyDAO.save(entity.getBodyEntity());
			return save(entity);
		}
		else{
			return byHashOf.get(0);
		}
	}
}

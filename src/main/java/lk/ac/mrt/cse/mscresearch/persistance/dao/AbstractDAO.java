package lk.ac.mrt.cse.mscresearch.persistance.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.query.Query;

import lk.ac.mrt.cse.mscresearch.persistance.entities.EntityId;

public abstract class AbstractDAO<T extends EntityId> implements DAO<T> {

	private final Session session;
	
	protected AbstractDAO(Session session) {
		this.session = session;
	}
	
	@Override
	public T save(T entity) {
		session.persist(entity);
		session.flush();
		return getById(entity.getPrimaryKey());
	}

	@Override
	public T getById(int id){
		return session.get(getEntityClass(), id);
	}
	
	@Override
	public List<T> saveAll(List<T> entities) {
		return entities.stream().map(this::save).collect(Collectors.toList());
	}

//	@Override
//	public T update(T entity) {
//		return null;
//	}
//
//	@Override
//	public List<T> updateAll(List<T> entities) {
//		return null;
//	}

	@Override
	public List<T> getByHashOf(String hash) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(getEntityClass());
		Root<T> root = criteria.from(getEntityClass());
		Predicate predicate = getPredicateForHashCompare(hash, builder, root);
		criteria.select(root).where(predicate);
		Query<T> quarry = session.createQuery(criteria);
		return quarry.getResultList();
	}

	protected Predicate getPredicateForHashCompare(String hash, CriteriaBuilder builder, Root<T> root) {
		Path<T> hashColumn = root.<T> get(getHashQuarryValueField());
		Predicate predicate = builder.equal(hashColumn, hash);
		return predicate;
	}

	@Override
	public List<T> getByIds(List<Integer> ids) {
		MultiIdentifierLoadAccess<T> multiLoadAccess = session.byMultipleIds(getEntityClass());
		return multiLoadAccess.multiLoad(ids);
	}

	@Override
	public T createIfNotExists(T entity) {
		List<T> byHashOf = getByHashOf(getHashQuarryValue(entity));
		if(byHashOf.isEmpty()){
			return save(entity);
		}
		else{
			return byHashOf.get(0);
		}
	}

//	protected <R,P> R inTransaction(Function<P, R> function){
//		Transaction transaction = session.beginTransaction();
//		R r = function.apply(t)
//	}
	
	protected String getPrimaryKeyField(){
		return "primaryKey";
	}
	
	protected abstract String getHashQuarryValueField();
	
	protected abstract String getHashQuarryValue(T entity);
}

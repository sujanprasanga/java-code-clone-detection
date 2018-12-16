package lk.ac.mrt.cse.mscresearch.persistance.dao;

import java.util.List;

import lk.ac.mrt.cse.mscresearch.persistance.entities.EntityId;

public interface DAO<T extends EntityId> {

	T save(T entity);
	List<T> saveAll(List<T> entities);
//	T update(T entity);
//	List<T> updateAll(List<T> entities);
	List<T> getByHashOf(String hash);
	List<T> getByIds(List<Integer> ids);
	T createIfNotExists(T entity);
	Class<T> getEntityClass();
	String getHashQuarryValue(T entity);
	String getHashQuarryValueColumn();
}

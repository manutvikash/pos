package com.increff.project.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

public abstract class AbstractDao {
	
	@PersistenceContext
	private EntityManager em;
	
	//private Class<T> clazz;
	
	
	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}
	
	protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
		return em.createQuery(jpql, clazz);
	}
	
	protected EntityManager em() {
		return em;
	}

	@Transactional
	public <T> void insert(T t) {
		em.persist(t);
	}
	public <T> void update(T t) {
		
	}
	
//	public List<T> selectAll(){
//		return em.createQuery("from"+clazz.getName()).getResultList();
//	}
//	public <T> T select(int id){
//		return (T)em.find( clazz, id );
//	}

}

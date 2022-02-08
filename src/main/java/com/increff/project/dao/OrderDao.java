package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao{

	
    private static String select_all = "select p from OrderPojo p";

	@PersistenceContext
	EntityManager em;
   // @Transactional
    public int insert(OrderPojo p) {
		em.persist(p);
		em.flush();
		return p.getId();
	}
	
	//Delete Order from DB
	public void delete(int id) {
		OrderPojo p = em.find(OrderPojo.class, id);
		em.remove(p);
	}
	
	//Select Order from DB
	public OrderPojo select(int id) {
		return em.find(OrderPojo.class, id);
	}
	
	//Select All Orders from DB
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all,OrderPojo.class);
		return query.getResultList();
	}
	
	//Update Order
	public void update(OrderPojo p) {
		
	}
	
}

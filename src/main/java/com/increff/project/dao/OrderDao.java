package com.increff.project.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao{

	
    private static String SELECT_ALL = "select p from OrderPojo p";
    private static String SELECT_BY_DATE="select p from OrderPojo p where datetime between :startDate and :endDate";
	@PersistenceContext
	EntityManager em;
   // @Transactional
    public int insert(OrderPojo p) {
		em.persist(p);
		em.flush();
		return p.getId();
	}
	
	//Delete Order from DB
	public void delete(Integer id) {
		OrderPojo orderPojo = em.find(OrderPojo.class, id);
		em.remove(orderPojo);
	}
	
	//Select Order from DB
	public OrderPojo select(Integer id) {
		return em.find(OrderPojo.class, id);
	}
	
	//Select All Orders from DB
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL,OrderPojo.class);
		return query.getResultList();
	}
	
	public List<OrderPojo> selectByDate(LocalDateTime startDate,LocalDateTime endDate){
		TypedQuery<OrderPojo> query=getQuery(SELECT_BY_DATE,OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
	//Update Order
	public void update(OrderPojo orderPojo) {
		
	}
	
}

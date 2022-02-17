package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao{
	
	private static String SELECT_ALL = "select p from OrderItemPojo p";
	private static String SELECT_ORDER = "select p from OrderItemPojo p where orderId=:orderId";
	    @PersistenceContext
	    EntityManager em;
	  //Insert OrderItem to DB
		public void insert(OrderItemPojo orderItemPojo) {
			em.persist(orderItemPojo);
		}
		
		//Delete OrderItem from DB
		public void delete(Integer id) {
			OrderItemPojo orderItemPojo = em.find(OrderItemPojo.class, id);
			em.remove(orderItemPojo);
		}
		
		//Select OrderItem from DB
		public OrderItemPojo select(Integer id) {
			return em.find(OrderItemPojo.class, id);
		}
		
		//Select all OrderItems from DB
		public List<OrderItemPojo> selectAll() {
			TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL,OrderItemPojo.class);
			return query.getResultList();	
		}
		
		//Select all OrderItems from DB of a particular order
		public List<OrderItemPojo> selectOrder(Integer orderId) {
			TypedQuery<OrderItemPojo> query = getQuery(SELECT_ORDER,OrderItemPojo.class);
			query.setParameter("orderId", orderId);
			return query.getResultList();
		}
		
		//Update Order Item
		public void update(OrderItemPojo orderItemPojo) {
			
		}
		
	 
}

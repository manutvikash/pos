package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao{
	
	private static String select_all = "select p from OrderItemPojo p";
	private static String select_order = "select p from OrderItemPojo p where orderId=:orderId";
	
	    @PersistenceContext
	    EntityManager em;
	  //Insert OrderItem to DB
		public void insert(OrderItemPojo p) {
			em.persist(p);
		}
		
		//Delete OrderItem from DB
		public void delete(int id) {
			OrderItemPojo p = em.find(OrderItemPojo.class, id);
			em.remove(p);
		}
		
		//Select OrderItem from DB
		public OrderItemPojo select(int id) {
			return em.find(OrderItemPojo.class, id);
		}
		
		//Select all OrderItems from DB
		public List<OrderItemPojo> selectAll() {
			TypedQuery<OrderItemPojo> query = getQuery(select_all,OrderItemPojo.class);
			return query.getResultList();	
		}
		
		//Select all OrderItems from DB of a particular order
		public List<OrderItemPojo> selectOrder(int orderId) {
			TypedQuery<OrderItemPojo> query = getQuery(select_order,OrderItemPojo.class);
			query.setParameter("orderId", orderId);
			return query.getResultList();
		}
		
		//Update Order Item
		public void update(OrderItemPojo p) {
			
		}
		
	 
}

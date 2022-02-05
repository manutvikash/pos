package com.increff.project.dao;


import com.increff.project.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static String select_id = "select p from OrderItemPojo p where id=:id";
    private static String select_all = "select p from OrderItemPojo p";
    private static String selectByOrderId = "select p from OrderItemPojo p where p.orderId=:id";

    @PersistenceContext
	EntityManager em;
    
    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        em.persist(orderItemPojo);
    }

    public OrderItemPojo select(int id) {
        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = em.createQuery(select_all, OrderItemPojo.class);
        return query.getResultList();
    }

    public void update(OrderItemPojo pojo) {
    }


    public List<OrderItemPojo> getByOrderId(int id) {
        TypedQuery<OrderItemPojo> query = em.createQuery(selectByOrderId, OrderItemPojo.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

  
}

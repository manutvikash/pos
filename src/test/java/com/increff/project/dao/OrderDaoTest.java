package com.increff.project.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.pojo.OrderPojo;
import com.increff.project.service.ApiException;
import com.increff.project.spring.AbstractUnitTest;

public class OrderDaoTest extends AbstractUnitTest{

	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Autowired
	private OrderDao order_dao;
	
	@Test
	public void testInsert() {
		List<OrderPojo> order_list_before = order_dao.selectAll();
		OrderPojo order = getOrderPojo();
		order_dao.insert(order);
		List<OrderPojo> order_list_after = order_dao.selectAll();
		assertEquals(order_list_before.size()+1,order_list_after.size());
	}
	
	@Test
	public void testDelete() {
		
		OrderPojo order = getOrderPojo();
		order_dao.insert(order);
		List<OrderPojo> order_list_before = order_dao.selectAll();
		order_dao.delete(order.getId());
		List<OrderPojo> order_list_after = order_dao.selectAll();
		assertEquals(order_list_before.size()-1,order_list_after.size());
	}
	

	
	private OrderPojo getOrderPojo() {
		OrderPojo order = new OrderPojo();
		order.setDatetime(LocalDateTime.now());
		return order;
	}
}

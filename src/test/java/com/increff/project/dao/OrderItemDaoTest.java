package com.increff.project.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.spring.AbstractUnitTest;

public class OrderItemDaoTest extends AbstractUnitTest {

	@Autowired
	private OrderDao order_dao;

	@Autowired
	private OrderItemDao orderitem_dao;

	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}

	@Test
	public void testInsert() {
		List<OrderItemPojo> orderitem_list_before = orderitem_dao.selectAll();

		OrderPojo order = getOrderPojo();
		order_dao.insert(order);
		OrderItemPojo orderitem = getOrderItemPojo(products.get(0), 2);
		orderitem.setOrderpojo(order);
		orderitem_dao.insert(orderitem);

		List<OrderItemPojo> orderitem_list_after = orderitem_dao.selectAll();
		assertEquals(orderitem_list_before.size() + 1, orderitem_list_after.size());
		assertEquals(orderitem.getProductpojo(), orderitem_dao.select(orderitem.getId()).getProductpojo());
		assertEquals(orderitem.getQuantity(), orderitem_dao.select(orderitem.getId()).getQuantity());
		assertEquals(orderitem.getSellingPrice(), orderitem_dao.select(orderitem.getId()).getSellingPrice(), 0.001);
	}

	@Test
	public void testDelete() {

		OrderPojo order = getOrderPojo();
		order_dao.insert(order);
		OrderItemPojo orderitem = getOrderItemPojo(products.get(0), 2);
		orderitem.setOrderpojo(order);
		orderitem_dao.insert(orderitem);

		List<OrderItemPojo> orderitem_list_before = orderitem_dao.selectAll();
		orderitem_dao.delete(orderitem.getId());
		List<OrderItemPojo> orderitem_list_after = orderitem_dao.selectAll();
		assertEquals(orderitem_list_before.size() - 1, orderitem_list_after.size());
	}
	


	private OrderPojo getOrderPojo() {
		OrderPojo order = new OrderPojo();
		order.setDatetime(LocalDateTime.now());
		return order;
	}

	private OrderItemPojo getOrderItemPojo(ProductPojo p, Integer quantity) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductpojo(p);
		order_item.setQuantity(quantity);
		order_item.setSellingPrice(p.getMrp());
		return order_item;
	}

}

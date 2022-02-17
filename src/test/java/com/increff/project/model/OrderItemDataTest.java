package com.increff.project.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.project.spring.AbstractUnitTest;

public class OrderItemDataTest extends AbstractUnitTest {

	@Test
	public void testOrderItemData() {
		Integer id = 1;
		String barcode = "123";
		Integer quantity = 2;
		String name = "vikash";
		Double price = 800.00;
		Integer orderId = 1;
		
		OrderItemData orderitem = new OrderItemData();
		orderitem.setId(id);
		orderitem.setBarcode(barcode);
		orderitem.setSellingPrice(price); 
		orderitem.setName(name);
		orderitem.setOrderId(orderId);
		orderitem.setQuantity(quantity);
		
		assertEquals(id,orderitem.getId());
		assertEquals(barcode,orderitem.getBarcode());
		assertEquals(price,orderitem.getSellingPrice(),0.001);
		assertEquals(name,orderitem.getName());
		assertEquals(orderId,orderitem.getOrderId(),0.001);
		assertEquals(quantity,orderitem.getQuantity());
	}
}

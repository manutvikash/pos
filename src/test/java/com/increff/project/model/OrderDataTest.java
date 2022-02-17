package com.increff.project.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.project.spring.AbstractUnitTest;

public class OrderDataTest extends AbstractUnitTest {

	@Test
	public void testOrderData() {
		Integer id = 1;
		String datetime = "2022-02-11";
		Integer amount=10;
		boolean invoice=true;
		
		OrderData order = new OrderData();
		order.setDatetime(datetime);
		order.setId(id);
		order.setAmount(amount);
		order.setInvoiceCreated(invoice);
		
		assertEquals(id,order.getId());
		assertEquals(datetime,order.getDatetime());
		assertEquals(invoice,order.getInvoiceCreated());
	}

}

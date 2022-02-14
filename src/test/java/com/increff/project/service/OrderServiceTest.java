package com.increff.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.spring.AbstractUnitTest;

public class OrderServiceTest extends AbstractUnitTest{

	
	@Before
	public void init() throws ApiException {
		// Inserting initial pojos
		insertPojos();		
	}

	// Testing adding of order
	@Test
	public void testAdd() throws ApiException {

		OrderItemPojo order_item = getOrderItemPojo(products.get(0), 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		List<OrderPojo> order_list_before = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_before = order_service.getAll();
		order_service.add(lis);
		List<OrderPojo> order_list_after = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_after = order_service.getAll();

		assertEquals(order_list_before.size() + 1, order_list_after.size());
		assertEquals(orderitem_list_before.size() + 1, orderitem_list_after.size());
		List<OrderItemPojo> db_orderitem_list = order_service.getOrderItems(order_item.getOrderpojo().getId());
		assertEquals(lis.size(), db_orderitem_list.size());
		assertEquals(order_item.getOrderpojo(), db_orderitem_list.get(0).getOrderpojo());
		assertEquals(order_item.getProductpojo(), db_orderitem_list.get(0).getProductpojo());
		assertEquals(order_item.getQuantity(), db_orderitem_list.get(0).getQuantity());
		assertEquals(order_item.getSellingPrice(), db_orderitem_list.get(0).getSellingPrice(), 0.001);

	}
	
	//Testing addition of order item to an existing order 
	@Test
	public void testAddOrderItem() throws ApiException {

		//getInventoryPojo(products.get(2));
		OrderItemPojo new_order_item = getOrderItemPojo(products.get(2), 6);
		order_service.addOrderItem(order_id, new_order_item);

		assertEquals(2, order_service.getOrderItems(order_id).size());
	}
	
	// Testing adding of invalid order. Exception should be thrown 
	@Test
	public void testAddWrong() throws ApiException {

		OrderItemPojo order_item = getWrongOrderItemPojo(products.get(0));
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		try {
			order_service.add(lis);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Quantity must be positive");
		}

	}
	
	@Test
	public void testAddNullProduct() throws ApiException {

		OrderItemPojo order_item = getOrderItemPojo(null,5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		try {
			order_service.add(lis);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "An invalid product was entered. Please check");
		}

	}
	

     //	 Testing Get for order items 
	@Test
	public void testGet() throws ApiException {

		OrderItemPojo db_orderitem_pojo = order_service.get(orderitems.get(0).getId());
		assertEquals(orderitems.get(0).getOrderpojo(), db_orderitem_pojo.getOrderpojo());
		assertEquals(orderitems.get(0).getProductpojo(), db_orderitem_pojo.getProductpojo());
		assertEquals(orderitems.get(0).getQuantity(), db_orderitem_pojo.getQuantity());
		assertEquals(orderitems.get(0).getSellingPrice(), db_orderitem_pojo.getSellingPrice(), 0.001);
	}
	
	// Testing Get for order
	@Test
	public void testGetOrder() throws ApiException {

		OrderPojo db_order_pojo = order_service.getOrder(order_id);
		assertEquals(order_id, db_order_pojo.getId());
	}
	
	// Testing getting all order items for a particular order 
	@Test
	public void testGetOrderItemsByOrderId() throws ApiException {

		List<OrderItemPojo> orderitem_list = order_service.getOrderItems(order_id);
		assertEquals(2, orderitem_list.size());
	}

	// Testing getting all orders 
	@Test
	public void testGetAllOrders() throws ApiException {

		List<OrderPojo> orderitem_list = order_service.getAllOrders();
		assertEquals(1, orderitem_list.size());
	}
	

	

	
	
	// Testing deletion of order items 
	@Test
	public void testDelete() throws ApiException {

		OrderItemPojo order_item = getOrderItemPojo(products.get(0), 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		order_service.add(lis);

		int id = order_item.getId();
		List<OrderPojo> order_list_before = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_before = order_service.getAll();
		order_service.delete(id);
		List<OrderPojo> order_list_after = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_after = order_service.getAll();
		assertEquals(order_list_before.size() - 1, order_list_after.size());
		assertEquals(orderitem_list_before.size() - 1, orderitem_list_after.size());
		try {
			order_service.get(id);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "OrderItem with given ID does not exist, id: " + id);
		}
	}
	
	// Testing updation of order items 
	@Test
	public void testUpdate() throws ApiException {

		OrderItemPojo new_order_item = getOrderItemPojo(products.get(0), 7);
		order_service.update(orderitems.get(0).getId(), new_order_item);
		assertEquals(orderitems.get(0).getProductpojo(), new_order_item.getProductpojo());
		assertEquals(orderitems.get(0).getQuantity(), new_order_item.getQuantity());
		assertEquals(orderitems.get(0).getSellingPrice(), new_order_item.getSellingPrice(), 0.001);
	}
	
	

    
	// Testing updation of inventory during editing of order items
	@Test
	public void testUpdateInventoryDuringEdit() throws ApiException {
		order_service.updateInventory(getOrderItemPojo(products.get(1), 3), 2);
		assertEquals(19, inventory_service.getByProductId(products.get(1).getId()).getQuantity());
	}

	@Test
	public void testUpdateInventoryDuringEditExceeding() throws ApiException {

		try {
			order_service.updateInventory(getOrderItemPojo(products.get(0), 100), 2);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory does not contain this much quantity of product. Existing quantity in inventory: "
							+ (inventory_service.getByProductId(products.get(0).getId()).getQuantity() + 2));
		}

	}
	@Test
	public void testUpdateInventoryExceeding() throws ApiException {

		try {
			order_service.updateInventory(getOrderItemPojo(products.get(0), 100), 0);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory does not contain this much quantity of product. Existing quantity in inventory: "
							+ inventories.get(0).getQuantity());
		}

	}
	// Testing checkifexists for a non-existent order. Should throw exception 
	@Test
	public void testCheckIfExistsOrderWrong() throws ApiException {

		try {
			order_service.checkIfExistsOrder(100);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Order with given ID does not exist, id: " + 100);
		}

	}
	
	@Test
	public void testCheckIfExistsWrong() throws ApiException {
		int orderitem_id = 5;

		try {
			order_service.checkIfExists(orderitem_id);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "OrderItem with given ID does not exist, id: " + orderitem_id);
		}

	}
	
	private OrderItemPojo getOrderItemPojo(ProductPojo p, int quantity) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductpojo(p);
		order_item.setQuantity(quantity);
		if(p!=null) {
			order_item.setSellingPrice(p.getMrp());
		}
		return order_item;
	}
	

	private OrderItemPojo getWrongOrderItemPojo(ProductPojo p) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductpojo(p);
		order_item.setQuantity(-5);
		order_item.setSellingPrice(543);
		return order_item;
	}

	
}

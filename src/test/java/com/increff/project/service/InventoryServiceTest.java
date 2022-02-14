package com.increff.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.spring.AbstractUnitTest;

public class InventoryServiceTest extends AbstractUnitTest {

	@Before
	public void init() throws ApiException {
		// Inserting initial pojos into the in-memory DB
		insertPojos();
	}

	/* Testing adding of inventory pojo */
	@Test()
	public void testAdd() throws ApiException {

		InventoryPojo i = getInventoryPojo(products.get(3));
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.add(i);
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		// Number of brand pojos should increase by one
		assertEquals(inv_list_before.size() + 1, inv_list_after.size());
		assertEquals(i.getProductPojo(), inventory_service.get(i.getId()).getProductPojo());
		assertEquals(i.getQuantity(), inventory_service.get(i.getId()).getQuantity());

	}
	
	
	@Test()
	public void testAddDuplicate() throws ApiException {

		InventoryPojo i = getInventoryPojo(products.get(3));
		inventory_service.add(i);

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory for this product already exists.");
		}

	}
	
	/* Testing adding of an invalid pojo. Should throw exception */
	@Test()
	public void testAddWrong() throws ApiException {

		InventoryPojo i = getWrongInventoryPojo(products.get(0));

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}

	}
	/* Testing get by id */
	@Test()
	public void testGetById() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.get(inventories.get(0).getId());
		assertEquals(inventories.get(0).getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());

	}
	
	@Test()
	public void testUpdate() throws ApiException {
		InventoryPojo i=getNewInventoryPojo(products.get(0));
		inventory_service.update(inventories.get(0).getId(), i);
		assertEquals(i.getProductPojo(), inventories.get(0).getProductPojo());
		assertEquals(i.getQuantity(), inventories.get(0).getQuantity());
	}
	private InventoryPojo getInventoryPojo(ProductPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(3);
		return i;
	}
	
	@Test()
	public void testGetByIdNotExisting() throws ApiException {

		int id = 5;
		try {
			inventory_service.get(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + id);
		}

	}
	
	@Test()
	public void testGetByProductId() throws ApiException {
		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(inventories.get(0).getProductPojo().getId());
		assertEquals(inventories.get(0).getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());

	}
	
	@Test()
	public void testGetByProductIdWrong() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(30);
		assertNull(db_inventory_pojo);

	}
	
	private InventoryPojo getNewInventoryPojo(ProductPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(4);
		return i;
	}
	private InventoryPojo getWrongInventoryPojo(ProductPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(-5);
		return i;
	}
}

package com.increff.project.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.project.spring.AbstractUnitTest;

public class InventoryDataTest extends AbstractUnitTest {

	@Test
	public void testInventoryData() {
		Integer id = 1;
		String barcode = "123";
		String name="vikash";
		String brand="adidas";
		String category="shoes";
		Integer quantity = 20;
		
		InventoryData inventory = new InventoryData();
		inventory.setBarcode(barcode);
		inventory.setId(id);
		inventory.setQuantity(quantity);
		inventory.setBrand(brand);
		inventory.setCategory(category);
		inventory.setName(name);
		
		assertEquals(id,inventory.getId());
		assertEquals(barcode,inventory.getBarcode());
		assertEquals(quantity,inventory.getQuantity());
		assertEquals(name,inventory.getName());
		assertEquals(brand,inventory.getBrand());
		assertEquals(category,inventory.getCategory());
	}
}

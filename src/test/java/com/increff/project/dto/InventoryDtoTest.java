package com.increff.project.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.service.ApiException;
import com.increff.project.spring.AbstractUnitTest;

public class InventoryDtoTest extends AbstractUnitTest {

	@Autowired
	private InventoryDto inventoryDto;
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
//	
//	@Test
//	public void testAdd() throws ApiException{
//		InventoryForm inventoryForm = new InventoryForm();
//		inventoryForm.setBarcode("x0"); 
//		inventoryForm.setQuantity(30);
//
//		assertEquals(3, inventoryDto.getAll().size());
//		inventoryDto.update(1, inventoryForm); 
//		
//		assertEquals(30,inventoryDto.get(1).getQuantity());
//	}
	@Test
	public void testGet() throws ApiException{
		Integer id=inventories.get(0).getId();
		InventoryData inventoryData=inventoryDto.get(id);
		assertEquals("product0", inventoryData.getName());
		assertEquals(20, inventoryData.getQuantity(),0.001);
		assertEquals("x0", inventoryData.getBarcode());
	}
	

	@Test
	public void testUpdate() throws ApiException{
		Integer id=inventories.get(0).getId();
		InventoryForm inventoryForm=new InventoryForm();
		inventoryForm.setQuantity(30);
		inventoryForm.setBarcode("x0");
		inventoryDto.update(id, inventoryForm);
		InventoryData inventoryData=inventoryDto.get(id);
//		assertEquals(30,inventoryData.getQuantity());
	}
	
	@Test
	public void testGetId() throws ApiException{
		InventoryForm inventoryForm = new InventoryForm();
		inventoryForm.setBarcode("x0"); 
		inventoryForm.setQuantity(30);
		
		inventoryDto.getid(inventoryForm);
	}
	
}

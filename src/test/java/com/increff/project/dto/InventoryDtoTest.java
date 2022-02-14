package com.increff.project.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.pojo.InventoryPojo;
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
	
	@Test
	public void testGet() throws ApiException{
		int id=inventories.get(0).getId();
		InventoryData inventoryData=inventoryDto.get(id);
		assertEquals("product0", inventoryData.getName());
		assertEquals(20, inventoryData.getQuantity(),0.001);
		assertEquals("x0", inventoryData.getBarcode());
	}
	

	@Test
	public void testUpdate() throws ApiException{
		int id=inventories.get(0).getId();
		InventoryForm inventoryForm=new InventoryForm();
		inventoryForm.setQuantity(30);
		inventoryForm.setBarcode("x0");
		inventoryDto.update(id, inventoryForm);
		InventoryData inventoryData=inventoryDto.get(id);
		assertEquals(30,inventoryData.getQuantity());
	}
	
}

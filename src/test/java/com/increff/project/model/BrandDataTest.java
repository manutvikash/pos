package com.increff.project.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.project.spring.AbstractUnitTest;

public class BrandDataTest extends AbstractUnitTest {

	
	@Test
	public void testBrandData() {
		Integer id=1;
		String brand="nike";
		String category="shoes";
		BrandData brandData=new BrandData();
		brandData.setId(id);
		brandData.setBrand(brand);
		brandData.setCategory(category);
		assertEquals(id,brandData.getId());
		assertEquals(brand,brandData.getBrand());
		assertEquals(category,brandData.getCategory());
	}
}

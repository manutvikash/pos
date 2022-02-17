package com.increff.project.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.project.spring.AbstractUnitTest;

public class ProductDataTest extends AbstractUnitTest {
	@Test
	public void testProductData() {
		Integer id = 1;
		String brand = "adidas";
		String category = "shoes";
		String name = "adidas air";
		double mrp = 70.00;
		String barcode = "123";
		
		ProductData product = new ProductData();
		product.setBarcode(barcode);
		product.setBrand(brand);
		product.setCategory(category);
		product.setId(id);
		product.setMrp(new Double(mrp));
		product.setName(name);
		
		assertEquals(brand,product.getBrand());
		assertEquals(category,product.getCategory());
		assertEquals(name,product.getName());
		assertEquals(mrp, product.getMrp(),0.001);
		assertEquals(barcode, product.getBarcode());
		assertEquals(id,product.getId());
	}
}

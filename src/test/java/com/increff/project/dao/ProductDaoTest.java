package com.increff.project.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.spring.AbstractUnitTest;

public class ProductDaoTest extends AbstractUnitTest{
	@Autowired
	private ProductDao product_dao;
	
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Test
	public void testInsert() {
		List<ProductPojo> product_list_before = product_dao.selectAll();
		ProductPojo product = getProductPojo(brands.get(0));
		product_dao.insert(product);
		
		List<ProductPojo> product_list_after = product_dao.selectAll();
		assertEquals(product_list_before.size()+1,product_list_after.size());
		assertEquals("milk",product_dao.select(product.getId()).getName());
		assertEquals(50,product_dao.select(product.getId()).getMrp(),0.001);
		assertEquals(brands.get(0),product_dao.select(product.getId()).getBrandpojo());
		
	}
	

	
	
	@Test
	public void testSelectAll() {
		
		List<ProductPojo> product_list = product_dao.selectAll();
		assertEquals(3,product_list.size());
		
	}
	
	private ProductPojo getProductPojo(BrandPojo b){
		ProductPojo p = new ProductPojo();
		p.setBrandpojo(b);
		p.setName("milk");
		p.setMrp(50);
		return p;
	}

	
}

package com.increff.project.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.pojo.BrandPojo;
import com.increff.project.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService service;
	@Test
	public void testAdd(){
		BrandPojo p=new BrandPojo();
		p.setBrand("Vikash");
		p.setCategory("Singh");
		//service.add(p);
		
	}
	@Test
	public void testNormalize() {
		BrandPojo p=new BrandPojo();
		p.setBrand("nike");
		p.setCategory("shoes");
		BrandService.normalize(p);
		assertEquals("nike",p.getBrand());
	}
}

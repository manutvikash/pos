package com.increff.project.dto;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.spring.AbstractUnitTest;

@Transactional
public class BrandDtoTest extends AbstractUnitTest{

	@Autowired
	private BrandDto brandDto;
	@Autowired
	private BrandService brandService;
	
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Test
	public void testAdd() throws ApiException{
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand("amul");
		brandForm.setCategory("milk");
		brandDto.add(brandForm);
		assertEquals(4, brandDto.getAll().size());
	}
	
	@Test
	public void testGet() throws ApiException {
		int id=brands.get(0).getId();
		BrandData brandData = brandDto.get(id);
		assertEquals("brand", brandData.getBrand());
		assertEquals("category0", brandData.getCategory());
	}
	
	@Test
	public void testUpdate() throws ApiException {
		int id=brands.get(0).getId();
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand("amul");
		brandForm.setCategory("milk");
		brandDto.update(id, brandForm);
		assertEquals("amul",brandDto.get(id).getBrand());
		assertEquals("milk",brandDto.get(id).getCategory());
	}
	
	@Test
	public void testSearch() throws ApiException{
		int id=brands.get(0).getId();
		BrandForm brandForm = new BrandForm();
		brandForm.setBrand("amul");
		brandForm.setCategory("milk");
		brandDto.search(brandForm);
	}
	
}

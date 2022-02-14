package com.increff.project.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.model.ProductFormSearch;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.InventoryService;
import com.increff.project.service.ProductService;
import com.increff.project.spring.AbstractUnitTest;

public class ProductDtoTest extends AbstractUnitTest{

	@Autowired
	private ProductDto productDto;
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ProductService productService;
	
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Test
	public void testAdd() throws ApiException {
		ProductForm productForm=new ProductForm();
		productForm.setBarcode("123");
		productForm.setMrp(999);
		productForm.setName("air");
		productForm.setBrand(brands.get(0).getBrand());
		productForm.setCategory(brands.get(0).getCategory());
		productDto.add(productForm);		
		assertEquals(5,productDto.getAll().size());
		
	}
	
	@Test
	public void testGet() throws ApiException{
		int id=products.get(0).getId();
		ProductData productData=productDto.get(id);
		assertEquals("product0", productData.getName());
		assertEquals(50, productData.getMrp(),0.001);
		assertEquals("x0", productData.getBarcode());
	}
	
	@Test
	public void testSearch() throws ApiException{
		ProductForm productForm=new ProductForm();
		productForm.setBarcode("123");
		productForm.setMrp(999);
		productForm.setName("air");
		productForm.setBrand(brands.get(0).getBrand());
		productForm.setCategory(brands.get(0).getCategory());
		productDto.add(productForm);	
		
		ProductFormSearch productFormSearch=new ProductFormSearch();
		productFormSearch.setBarcode("123");
		ProductData p=productDto.search(productFormSearch);
		
		assertEquals(p.getBarcode(),productForm.getBarcode());
		assertEquals(p.getBrand(),productForm.getBrand());
		assertEquals(p.getCategory(),productForm.getCategory());
		assertEquals(p.getMrp(),productForm.getMrp(),0.001);
		assertEquals(p.getName(),productForm.getName());
	}
	
	@Test
	public void testUpdate() throws ApiException{
		int id=products.get(0).getId();
		ProductForm p=new ProductForm();
		p.setMrp(1000);
		p.setBrand("brand");
		p.setCategory("category0");
		p.setBarcode("12");
		p.setName("Vikash");
		productDto.update(id, p);
		
		ProductData productData=productDto.get(id);
		assertEquals(1000,productData.getMrp(),0.001);
		assertEquals("brand",productData.getBrand());
		assertEquals("category0",productData.getCategory());
		assertEquals("vikash",productData.getName());
		assertEquals("12",productData.getBarcode());
		
	}

	
}

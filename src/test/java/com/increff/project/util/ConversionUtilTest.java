package com.increff.project.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.model.ProductForm;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.spring.AbstractUnitTest;

public class ConversionUtilTest extends AbstractUnitTest{

	@Before
	public void init() throws ApiException{
		insertPojos();
	}
	
	@Test
	public void testConverttoBrandPojo() {
		BrandForm f=new BrandForm();
		f.setBrand("adidas");
		f.setCategory("shirt");
		BrandPojo brandPojo=ConversionUtil.convert(f);
		assertEquals(f.getBrand(), brandPojo.getBrand());
		assertEquals(f.getCategory(),brandPojo.getCategory());
	}
	@Test
	public void testConverttoBrandData() {
		BrandPojo f=new BrandPojo();
		f.setBrand("adidas");
		f.setCategory("shirt");
		BrandData brandData=ConversionUtil.convert(f);
		assertEquals(f.getBrand(), brandData.getBrand());
		assertEquals(f.getCategory(),brandData.getCategory());
	}
	
	@Test
	public void testConverttoBrandForm() {
		BrandPojo f=new BrandPojo();
		f.setBrand("adidas");
		f.setCategory("shirt");
		BrandForm brandForm=ConversionUtil.convertBrandPojotoBrandForm(f);
		assertEquals(f.getBrand(), brandForm.getBrand());
		assertEquals(f.getCategory(),brandForm.getCategory());
	}
	

	@SuppressWarnings("deprecation")
	@Test
	public void testConverttoProductPojo() throws ApiException{
		ProductForm f=new ProductForm();
		f.setBarcode("123");
		f.setMrp(2345);
		f.setName("Vikash");
	
		f.setBrand(brands.get(0).getBrand());
		f.setCategory(brands.get(0).getCategory());
		ProductPojo productPojo=ConversionUtil.convert(brands.get(0), f);
		assertEquals(f.getBarcode(),productPojo.getBarcode());
		assertEquals(f.getName(),productPojo.getName());
		assertEquals(f.getMrp(),productPojo.getMrp(),0.0);
		
		assertEquals(f.getBrand(), productPojo.getBrandpojo().getBrand());
		assertEquals(f.getCategory(),productPojo.getBrandpojo().getCategory());
	}
	
	@Test
	public void testConvertProductFormtoBrandPojo()throws ApiException{
		ProductForm f=new ProductForm();
		f.setBarcode("123");
		f.setBrand(brands.get(0).getBrand());
		f.setCategory(brands.get(0).getCategory());
		
		BrandPojo brandPojo=ConversionUtil.convertProductFormtoBrandPojo(f);
		assertEquals(f.getBrand(),brandPojo.getBrand());
		assertEquals(f.getCategory(),brandPojo.getCategory());
	}
	
	
}

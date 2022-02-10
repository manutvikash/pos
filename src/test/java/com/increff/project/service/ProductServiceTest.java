package com.increff.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.hibernate.mapping.Map;
import org.junit.Before;
import org.junit.Test;

import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.spring.AbstractUnitTest;

public class ProductServiceTest extends AbstractUnitTest {
	@Before
	public void init() throws ApiException {
		// Inserting initial pojos
		insertPojos();
	}

	/* Testing adding of product details pojo */
	@Test()
	public void testAdd() throws ApiException {

		BrandPojo b = brands.get(0);
		ProductPojo p = getProductPojo(b);
		List<ProductPojo> product_list_before = product_service.getAll();
		product_service.add(p);
		List<ProductPojo> product_list_after = product_service.getAll();
		assertEquals(product_list_before.size() + 1, product_list_after.size());
		assertEquals(p.getBarcode(), product_service.get(p.getId()).getBarcode());
		assertEquals(p.getName(), product_service.get(p.getId()).getName());
		assertEquals(p.getMrp(), product_service.get(p.getId()).getMrp(), 0.001);
		assertEquals(p.getBrandpojo(), product_service.get(p.getId()).getBrandpojo());

	}


	


	/* Testing get by id */
	@Test()
	public void testGetById() throws ApiException {

		ProductPojo db_product_pojo = product_service.get(products.get(0).getId());
		assertEquals(products.get(0).getBarcode(), db_product_pojo.getBarcode());
		assertEquals(products.get(0).getBrandpojo(), db_product_pojo.getBrandpojo());
		assertEquals(products.get(0).getMrp(), db_product_pojo.getMrp(), 0.001);
		assertEquals(products.get(0).getName(), db_product_pojo.getName());

	}



	/* Testing get by barcode for a non-existent productdetails pojo */
	@Test()
	public void testGetByBarcodeNotExisting() throws ApiException {

		try {
			product_service.get("abcdefgh");
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "ProductDetails with given barcode does not exist, barcode: " + "abcdefgh");
		}

	}

	/* Testing get all productdetails pojos */
	@Test
	public void testGetAll() {
		List<ProductPojo> product_list = product_service.getAll();
		assertEquals(3, product_list.size());
	}



	/* Testing updation of productdetails pojo */
	@Test
	public void testUpdate() throws ApiException {

		ProductPojo p2 = getNewProductPojo(brands.get(1));
		product_service.update(products.get(0).getId(), p2);
		assertEquals(p2.getBrandpojo(), product_service.get(products.get(0).getId()).getBrandpojo());
		assertEquals(p2.getName(), product_service.get(products.get(0).getId()).getName());
		assertEquals(p2.getMrp(), product_service.get(products.get(0).getId()).getMrp(), 0.001);
	}

	/* Testing updation with invalid details. Should throw exception */
	@Test()
	public void testUpdateWrong() throws ApiException {

		ProductPojo p2 = getWrongProductPojo(brands.get(1));
		try {
			product_service.update(products.get(0).getId(), p2);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Product name must not be empty");
		}

	}

	/* Testing checkifexists */
	@Test()
	public void testCheckIfExistsId() throws ApiException {

		ProductPojo db_product_pojo = product_service.checkId(products.get(0).getId());
		assertEquals(products.get(0).getId(), db_product_pojo.getId());
		assertEquals(products.get(0).getBrandpojo(), db_product_pojo.getBrandpojo());
		assertEquals(products.get(0).getMrp(), db_product_pojo.getMrp(), 0.001);
		assertEquals(products.get(0).getName(), db_product_pojo.getName());
	}



	/* Testing checkifexists for wrong barcode. Should throw exception */
	@Test()
	public void testCheckIfExistsBarcodeWrong() throws ApiException {

		try {
			product_service.checkbarcode("abcdefgh");
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "ProductDetails with given barcode does not exist, barcode: " + "abcdefgh");
		}
	}

	/* Testing normalize */
	@Test
	public void testNormalize() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductPojo p = getProductPojo(b);

		product_service.normalize(p);
		assertEquals("milk", p.getName());

	}

	/* Testing validate */
	@Test
	public void testValidate() throws ApiException {
		ProductPojo p = getProductPojo(brands.get(0));

		product_service.check(p);
		assertTrue(!p.getName().isEmpty());
		assertTrue(p.getMrp() >= 0);

	}

	/*
	 * Testing validate for an invalid product details pojo. Should throw exception
	 */
	@Test()
	public void testValidateWrong() throws ApiException {
		ProductPojo p = getWrongProductPojo(brands.get(0));

		try {
			product_service.check(p);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Product name must not be empty");
		}

	}
	/* Testing check if exists for wrong id */
	@Test()
	public void testCheckIfExistsIdWrong() throws ApiException {

		try {
			product_service.checkId(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Product with the given Id does not exist,id: "+5);
		}

	}

	private BrandPojo getBrandPojo() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("Amul");
		p.setCategory("Dairy");
		brand_service.add(p);
		return p;
	}

	private ProductPojo getProductPojo(BrandPojo b) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBrandpojo(b);
		p.setName("Milk");
		p.setMrp(50);
		return p;
	}

	private ProductPojo getNewProductPojo(BrandPojo b) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBrandpojo(b);
		p.setName("Milk2");
		p.setMrp(70);
		return p;
	}

	private ProductPojo getWrongProductPojo(BrandPojo b) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBrandpojo(b);
		p.setName("");
		p.setMrp(-5);
		return p;
	}
}

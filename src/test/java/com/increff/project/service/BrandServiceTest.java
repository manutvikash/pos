package com.increff.project.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.increff.project.pojo.BrandPojo;
import com.increff.project.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {

	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}

	/* Testing Adding of brand */
	@Test()
	public void testAdd() throws ApiException {

		BrandPojo p = getBrandPojo();
		List<BrandPojo> brand_list_before = brand_service.getAll();
		brand_service.add(p);
		List<BrandPojo> brand_list_after = brand_service.getAll();

		// Number of brands should increase
		assertEquals(brand_list_before.size() + 1, brand_list_after.size());
		assertEquals(p.getBrand(), brand_service.get(p.getId()).getBrand());
		assertEquals(p.getCategory(), brand_service.get(p.getId()).getCategory());

	}

	/* Testing Adding of duplicates. Exception should be thrown */
	@Test()
	public void testAddDuplicate() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and category values entered already exists");
		}

	}

	/* Testing adding of invalid brand pojo. Exception should be thrown */
	@Test()
	public void testAddWrong() throws ApiException {

		BrandPojo p = getWrongBrandPojo();
		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and Category must not be empty");
		}

	}

	
	/* Testing Get for brand pojo */
	@Test()
	public void testGet() throws ApiException {

		Integer id = brands.get(0).getId();
		brand_service.get(id);
		assertEquals(brands.get(0).getBrand(), brand_service.get(id).getBrand());
		assertEquals(brands.get(0).getCategory(), brand_service.get(id).getCategory());

	}

	/* Testing Get for a non-existent pojo. Should throw exception */
	@Test()
	public void testGetNotExisting() throws ApiException {

		Integer id = 5;
		try {
			brand_service.get(id);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Given Id brand does not exist, id:"+id);
		}

	}

	/* Testing update */
	@Test()
	public void testUpdate() throws ApiException {

		BrandPojo np = getNewBrandPojo();
		Integer id = brands.get(0).getId();
		brand_service.update(np, id);
		assertEquals(np.getBrand(), brand_service.get(id).getBrand());
		assertEquals(np.getCategory(), brand_service.get(id).getCategory());

	}

	/* Testing update with invalid brand pojo. Exception should be thrown */
	@Test()
	public void testUpdateWrong() throws ApiException {

		BrandPojo np = getWrongBrandPojo();

		Integer id = brands.get(0).getId();
		try {
			brand_service.update(np, id);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and Category must not be empty");
		}

	}

	/* Testing get all brand pojos */
	@Test()
	public void testGetAll() throws ApiException {

		List<BrandPojo> get_brand_list = brand_service.getAll();
		assertEquals(3, get_brand_list.size());

	}

	/* Testing checkifexists */
	@Test()
	public void testCheckIfExists() throws ApiException {

		Integer id = brands.get(0).getId();
		BrandPojo db_brand_pojo = brand_service.checkId(id);
		assertEquals(brands.get(0).getBrand(), db_brand_pojo.getBrand());
		assertEquals(brands.get(0).getCategory(), db_brand_pojo.getCategory());

	}

	/* Testing checkifexists for a non-existent pojo */
	@Test()
	public void testCheckIfExistsNotExisting() throws ApiException {

		Integer id = 100;
		try {
			brand_service.checkId(id);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Given Id brand does not exist, id:"+id);
		}

	}

	/* Testing getting of brand id based on brand and category */
	@Test()
	public void testGetId() throws ApiException {

		BrandPojo p = brands.get(0);

		BrandPojo brand_pojo = brand_service.getByBrandAndCategory(p.getBrand(), p.getCategory());
		assertEquals(brand_pojo.getBrand(), p.getBrand());
		assertEquals(brand_pojo.getCategory(), p.getCategory());

	}



//	/* Testing normalize */
//	@Test
//	public void testNormalize() throws ApiException {
//		BrandPojo p = getBrandPojo();
//
//		brand_service.normalize(p);
//		assertEquals("parle", p.getBrand());
//		assertEquals("biscuits", p.getCategory());
//	}
//
//	/* Testing Validate */
//	@Test
//	public void testValidate() throws ApiException {
//		BrandPojo p = getBrandPojo();
//
//		brand_service.check(p);
//		assertTrue(!p.getBrand().isEmpty());
//		assertTrue(!p.getCategory().isEmpty());
//	}

	private BrandPojo getBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Parle");
		p.setCategory("Biscuits");
		return p;
	}

	private BrandPojo getNewBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Parle2");
		p.setCategory("Biscuits2");
		return p;
	}

	private BrandPojo getWrongBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("");
		return p;
	}
}

package com.increff.project.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.increff.project.dao.BrandDao;
import com.increff.project.pojo.BrandPojo;

@Service
public class BrandService {

	@Autowired
	public BrandDao brand_dao;
	
	
	/*Adding brands*/
	@Transactional
	public void add(BrandPojo brandPojo) throws ApiException{
		check(brandPojo);
		normalize(brandPojo);
		brand_dao.insert(brandPojo);
	}
	
	/*Validation*/
	private void check(BrandPojo brandPojo) throws ApiException{
		if(brandPojo.getBrand().isEmpty() || brandPojo.getCategory().isEmpty()) {
			throw new ApiException("Brand and Category must not be empty");
		}
		List<BrandPojo> list=brand_dao.selectBrandCategory(brandPojo.getBrand(), brandPojo.getCategory());
		if(!list.isEmpty()) {
			throw new ApiException("Brand and category values entered already exists");
		}
	}
	
	/*Converting to lowercase and trimming it as well*/
	private static void normalize(BrandPojo brandPojo) {
		brandPojo.setBrand(brandPojo.getBrand().toLowerCase().trim());
		brandPojo.setCategory(brandPojo.getCategory().toLowerCase().trim());
	}
	
	
	/*Getting Brand By id*/
	@Transactional
	public BrandPojo get(Integer id) throws ApiException{
		BrandPojo brandPojo= checkId(id);
		return brandPojo;
	}
	
	
	/*Getting all the brands*/
	@Transactional
	public List<BrandPojo> getAll() {
		return brand_dao.selectAll();
	}
	
	/*Getting by brand*/
	@Transactional
	public List<BrandPojo> getByBrand(String brand){
		
		return brand_dao.selectBrand(brand);
	}
	
	@Transactional
	public BrandPojo getByBrandAndCategory(String brand,String category) throws ApiException {
	    List<BrandPojo> brandPojoList=brand_dao.selectBrandCategory(brand, category);
	    if(brandPojoList.isEmpty()) {
	    	throw new ApiException("Given brand name and category does not exist brand: " + brand +" , category: "+category);
	    }
	    return brandPojoList.get(0);
	}
	
	
	
	/*Checking if brand exists or not*/
	@Transactional(rollbackOn= ApiException.class)
	public BrandPojo checkId(Integer id) throws ApiException{
		BrandPojo brandPojo=brand_dao.select(id);
		if(brandPojo==null) {
			throw new ApiException("Given Id brand does not exist, id:"+id);
		}
		return brandPojo;
	}
	
	@Transactional
	public List<BrandPojo> search(BrandPojo brandPojo){
		normalize(brandPojo);
		if(brandPojo.getBrand().isEmpty()&&brandPojo.getCategory().isEmpty()) {
			return brand_dao.selectAll();
		}
		if(brandPojo.getBrand().isEmpty()) {
			return brand_dao.selectCategory(brandPojo.getCategory());
		}
		if(brandPojo.getCategory().isEmpty()) {
			return brand_dao.selectBrand(brandPojo.getBrand());
		}
		
		return brand_dao.selectBrandCategory(brandPojo.getBrand(),brandPojo.getCategory() );
	}
	
	/*Updating the brands*/
	@Transactional(rollbackOn = ApiException.class)
	public void update(BrandPojo brandPojo, Integer id) throws ApiException {
	    check(brandPojo);
	    normalize(brandPojo);
	    BrandPojo prevBrandPojo=checkId(id);
	    prevBrandPojo.setBrand(brandPojo.getBrand());
	    prevBrandPojo.setCategory(brandPojo.getCategory());
		brand_dao.update(brandPojo);
	}
}

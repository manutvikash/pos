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
	public void add(BrandPojo p) throws ApiException{
		check(p);
		normalize(p);
		brand_dao.insert(p);
	}
	
	/*Validation*/
	protected void check(BrandPojo p) throws ApiException{
		if(p.getBrand().isEmpty() || p.getCategory().isEmpty()) {
			throw new ApiException("Brand and Category must not be empty");
		}
		List<BrandPojo> list=brand_dao.selectBrandCategory(p.getBrand(), p.getCategory());
		if(!list.isEmpty()) {
			throw new ApiException("Brand and category values entered already exists");
		}
	}
	
	/*Converting to lowercase and trimming it as well*/
	protected static void normalize(BrandPojo p) {
		p.setBrand(p.getBrand().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());
	}
	
	
	/*Getting Brand By id*/
	@Transactional
	public BrandPojo get(int id) throws ApiException{
		BrandPojo p= checkId(id);
		return p;
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
	    List<BrandPojo> l=brand_dao.selectBrandCategory(brand, category);
	    if(l.isEmpty()) {
	    	throw new ApiException("Given brand name and category does not exist brand: " + brand +" , category: "+category);
	    }
	    return l.get(0);
	}
	
	
	
	/*Checking if brand exists or not*/
	@Transactional(rollbackOn= ApiException.class)
	public BrandPojo checkId(int id) throws ApiException{
		BrandPojo b=brand_dao.select(id);
		if(b==null) {
			throw new ApiException("Given Id brand does not exist, id:"+id);
		}
		return b;
	}
	
	@Transactional
	public List<BrandPojo> search(BrandPojo p){
		normalize(p);
		if(p.getBrand().isEmpty()&&p.getCategory().isEmpty()) {
			return brand_dao.selectAll();
		}
		if(p.getBrand().isEmpty()) {
			return brand_dao.selectCategory(p.getCategory());
		}
		if(p.getCategory().isEmpty()) {
			return brand_dao.selectBrand(p.getBrand());
		}
		
		return brand_dao.selectBrandCategory(p.getBrand(),p.getCategory() );
	}
	
	/*Updating the brands*/
	@Transactional(rollbackOn = ApiException.class)
	public void update(BrandPojo b,int id) throws ApiException {
	    check(b);
	    normalize(b);
	    BrandPojo prev=checkId(id);
	    prev.setBrand(b.getBrand());
	    prev.setCategory(b.getCategory());
		brand_dao.update(b);
	}
}

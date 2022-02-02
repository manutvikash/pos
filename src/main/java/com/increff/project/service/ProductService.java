package com.increff.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.project.dao.ProductDao;
import com.increff.project.pojo.ProductPojo;

@Service
public class ProductService {

	@Autowired
	public ProductDao productDao;
	
	@Transactional
	public void add(ProductPojo p) throws ApiException{
		check(p);
		checkbarcode(p);
		normalize(p);
		productDao.insert(p);
	}
	
	@Transactional
	public ProductPojo get(int id) throws ApiException{
		ProductPojo p=checkId(id);
		return p;
	}
	
	@Transactional
	public ProductPojo get(String barcode) throws ApiException {
		ProductPojo p = checkbarcode(barcode);
		return p;
	}
	
	/* Check if product exists with given barcode */
	@Transactional(rollbackFor = ApiException.class)
	public ProductPojo checkbarcode(String barcode) throws ApiException {
		ProductPojo p = productDao.select(barcode);
		if (p == null) {
			throw new ApiException("ProductDetails with given barcode does not exist, barcode: " + barcode);
		}
		return p;
	}
	
	@Transactional(rollbackFor=ApiException.class)
	public ProductPojo checkId(int id) throws ApiException{
		ProductPojo p=productDao.select(id);
		if(p==null) {
			throw new ApiException("Product with the given Id does not exist,id: "+id);
			
		}
		return p;
	}
	
	@Transactional(rollbackFor=ApiException.class)
	public void returnProductPojoCheckBarcode(String newBarcode,String oldBarcode) throws ApiException{
		ProductPojo p=productDao.select(newBarcode);
		if(p!=null) {
			if(!oldBarcode.equals(newBarcode)) {
				throw new ApiException("New Barcode already exist: "+newBarcode);
	
			}						
		}
	}
	
	protected void checkbarcode(ProductPojo p) throws ApiException{
		ProductPojo p1=productDao.select(p.getBarcode());
		if(p1!=null) {
			throw new ApiException("Barcode already exists");
		}
	}
	protected void check(ProductPojo p) throws ApiException{
		if(p.getName().isEmpty()) {
			throw new ApiException("Product name must not be empty");
		}
		if(p.getMrp()<=0) {
			throw new ApiException("Mrp Value must be positive");
		}
		
	}
	
	protected void normalize(ProductPojo p) {
		p.setName(p.getName().toLowerCase());
	}
	
	
	@Transactional
	public List<ProductPojo> getAll(){
		return productDao.selectAll();
	}
	
	/* Mapping of product pojo with barcode as key */
	@Transactional
	public Map<String, ProductPojo> getAllProductPojosByBarcode() {
		List<ProductPojo> product_list = getAll();
		Map<String, ProductPojo> barcode_product = new HashMap<String, ProductPojo>();
		for (ProductPojo product : product_list) {
			barcode_product.put(product.getBarcode(), product);
		}
		return barcode_product;
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		check(p);
		normalize(p);
		
		ProductPojo ex = checkId(id);
		returnProductPojoCheckBarcode(p.getBarcode(),ex.getBarcode());
		ex.setBarcode(p.getBarcode());
		ex.setBrandpojo(p.getBrandpojo());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		productDao.update(ex);
	}
	
}

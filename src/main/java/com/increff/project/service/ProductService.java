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
	public void add(ProductPojo productPojo) throws ApiException{
		check(productPojo);
		checkbarcode(productPojo);
		normalize(productPojo);
		productDao.insert(productPojo);
	}
	
	@Transactional
	public ProductPojo get(Integer id) throws ApiException{
		ProductPojo  productPojo=checkId(id);
		return  productPojo;
	}
	
	@Transactional
	public ProductPojo get(String barcode) throws ApiException {
		ProductPojo  productPojo = checkbarcode(barcode);
		return  productPojo;
	}
	
	/* Check if product exists with given barcode */
	@Transactional(rollbackFor = ApiException.class)
	public ProductPojo checkbarcode(String barcode) throws ApiException {
		ProductPojo  productPojo = productDao.select(barcode);
		if ( productPojo == null) {
			throw new ApiException("ProductDetails with given barcode does not exist, barcode: " + barcode);
		}
		return  productPojo;
	}
	
	@Transactional(rollbackFor=ApiException.class)
	public ProductPojo checkId(Integer id) throws ApiException{
		ProductPojo  productPojo=productDao.select(id);
		if( productPojo==null) {
			throw new ApiException("Product with the given Id does not exist,id: "+id);
			
		}
		return  productPojo;
	}
	
	@Transactional(rollbackFor=ApiException.class)
	public void returnProductPojoCheckBarcode(String newBarcode,String oldBarcode) throws ApiException{
		ProductPojo  productPojo=productDao.select(newBarcode);
		if( productPojo!=null) {
			if(!oldBarcode.equals(newBarcode)) {
				throw new ApiException("New Barcode already exist: "+newBarcode);
	
			}						
		}
	}
	
	protected void checkbarcode(ProductPojo  productPojo) throws ApiException{
		ProductPojo newProductPojo=productDao.select( productPojo.getBarcode());
		if(newProductPojo!=null) {
			throw new ApiException("Barcode already exists");
		}
	}
	private void check(ProductPojo productPojo) throws ApiException{
		if(productPojo.getName().isEmpty()) {
			throw new ApiException("Product name must not be empty");
		}
		if(productPojo.getMrp()<=0) {
			throw new ApiException("MRP value must be positive");
		}
		
	}
	
	private void normalize(ProductPojo p) {
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
	public void update(Integer id, ProductPojo p) throws ApiException {
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
	
	
	@Transactional
	public List<ProductPojo> search(ProductPojo productPojo){
		normalize(productPojo);
		if(productPojo.getBrandpojo().getBrand().isEmpty()&&productPojo.getBrandpojo().getCategory().isEmpty()) {
			return productDao.selectAll();
		}
		/*if(p.getBrandpojo().getBrand().isEmpty()) {
			return productDao.selectCategory(p.getBrandpojo().getCategory());
		}*/
		if(productPojo.getBrandpojo().getCategory().isEmpty()) {
			return productDao.selectBrand(productPojo.getBrandpojo().getCategory());
		}
		
		return productDao.selectAll();
	}
	
	@Transactional
	public List<ProductPojo> getByBrand(String brand){
		return productDao.selectBrand(brand);
	}
	
	
}

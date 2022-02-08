package com.increff.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.model.ProductSearchForm;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.service.InventoryService;
import com.increff.project.service.ProductService;
import com.increff.project.util.ConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@ApiOperation(value="Add Products")
	@RequestMapping(path="/api/product",method=RequestMethod.POST)
	public void add(@RequestBody ProductForm userForm) throws ApiException{
		BrandPojo brand_pojo = brandService.getByBrandAndCategory(userForm.getBrand(), userForm.getCategory()); 
		ProductPojo p = ConversionUtil.convert(brand_pojo,userForm);
		productService.add(p);
		
		InventoryPojo inventory=new InventoryPojo();
		inventory.setQuantity(0);
		inventory.setProductPojo(p);
		inventoryService.add(inventory);
	}
	
	@ApiOperation(value="Get Product Details by ID")
	@RequestMapping(path="/api/product/{id}",method=RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		ProductPojo p=productService.get(id);
		return ConversionUtil.convert(p);
	}
	
	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() {
		List<ProductPojo> product_pojo_list = productService.getAll();
		return ConversionUtil.convertProductList(product_pojo_list);
	}
	
	@ApiOperation(value = "Updates a ProductDetails record")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm userform) throws ApiException {
		BrandPojo brand_pojo = brandService.getByBrandAndCategory(userform.getBrand(), userform.getCategory());
		ProductPojo p = ConversionUtil.convert(brand_pojo,userform);
		productService.update(id, p);
	}
	

	@ApiOperation(value = "Search by Brand and Category")
	@RequestMapping(path = "/api/product/search", method = RequestMethod.POST)
	public List<ProductData> search(@RequestBody ProductForm f) throws ApiException {
		//BrandPojo brand_pojo = brandService.getByBrandAndCategory(f.getBrand(), f.getCategory());
		//ProductPojo z=productService.checkbarcode(f.getBarcode());
		List<BrandPojo> brand_pojo=brandService.search(ConversionUtil.convertProductFormtoBrandPojo(f));
					
		List<ProductData> productDataList=new ArrayList<ProductData>();
		
	    for(BrandPojo p1:brand_pojo) {
	    	List<ProductPojo> productList=productService.getByBrand(p1.getBrand());
	    	for(ProductPojo p:productList) {
	    		productDataList.add(ConversionUtil.convertProductpojotoProductData(p,brandService.get(p.getBrandpojo().getId())));
	    	}
			
		}
		return productDataList;
	}

	
}

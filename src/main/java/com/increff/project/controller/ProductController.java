package com.increff.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
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
	
	@ApiOperation(value="Add Products")
	@RequestMapping(path="/api/product",method=RequestMethod.POST)
	public void add(@RequestBody ProductForm userForm) throws ApiException{
		BrandPojo brand_pojo = brandService.getByBrandAndCategory(userForm.getBrand(), userForm.getCategory()); 
		ProductPojo p = ConversionUtil.convert(brand_pojo,userForm);
		productService.add(p);
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
}

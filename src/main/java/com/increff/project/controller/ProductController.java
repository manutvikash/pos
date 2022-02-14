package com.increff.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.dto.ProductDto;
import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.model.ProductFormSearch;
import com.increff.project.service.ApiException;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	
	
	@Autowired
	private ProductDto productDto;
	
	@ApiOperation(value="Add Products")
	@RequestMapping(path="/api/product",method=RequestMethod.POST)
	public void add(@RequestBody ProductForm userForm) throws ApiException{
		productDto.add(userForm);
	}
	
	@ApiOperation(value="Get Product Details by ID")
	@RequestMapping(path="/api/product/{id}",method=RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		return productDto.get(id);
	}
	
	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {

		return productDto.getAll();
	}
	
	@ApiOperation(value = "Updates a ProductDetails record")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm userform) throws ApiException {
		productDto.update(id, userform);
	}
	


	
	@ApiOperation(value="Search by barcode")
	@RequestMapping(path="/api/product/search",method=RequestMethod.POST)
	public ProductData search(@RequestBody ProductFormSearch productForm) throws ApiException{
		return productDto.search(productForm);
	}

	
}

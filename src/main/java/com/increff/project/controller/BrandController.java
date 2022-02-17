package com.increff.project.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.dto.BrandDto;
import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;

import com.increff.project.service.ApiException;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {

	
	@Autowired
	private BrandDto brandDto;
	
	@ApiOperation(value="Add Brands")
	@RequestMapping(path="/api/brand", method=RequestMethod.POST)
	public void add(@RequestBody BrandForm brandForm) throws ApiException {
		brandDto.add(brandForm);
	}
	

	
	@ApiOperation(value = "Get Brand details by id")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable Integer id) throws ApiException {
		return brandDto.get(id);
	}


	
	@ApiOperation(value = "Gets list of Brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() throws ApiException{
		//List<BrandPojo> brand_pojo_list = brandService.getAll();
		return brandDto.getAll();
	}
	

	@ApiOperation(value = "Updates a Brand details record")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody BrandForm brandForm) throws ApiException {
		brandDto.update(id, brandForm);
	}
	
	@ApiOperation(value = "Search by Brand and Category")
	@RequestMapping(path = "/api/brand/search", method = RequestMethod.POST)
	public List<BrandData> search(@RequestBody BrandForm brandForm) throws ApiException {
		return brandDto.search(brandForm);
	}
	
}

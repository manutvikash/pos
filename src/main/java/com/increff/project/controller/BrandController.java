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
import com.increff.project.pojo.BrandPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.util.ConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@ApiOperation(value="Add Brands")
	@RequestMapping(path="/api/brand", method=RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		BrandPojo p=ConversionUtil.convert(form);
		brandService.add(p);
	}
	

	
	@ApiOperation(value = "Get Brand details by id")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		BrandPojo brand_pojo = brandService.get(id);
		return convertpojo(brand_pojo);
	}

	
	//Convert to BrandData
	private static BrandData convertpojo(BrandPojo b) {
		BrandData data=new BrandData();
		data.setBrand(b.getBrand());
		data.setCategory(b.getCategory());
		data.setId(b.getId());
		return data; 
	}
	
	@ApiOperation(value = "Gets list of Brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		List<BrandPojo> brand_pojo_list = brandService.getAll();
		return convert(brand_pojo_list);
	}
	
	//Convert list of brand pojos to list of brand data
	public static List<BrandData> convert(List<BrandPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list) {
			list2.add(convertpojo(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a Brand details record")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm f) throws ApiException {
		BrandPojo brand_pojo = ConversionUtil.convert(f);
		brandService.update(brand_pojo,id);
	}
	
	@ApiOperation(value = "Search by Brand and Category")
	@RequestMapping(path = "/api/brand/search", method = RequestMethod.POST)
	public List<BrandData> search(@RequestBody BrandForm f) throws ApiException {
		BrandPojo brand_pojo = ConversionUtil.convert(f);
		List<BrandPojo> brandList=brandService.search(brand_pojo);
		List<BrandData> brandDataList=new ArrayList<BrandData>();
		
	    for(BrandPojo p1:brandList) {
			brandDataList.add(ConversionUtil.convert(p1));
		}
		return brandDataList;
	}
	
}

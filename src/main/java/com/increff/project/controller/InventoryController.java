package com.increff.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.InventoryService;
import com.increff.project.service.ProductService;
import com.increff.project.util.ConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {

	@Autowired
	private InventoryService inventory_service;

	@Autowired
	private ProductService product_service;

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm userform) throws ApiException {
		ProductPojo product = product_service.get(userform.getBarcode());
		InventoryPojo inventory_pojo = ConversionUtil.convert(userform,product);
		inventory_service.add(inventory_pojo);
	}

	

	@ApiOperation(value = "Gets an Inventory record by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo inventory_pojo = inventory_service.get(id);
		return ConversionUtil.convert(inventory_pojo);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		return ConversionUtil.convertInventoryList(inventory_pojo_list);
	}

	@ApiOperation(value = "Updates an Inventory record")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		ProductPojo product = product_service.get(f.getBarcode());
		InventoryPojo inventory_pojo = ConversionUtil.convert(f,product);
		inventory_service.update(id, inventory_pojo);
	}

//	@ApiOperation(value = "Updates an Inventory record")
//	@RequestMapping(path = "/api/inventory/{barcode}", method = RequestMethod.PUT)
//	public void update(@PathVariable String barcode, @RequestBody InventoryForm f) throws ApiException {
//		ProductPojo product = product_service.get(f.getBarcode());
//		InventoryPojo inventory_pojo = ConversionUtil.convert(f,product);
//		inventory_service.updatebybarcode(barcode, inventory_pojo);
//	}
	@ApiOperation(value="Gets Id of the Barcode")
	@RequestMapping(path="/api/inventory/barcode",method=RequestMethod.POST)
	public List<InventoryData> getid(@RequestBody InventoryForm f) throws ApiException{
		ProductPojo p=product_service.checkbarcode(f.getBarcode());
		InventoryPojo i=ConversionUtil.convert(f, p);
		//inventory_service.updatefileData(i);
		ProductPojo p1=i.getProductPojo();
		int id=p1.getId();
		InventoryPojo i2=inventory_service.getByProductId(id);
		//inventory_service.update(id, i2);
		InventoryData arrayData= ConversionUtil.convert(i2);
		
		List<InventoryData> list=new ArrayList<InventoryData>();
		list.add(arrayData);
		return list;
	}
	

}

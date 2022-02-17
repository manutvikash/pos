package com.increff.project.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.dto.InventoryDto;
import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.service.ApiException;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {


	
	@Autowired
	private InventoryDto inventoryDto;

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
		inventoryDto.add(inventoryForm);
	}

	@ApiOperation(value = "Gets an Inventory record by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable Integer id) throws ApiException {

		return inventoryDto.get(id);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		return inventoryDto.getAll();
	}

	@ApiOperation(value = "Updates an Inventory record")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody InventoryForm inventoryForm) throws ApiException {

		inventoryDto.update(id, inventoryForm);
	}


	@ApiOperation(value="Gets Id of the Barcode")
	@RequestMapping(path="/api/inventory/barcode",method=RequestMethod.POST)
	public List<InventoryData> getid(@RequestBody InventoryForm inventoryForm) throws ApiException{
		return inventoryDto.getid(inventoryForm);
	}
	

}

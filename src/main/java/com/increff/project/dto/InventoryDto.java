package com.increff.project.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.service.InventoryService;
import com.increff.project.service.ProductService;
import com.increff.project.util.ConversionUtil;

@Component
public class InventoryDto {

	@Autowired
	private InventoryService inventory_service;

	@Autowired
	private ProductService product_service;
	
	@Autowired
	private BrandService brand_service;
	
	public void add(InventoryForm inventoryForm) throws ApiException{
		ProductPojo product = product_service.get(inventoryForm.getBarcode());
		InventoryPojo inventory_pojo = ConversionUtil.convert(inventoryForm,product);
		inventory_service.add(inventory_pojo);
	}
	
	public InventoryData get(Integer id) throws ApiException{
		InventoryPojo inventory_pojo = inventory_service.get(id);
		return ConversionUtil.convert(inventory_pojo);
	}
	
	public List<InventoryData> getAll() throws ApiException{
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		List<InventoryData> inventoryData=new ArrayList<InventoryData>();
		for(InventoryPojo p:inventory_pojo_list) {
			ProductPojo p1=product_service.get(p.getId());
			InventoryData d=ConversionUtil.convertInventoryPojotoInventoryData(p,p1,brand_service.get(p1.getBrandpojo().getId()));
			inventoryData.add(d);
		}
	    return inventoryData;
	}
	
	public List<InventoryData> getid(InventoryForm inventoryForm) throws ApiException{
		ProductPojo p=product_service.checkbarcode(inventoryForm.getBarcode());
		InventoryPojo i=ConversionUtil.convert(inventoryForm, p);
		//inventory_service.updatefileData(i);
		ProductPojo p1=i.getProductPojo();
		Integer id=p1.getId();
		InventoryPojo i2=inventory_service.getByProductId(id);
		//inventory_service.update(id, i2);
		InventoryData arrayData= ConversionUtil.convert(i2);
		
		List<InventoryData> list=new ArrayList<InventoryData>();
		list.add(arrayData);
		return list;

	}
	
	public void update(Integer id, InventoryForm inventoryForm) throws ApiException{
		ProductPojo product = product_service.get(inventoryForm.getBarcode());
		InventoryPojo inventory_pojo = ConversionUtil.convert(inventoryForm,product);
		inventory_service.update(id, inventory_pojo);
	}
}

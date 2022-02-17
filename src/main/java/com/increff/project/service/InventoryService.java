package com.increff.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.project.dao.InventoryDao;
import com.increff.project.dao.ProductDao;
import com.increff.project.pojo.InventoryPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao inventory_dao;

	@Autowired
	private ProductDao product_dao;
	/* Adding Inventory */
	@Transactional
	public void add(InventoryPojo inventoryPojo) throws ApiException {
		validate(inventoryPojo);
		checkIfBarcodePresent(inventoryPojo);
		inventory_dao.insert(inventoryPojo);
	}



	/* Fetch inventory by id */
	@Transactional
	public InventoryPojo get(Integer id) throws ApiException {
		InventoryPojo inventoryPojo = checkIfExists(id);
		return inventoryPojo;
	}

	/* Fetch inventory by product id */
	@Transactional
	public InventoryPojo getByProductId(Integer product_id) throws ApiException {
		List<InventoryPojo> inventoryPojolist = getAll();
		for (InventoryPojo inventoryPojo : inventoryPojolist) {
			if (inventoryPojo.getProductPojo().getId() == product_id) {
				return inventoryPojo;
			}
		}
		return null;
	}

	/* Fetch all inventory pojos */
	@Transactional
	public List<InventoryPojo> getAll() {
		return inventory_dao.selectAll();
	}

	/* Updation of inventory */
	@Transactional(rollbackFor = ApiException.class)
	public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
		validate(inventoryPojo);
		InventoryPojo ex = checkIfExists(id);
		ex.setQuantity(inventoryPojo.getQuantity());
		inventory_dao.update(inventoryPojo);
	}
//
//	@Transactional(rollbackFor=ApiException.class)
//	public void updatebybarcode(String barcode,InventoryPojo p) throws ApiException{
//		InventoryPojo ex=checkIfBarcodeExists(barcode);
//		ex.setQuantity(p.getQuantity());
//		
//	}
	
//	@Transactional(rollbackFor=ApiException.class)
//	public InventoryPojo checkIfBarcodeExists(String barcode) throws ApiException{
//		ProductPojo p= product_dao.select(barcode);
//		InventoryPojo i=inventory_dao.selectByProductpojo(p);
//		return i;
//	}
	/* Checking if particular inventory pojo exists */
	@Transactional(rollbackFor = ApiException.class)
	public InventoryPojo checkIfExists(Integer id) throws ApiException {
		InventoryPojo inventoryPojo = inventory_dao.select(id);
		if (inventoryPojo == null) {
			throw new ApiException("Inventory with given ID does not exist, id: " + id);
		}
		return inventoryPojo;
	}

	/* Validate */
	private void validate(InventoryPojo inventoryPojo) throws ApiException {
		if (inventoryPojo.getQuantity() < 0) {
			throw new ApiException("Inventory quantity should be positive");
		}

	}

	/* Check if inventory exists or not by barcode */
	protected void checkIfBarcodePresent(InventoryPojo inventoryPojo) throws ApiException {
		List<InventoryPojo> inventoryPojolist = inventory_dao.selectByProduct(inventoryPojo.getProductPojo());
		if (inventoryPojolist.size() > 0) {
			throw new ApiException(
					"Inventory for this product already exists.");
		}

	}

}

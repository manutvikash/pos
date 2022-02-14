package com.increff.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.project.dao.InventoryDao;
import com.increff.project.dao.ProductDao;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao inventory_dao;

	@Autowired
	private ProductDao product_dao;
	/* Adding Inventory */
	@Transactional
	public void add(InventoryPojo p) throws ApiException {
		validate(p);
		checkIfBarcodePresent(p);
		inventory_dao.insert(p);
	}



	/* Fetch inventory by id */
	@Transactional
	public InventoryPojo get(int id) throws ApiException {
		InventoryPojo p = checkIfExists(id);
		return p;
	}

	/* Fetch inventory by product id */
	@Transactional
	public InventoryPojo getByProductId(int product_id) throws ApiException {
		List<InventoryPojo> lis = getAll();
		for (InventoryPojo ip : lis) {
			if (ip.getProductPojo().getId() == product_id) {
				return ip;
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
	public void update(int id, InventoryPojo p) throws ApiException {
		validate(p);
		InventoryPojo ex = checkIfExists(id);
		ex.setQuantity(p.getQuantity());
		inventory_dao.update(p);
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
	public InventoryPojo checkIfExists(int id) throws ApiException {
		InventoryPojo p = inventory_dao.select(id);
		if (p == null) {
			throw new ApiException("Inventory with given ID does not exist, id: " + id);
		}
		return p;
	}

	/* Validate */
	protected void validate(InventoryPojo p) throws ApiException {
		if (p.getQuantity() < 0) {
			throw new ApiException("Inventory quantity should be positive");
		}

	}

	/* Check if inventory exists or not by barcode */
	protected void checkIfBarcodePresent(InventoryPojo p) throws ApiException {
		List<InventoryPojo> lis = inventory_dao.selectByProduct(p.getProductPojo());
		if (lis.size() > 0) {
			throw new ApiException(
					"Inventory for this product already exists.");
		}

	}

}

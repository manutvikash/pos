package com.increff.project.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity
public class InventoryPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private ProductPojo productPojo;
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProductPojo getProductPojo() {
		return productPojo;
	}

	public void setProductPojo(ProductPojo productPojo) {
		this.productPojo = productPojo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BrandPojo getBrandPojo() {
		return productPojo.getBrandpojo();
	}

}

package com.increff.project.pojo;

import javax.persistence.*;


@Entity
public class InventoryPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private ProductPojo productPojo;

	@Column(nullable=false)
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

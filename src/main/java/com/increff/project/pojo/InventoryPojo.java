package com.increff.project.pojo;

import javax.persistence.*;


@Entity
@Table(name="Inventory")
public class InventoryPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private ProductPojo productPojo;

	@Column(nullable=false)
	private Integer quantity;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProductPojo getProductPojo() {
		return productPojo;
	}

	public void setProductPojo(ProductPojo productPojo) {
		this.productPojo = productPojo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BrandPojo getBrandPojo() {
		return productPojo.getBrandpojo();
	}

}

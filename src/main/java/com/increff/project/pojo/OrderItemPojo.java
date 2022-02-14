package com.increff.project.pojo;

import javax.persistence.*;


@Entity
@Table(indexes= {
		@Index(name = "orderid_index", columnList = "orderId")
})
public class OrderItemPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(optional=false)
	@JoinColumn(name = "orderId", nullable = false)
	private OrderPojo orderpojo;
	@ManyToOne(optional=false)
	@JoinColumn(name = "productId", nullable = false)
	private ProductPojo productpojo;
	@Column(nullable = false)
	private int quantity;
	@Column(nullable = false)
	private double sellingPrice;
	
	private double mrp;
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public OrderPojo getOrderpojo() {
		return orderpojo;
	}
	public void setOrderpojo(OrderPojo orderpojo) {
		this.orderpojo = orderpojo;
	}
	public ProductPojo getProductpojo() {
		return productpojo;
	}
	public void setProductpojo(ProductPojo productpojo) {
		this.productpojo = productpojo;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public BrandPojo getBrandPojo() {
		return productpojo.getBrandpojo();
	}
	public double getRevenue() {
		return quantity*sellingPrice;
	}

}

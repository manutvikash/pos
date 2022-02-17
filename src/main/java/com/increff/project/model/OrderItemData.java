package com.increff.project.model;

public class OrderItemData extends OrderItemForm {

	private Integer id;
	private Integer orderId;
	private String name;
	private Double mrp;
	private Integer realQuantity;

	public Integer getRealQuantity() {
		return realQuantity;
	}

	public void setRealQuantity(Integer realQuantity) {
		this.realQuantity = realQuantity;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	// private double sellingPrice;
//	public double getSellingPrice() {
//		return sellingPrice;
//	}
//	public void setSellingPrice(double sellingPrice) {
//		this.sellingPrice = sellingPrice;
//	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

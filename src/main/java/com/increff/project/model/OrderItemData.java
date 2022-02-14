package com.increff.project.model;

public class OrderItemData extends OrderItemForm {

	private int id;
	private int orderId;
	private String name;
	private double mrp;
	private int realQuantity;

	public int getRealQuantity() {
		return realQuantity;
	}

	public void setRealQuantity(int realQuantity) {
		this.realQuantity = realQuantity;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	// private double sellingPrice;
//	public double getSellingPrice() {
//		return sellingPrice;
//	}
//	public void setSellingPrice(double sellingPrice) {
//		this.sellingPrice = sellingPrice;
//	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

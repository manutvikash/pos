package com.increff.project.model;

import javax.xml.bind.annotation.XmlElement;

public class InventoryReportData {

	public String brand;
	public String category;
	public int quantity;

	@XmlElement
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@XmlElement
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}

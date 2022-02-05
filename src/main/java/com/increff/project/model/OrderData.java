package com.increff.project.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderData extends OrderForm {
    private String productName;
    private double mrp;
    private int orderId;
    
    private String date;

    public String getDate() {
		return date;
	}

	public void setDate(String date) {
		
		this.date = date;
	}

	public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

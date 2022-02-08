package com.increff.project.model;

public class OrderData {

	private int id;
	private String datetime;
	private double amount;
	private int invoiceCreated;
	
	
	public int getInvoiceCreated() {
		return invoiceCreated;
	}
	public void setInvoiceCreated(int invoiceCreated) {
		this.invoiceCreated = invoiceCreated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}

package com.increff.project.model;

public class OrderData {

	private Integer id;
	private String datetime;
	private double amount;
	private Boolean invoiceCreated;
	
	
	public Boolean getInvoiceCreated() {
		return invoiceCreated;
	}
	public void setInvoiceCreated(Boolean invoiceCreated) {
		this.invoiceCreated = invoiceCreated;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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

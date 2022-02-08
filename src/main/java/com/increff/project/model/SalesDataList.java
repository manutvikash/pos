package com.increff.project.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sales_items")
public class SalesDataList {

	private List<SalesData> sales_list;

	@XmlElement(name="sales_item")
	public List<SalesData> getSales_list() {
		return sales_list;
	}

	public void setSales_list(List<SalesData> sales_list) {
		this.sales_list = sales_list;
	}

}

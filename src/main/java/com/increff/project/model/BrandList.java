package com.increff.project.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="brands")
public class BrandList {

	List<BrandData> brandList;
	
	@XmlElement(name="brand_item")
	public List<BrandData> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<BrandData> brandList) {
		this.brandList = brandList;
	}
	
	
}

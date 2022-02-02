package com.increff.project.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(indexes= {
		//@Index(name = "id", columnList = "id"),
		@Index(name = "barcode", columnList = "barcode",unique=true)
})
public class ProductPojo {
 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String barcode;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="brand_category",nullable=false)
	private BrandPojo brandpojo;
	private String name;
	private double mrp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public BrandPojo getBrandpojo() {
		return brandpojo;
	}
	public void setBrandpojo(BrandPojo brandpojo) {
		this.brandpojo = brandpojo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMrp() {
		return mrp;
	}
	public void setMrp(double mrp) {
		this.mrp = mrp;
	}
	
}

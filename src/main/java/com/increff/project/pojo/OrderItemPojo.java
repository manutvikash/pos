package com.increff.project.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(indexes= {@Index(columnList="id,orderId")})
public class OrderItemPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int productId;
	private int orderId;
	@Column(nullable=false)
	private int quantity;
	@Column(nullable=false)
	private double sellingPrice;
	

    public OrderItemPojo() {
    }

    public OrderItemPojo(int orderId, int productId, int quantity, double sellingPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }
    


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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
	
	
}

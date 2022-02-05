package com.increff.project.pojo;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(indexes= {@Index(columnList = "id,date")})
public class OrderPojo {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@DateTimeFormat(pattern="dd-MM-yyyy hh:mm")
	private Date date;

//	 public OrderPojo() {
//	    }
//	public OrderPojo(Date date) {
//		this.date=date;
//	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date dateobj) {
		this.date = dateobj;
	}



		
}

package com.increff.project.pojo;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderPojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private LocalDateTime datetime;

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

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}


}

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
    private Integer id;
    private LocalDateTime datetime;
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

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}


}

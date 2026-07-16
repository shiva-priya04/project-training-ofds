package com.ofds.entity;

import jakarta.persistence.Column;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "payment")
public class Payment {

	@Id
	@Column(name = "payId")
	
	private String payId;

	@Column(name = "orderId")
	private String orderId;

	@Column(name = "payMethod")
	private String payMethod;

	@Column(name = "payAmt")
	private double payAmt;

	@Column(name = "payStatus")
	private String payStatus;
	

    public Payment() {
    }

    
    public Payment(String payId, String orderId, String payMethod, double payAmt, String payStatus) {
        this.payId = payId;
        this.orderId = orderId;
        this.payMethod = payMethod;
        this.payAmt = payAmt;
        this.payStatus = payStatus;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(double payAmt) {
        this.payAmt = payAmt;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
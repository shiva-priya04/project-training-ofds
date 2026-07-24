package com.ofds.entity;

import jakarta.persistence.Column;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "payment")
public class Payment {

	@Id
	@Column(name = "payId")
	@NotBlank(message = "Payment ID is required")
	private String payId;

	@Column(name = "orderId")
	@NotBlank(message = "Order ID is required")
	private String orderId;

	@Column(name = "payMethod")
	@NotBlank(message = "Payment method is required")
	@Pattern(regexp = "UPI|CARD|WALLET|CASH", message = "Payment method must be UPI, CARD, WALLET, or CASH")
	private String payMethod;

	@Column(name = "payAmt")
	@DecimalMin(value = "1.0", message = "Payment amount must be greater than 0")
	private double payAmt;

	@Column(name = "payStatus")
	@NotBlank(message = "Payment status is required")
	@Pattern(regexp = "PENDING|SUCCESS|FAILED|REFUNDED", message = "Payment status must be PENDING, SUCCESS, FAILED, or REFUNDED")
	private String payStatus;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orderId", referencedColumnName = "orderId", insertable = false, updatable = false)
	private Orders order;
	
    public Payment() {
    }

    public Payment(String payId, String orderId, String payMethod, double payAmt, String payStatus) {
        this.payId = payId;
        this.orderId = orderId;
        this.payMethod = payMethod;
        this.payAmt = payAmt;
        this.payStatus = payStatus;
    }
 
    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
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
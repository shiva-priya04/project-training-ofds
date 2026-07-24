package com.ofds.entity;

import jakarta.persistence.Column;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.OneToMany;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "customerId")
	@NotBlank(message = "Customer ID is required")
	private String customerId;

	@Column(name = "customerName")
	@NotBlank(message = "Customer name is required")
	@Size(min = 2, max = 20, message = "Customer name must be between 2 and 20 characters")
	private String customerName;

	@Column(name = "customerEmail")
	@NotBlank(message = "Customer email is required")
	@Email(message = "Enter a valid email address")
	private String customerEmail;

	@Column(name = "customerPhoneNo")
	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain exactly 10 digits")
	private String customerPhoneNo;

	@Column(name = "customerAddress")
	@NotBlank(message = "Customer address is required")
	@Size(max = 100, message = "Address cannot exceed 100 characters")
	private String customerAddress;
	
	@JsonIgnore
	@OneToMany(mappedBy = "customer")
	private List<Orders> orders;

    public Customer() {
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
    
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }
}
package com.ofds.entity;

import java.util.List;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Orders {
	
	@Id
	@Column(name = "orderId")
	private String orderId;
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "customerId")
	private Customer customer;
	
	@Column(name = "orderStatus")
	private String orderStatus;
	
	@Column(name = "totalAmt")
	private Double totalAmt;
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "resId")
	private Restaurant restaurant;
	
	@ManyToMany
	@JoinTable(
			name = "orders_menu", 
			joinColumns = @JoinColumn(name = "orderId"), 
			inverseJoinColumns = @JoinColumn(name = "menuItemId"))
	private List<MenuItem> menus;
	
	public Orders() {
		
	}

	public Orders(String orderId, Customer customer, String orderStatus, Double totalAmt, Restaurant restaurant,
			List<MenuItem> menus) {
		this.orderId = orderId;
		this.customer = customer;
		this.orderStatus = orderStatus;
		this.totalAmt = totalAmt;
		this.restaurant = restaurant;
		this.menus = menus;
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<MenuItem> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuItem> menus) {
		this.menus = menus;
	}
}

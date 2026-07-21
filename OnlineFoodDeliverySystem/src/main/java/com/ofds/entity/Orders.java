package com.ofds.entity;

import java.util.List;

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
	
	@Column(name = "customerId")
	private String customerId;
	
	@Column(name = "orderStatus")
	private String orderStatus;
	
	@Column(name = "totalAmt")
	private Double totalAmt;
	
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

	public Orders(String orderId, String customerId, String orderStatus, Double totalAmt, Restaurant restaurant,
			List<MenuItem> menus) {
		this.orderId = orderId;
		this.customerId = customerId;
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

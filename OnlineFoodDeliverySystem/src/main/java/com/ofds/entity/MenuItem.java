package com.ofds.entity;

import java.util.List;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "menu")
public class MenuItem {
	
	@Id
	@NotBlank(message = "Item ID cannot be empty")
	@Column(name = "itemId", length = 100)
	private String itemId;
	
	@NotBlank(message = "Item name cannot be empty")
	@Size(min = 2, max = 50,
	message = "Item name must be between 2 and 50 characters")
	@Column(name = "itemName")
	private String itemName;
	
	@NotBlank(message = "Description cannot be empty")
	@Column(name = "descriptions")
	private String description;
	
	@NotNull(message = "Price cannot be null")
	@Positive(message = "Price must be greater than 0")
	@Column(name = "price")
	private Double price;
	
	//@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resId", referencedColumnName = "resId")
	private Restaurant restaurant;
	
	@ManyToMany(mappedBy = "menus")
	private List<Orders> orders;
	
	public MenuItem(){
		
	}

	public MenuItem(String itemId, String itemName, String description, Double price, Restaurant restaurant) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.description = description;
		this.price = price;
		this.restaurant = restaurant;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

}

package com.ofds.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @NotBlank(message = "Restaurant ID cannot be empty")
    @Column(name = "resId")
    private String resId;

    @NotBlank(message = "Restaurant name cannot be empty")
    @Size(min = 3, max = 50, message = "Restaurant name should be between 3 and 50 characters")
    @Column(name = "resName")
    private String resName;

    @NotBlank(message = "Must provide the address of the restaurant")
    @Column(name = "resAddress")
    private String resAddress;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10 digit number")
    @Column(name = "resPhoneNo")
    private String resPhoneNo;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Column(name = "resEmail")
    private String resEmail;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(String resId, String resName,
                      String resAddress, String resPhoneNo, String resEmail) {
        this.resId = resId;
        this.resName = resName;
        this.resAddress = resAddress;
        this.resPhoneNo = resPhoneNo;
        this.resEmail = resEmail;
    }

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResAddress() {
		return resAddress;
	}

	public void setResAddress(String resAddress) {
		this.resAddress = resAddress;
	}

	public String getResPhoneNo() {
		return resPhoneNo;
	}

	public void setResPhoneNo(String resPhoneNo) {
		this.resPhoneNo = resPhoneNo;
	}

	public String getResEmail() {
		return resEmail;
	}

	public void setResEmail(String resEmail) {
		this.resEmail = resEmail;
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
    
}
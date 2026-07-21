package com.ofds.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @Column(name = "resId")
    private String resId;

    @Column(name = "resName")
    private String resName;

    @Column(name = "resAddress")
    private String resAddress;

    @Column(name = "resPhoneNo")
    private Long resPhoneNo;

    @Column(name = "resEmail")
    private String resEmail;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(String resId, String resName,
                      String resAddress, Long resPhoneNo, String resEmail) {
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

	public long getResPhoneNo() {
		return resPhoneNo;
	}

	public void setResPhoneNo(Long resPhoneNo) {
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
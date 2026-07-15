package com.ofds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private long resPhoneNo;

    @Column(name = "resEmail")
    private String resEmail;

    public Restaurant() {
    }

    public Restaurant(String resId, String resName,
                      String resAddress, long resPhoneNo, String resEmail) {
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

	public void setResPhoneNo(long resPhoneNo) {
		this.resPhoneNo = resPhoneNo;
	}

	public String getResEmail() {
		return resEmail;
	}

	public void setResEmail(String resEmail) {
		this.resEmail = resEmail;
	}

	
    
    
}
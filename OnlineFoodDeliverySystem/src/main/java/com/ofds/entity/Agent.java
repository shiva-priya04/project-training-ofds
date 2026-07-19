package com.ofds.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agent")
public class Agent {

    @Id
    @Column(name = "agentId")
    private String agentId;

    @Column(name = "agentName")
    private String agentName;

    @Column(name = "agentPhoneNo")
    private String agentPhoneNo;
    
    @Column(name = "delId")
    private String delId;
    
    

	public Agent(String agentId, String agentName, String agentPhoneNo, String delId) {
		this.agentId = agentId;
		this.agentName = agentName;
		this.agentPhoneNo = agentPhoneNo;
		this.delId = delId;
	}

	public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentPhoneNo() {
        return agentPhoneNo;
    }

    public void setAgentPhoneNo(String agentPhoneNo) {
        this.agentPhoneNo = agentPhoneNo;
    }

	public String getDelId() {
		return delId;
	}

	public void setDelId(String delId) {
		this.delId = delId;
	}
    
    
}
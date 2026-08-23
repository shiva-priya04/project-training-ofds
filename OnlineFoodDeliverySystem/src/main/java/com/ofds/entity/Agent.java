package com.ofds.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "agent")
public class Agent {

    @Id
    @NotBlank(message = "Agent ID is required")
    @Column(name = "agentId", length = 20)
    private String agentId;

    @NotBlank(message = "Agent Name is required")
    @Column(name = "agentName")
    private String agentName;

    @NotBlank(message = "Phone Number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone Number must be 10 digits"
    )
    @Column(name = "agentPhoneNo")
    private String agentPhoneNo;

    @OneToMany(mappedBy = "agent")
    @JsonIgnore
    private List<Delivery> deliveries;

    public Agent() {

    }

    public Agent(String agentId,
                 String agentName,
                 String agentPhoneNo,
                 List<Delivery> deliveries) {

        this.agentId = agentId;
        this.agentName = agentName;
        this.agentPhoneNo = agentPhoneNo;
        this.deliveries = deliveries;
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

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
    
    
}
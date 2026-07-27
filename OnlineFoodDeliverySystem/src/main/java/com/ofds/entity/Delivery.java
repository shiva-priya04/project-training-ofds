package com.ofds.entity;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @NotBlank(message = "Delivery ID is required")
    private String delId;
    @NotBlank(message = "Delivery Status is required")
    private String delStatus;
    @NotNull(message = "Estimated Time Of Arrival is required")
    private LocalDateTime estimatedTimeOfArrival;

    @ManyToOne
    @JoinColumn(name = "agentId")
    private Agent agent;

    @OneToOne
    @JoinColumn(name = "orderId")
    private Orders order;

    public String getDelId() {
        return delId;
    }

    public void setDelId(String delId) {
        this.delId = delId;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }

    public LocalDateTime getEstimatedTimeOfArrival() {
        return estimatedTimeOfArrival;
    }

    public void setEstimatedTimeOfArrival(LocalDateTime estimatedTimeOfArrival) {
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

	public Delivery(String delId, String delStatus, LocalDateTime estimatedTimeOfArrival, Agent agent, Orders order) {
		this.delId = delId;
		this.delStatus = delStatus;
		this.estimatedTimeOfArrival = estimatedTimeOfArrival;
		this.agent = agent;
		this.order = order;
	}
	public Delivery(){
	
	}
    
}
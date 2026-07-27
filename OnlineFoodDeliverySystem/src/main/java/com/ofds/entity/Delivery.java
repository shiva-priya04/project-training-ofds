package com.ofds.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
    @Column(name = "delId")
    private String delId;

    @OneToOne
    @JoinColumn(name = "orderId")
    private Orders order;
    
    @ManyToOne
    @JoinColumn(name = "agentId")
    private Agent agent;
    
    @Column(name = "delStatus")
    private String delStatus;

    @Column(name = "estimatedTimeOfArrival")
    private LocalDateTime estimatedTimeOfArrival;
    
    

    public Delivery(String delId, Orders order, Agent agent, String delStatus,
			LocalDateTime estimatedTimeOfArrival) {
		this.delId = delId;
		this.order = order;
		this.agent = agent;
		this.delStatus = delStatus;
		this.estimatedTimeOfArrival = estimatedTimeOfArrival;
	}

	public Delivery() {
		
	}

	public String getDelId() {
        return delId;
    }

    public void setDelId(String delId) {
        this.delId = delId;
    }

    public Orders getOrder() {
		return order;
	}

	public void setOrder(Orders order) {
		this.order = order;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
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
}

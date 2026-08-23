package com.ofds.service;
import com.ofds.entity.Delivery;
import java.util.List;

public interface DeliveryService {
	Delivery saveDelivery(Delivery delivery);
	Delivery getDeliveryById(String delId);
	List<Delivery> getAllDeliveries();
	List<Delivery> getDeliveriesByAgentId(String agentId);
	Delivery updateDeliveryStatus(String delId, String delStatus);
	void deleteDelivery(String delId);
	
}

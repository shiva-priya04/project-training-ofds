package com.ofds.service;

import com.ofds.entity.Delivery;
import com.ofds.repository.DeliveryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {
	@Autowired
	private DeliveryRepository deliveryRepository;
	
	@Override
	public Delivery saveDelivery(Delivery delivery) {
		return deliveryRepository.save(delivery);
	}

	@Override
	public Delivery getDeliveryById(String delId) {
		return deliveryRepository.findById(delId).orElse(null);
				
	}
	
	@Override
	public List<Delivery> getAllDeliveries() {
		return deliveryRepository.findAll();
	}
	
	@Override
	public void deleteDelivery(String delId) {
		deliveryRepository.deleteById(delId);
	}

	@Override
	public List<Delivery> getDeliveriesByAgentId(String agentId) {

		return deliveryRepository.findByAgentAgentId(agentId);
	}

	

}

package com.ofds.service;

import com.ofds.entity.Delivery;
import com.ofds.repository.DeliveryRepository;
import com.ofds.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {
	@Autowired
	private DeliveryRepository deliveryRepository;

	@Autowired
	private OrdersService ordersService;

	private static final Set<String> VALID_STATUSES = Set.of(
			"ASSIGNED", "ACCEPTED", "DECLINED", "OUT_FOR_DELIVERY", "DELIVERED");
	
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

	@Override
	public Delivery updateDeliveryStatus(String delId, String delStatus) {
		Delivery delivery = deliveryRepository.findById(delId)
				.orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID: " + delId));

		String normalized = delStatus == null ? "" : delStatus.trim().toUpperCase().replace(' ', '_');
		if (!VALID_STATUSES.contains(normalized)) {
			throw new IllegalArgumentException("Invalid delivery status: " + delStatus);
		}

		delivery.setDelStatus(normalized);
		Delivery saved = deliveryRepository.save(delivery);

		// Cascade to the linked order so the customer's tracking page reflects
		// what the agent reports. DECLINED intentionally does not change the
		// order status - the order stays as-is until an admin reassigns it.
		if (saved.getOrder() != null) {
			String orderId = saved.getOrder().getOrderId();
			if ("OUT_FOR_DELIVERY".equals(normalized)) {
				ordersService.updateOrderStatus(orderId, "OUT_FOR_DELIVERY");
			} else if ("DELIVERED".equals(normalized)) {
				ordersService.updateOrderStatus(orderId, "DELIVERED");
			}
		}

		return saved;
	}

}

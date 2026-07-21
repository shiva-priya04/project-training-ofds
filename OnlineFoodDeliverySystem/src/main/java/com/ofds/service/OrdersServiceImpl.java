package com.ofds.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofds.entity.Orders;
import com.ofds.repository.OrdersRepository;

@Service
public class OrdersServiceImpl implements OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;

	@Override
	public Orders saveOrder(Orders order) {
		
		return ordersRepository.save(order);
	}

	@Override
	public List<Orders> getAllOrders() {
		
		return ordersRepository.findAll();
	}

	@Override
	public Optional<Orders> getOrderById(String orderId) {
		
		return ordersRepository.findById(orderId);
	}
	
	@Override
	public Orders updateOrder(Orders order) {
		
		return ordersRepository.save(order);
	}

	@Override
	public void deleteOrder(String orderId) {
		
		ordersRepository.deleteById(orderId);
		
	}

	@Override
	public List<Orders> getOrdersByCustomerId(String customerId) {
		
		return ordersRepository.findByCustomerId(customerId);
	}

	@Override
	public List<Orders> getOrdersByOrderStatus(String orderStatus) {
		
		return ordersRepository.findByOrderStatus(orderStatus);
	}

	@Override
	public List<Orders> getOrdersByRestaurantId(String resId) {
		
		return ordersRepository.findByRestaurantResId(resId);
	}

}

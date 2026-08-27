package com.ofds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofds.entity.Delivery;
import com.ofds.entity.Orders;
import com.ofds.entity.Payment;
import com.ofds.entity.Restaurant;
import com.ofds.exception.RestaurantNotFoundException;
import com.ofds.repository.DeliveryRepository;
import com.ofds.repository.OrdersRepository;
import com.ofds.repository.PaymentRepository;
import com.ofds.repository.RestaurantRepository;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private DeliveryRepository deliveryRepository;

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Override
	public Restaurant addRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	@Override
	public Restaurant getRestaurantById(String resId) {
		return restaurantRepository.findById(resId).orElseThrow(() -> new RestaurantNotFoundException(
		"Restaurant not found with ID: " + resId));
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	@Override
	public Restaurant updateRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}

	@Override
	public void deleteRestaurant(String resId) {
		Restaurant restaurant = restaurantRepository.findById(resId)
				.orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + resId));

		
		List<Orders> orders = ordersRepository.findByRestaurantResId(resId);
		for (Orders order : orders) {
			List<Delivery> deliveries = deliveryRepository.findByOrder_OrderId(order.getOrderId());
			deliveryRepository.deleteAll(deliveries);

			List<Payment> payments = paymentRepository.findByOrderId(order.getOrderId());
			paymentRepository.deleteAll(payments);
		}
		ordersRepository.deleteAll(orders);

		restaurantRepository.delete(restaurant);
	}

}

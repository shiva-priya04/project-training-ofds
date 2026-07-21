package com.ofds.service;

import java.util.List;
import java.util.Optional;

import com.ofds.entity.Orders;

public interface OrdersService {
	
	Orders saveOrder(Orders order);
	
	List<Orders> getAllOrders();
	
	Optional<Orders> getOrderById(String orderId);
	
	Orders updateOrder(Orders order);
	
	void deleteOrder(String orderId);
	
	List<Orders> getOrdersByCustomerId(String customerId);
	
	List<Orders> getOrdersByOrderStatus(String orderStatus);
	
	List<Orders> getOrdersByRestaurantId(String resId);

}

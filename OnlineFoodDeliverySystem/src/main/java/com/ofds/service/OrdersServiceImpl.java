package com.ofds.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofds.entity.Customer;
import com.ofds.entity.MenuItem;
import com.ofds.entity.Orders;
import com.ofds.repository.CustomerRepository;
import com.ofds.repository.MenuItemRepository;
import com.ofds.repository.OrdersRepository;
import com.ofds.repository.RestaurantRepository;
import com.ofds.exception.CustomerNotFoundException;
import com.ofds.exception.InvalidOrderException;
import com.ofds.exception.ItemNotFoundException;
import com.ofds.exception.RestaurantNotFoundException;

@Service
public class OrdersServiceImpl implements OrdersService {
	
	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Override
	public Orders saveOrder(Orders order) {
		
		System.out.println("Menus received: " + order.getMenus());
		System.out.println("Customer received: " + order.getCustomer());
		if(order.getMenus() == null) {
			throw new InvalidOrderException("Order must contain at least one menu item");
		}
		
		double totalAmt = 0;
		for(MenuItem item : order.getMenus()) {
			MenuItem dbItem = menuItemRepository.findById(item.getItemId()).orElseThrow(() -> new ItemNotFoundException("Menu Item not found: "));
        totalAmt += dbItem.getPrice();
		}
		
		String custId = order.getCustomer().getCustomerId();

	    Customer customer =
	            customerRepository.findById(custId)
	                    .orElseThrow(() ->
	                            new CustomerNotFoundException("Customer not found"));

	    order.setCustomer(customer);
		
		order.setTotalAmt(totalAmt);
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
	    return ordersRepository.findByCustomerCustomerId(customerId);
	}

	@Override
	public List<Orders> getOrdersByOrderStatus(String orderStatus) {
		
		return ordersRepository.findByOrderStatus(orderStatus);
	}

	@Override
	public List<Orders> getOrdersByRestaurantId(String resId) {
		
		restaurantRepository.findById(resId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with ID: " + resId));
		return ordersRepository.findByRestaurantResId(resId);
	}

}

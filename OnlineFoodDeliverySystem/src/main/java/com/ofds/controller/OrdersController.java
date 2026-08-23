package com.ofds.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ofds.entity.Orders;
import com.ofds.service.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {
	
	@Autowired
	private OrdersService ordersService;
	
	@PostMapping("/add")
	public Orders createOrder(@RequestBody Orders order) {
		return ordersService.saveOrder(order);
	}
	
	@GetMapping("/all")
	public List<Orders> getAllOrders(){
		return ordersService.getAllOrders();
	}
	
	@GetMapping("/{orderId}")
	public Optional<Orders> getOrderById(@PathVariable String orderId){
		return ordersService.getOrderById(orderId);
	}
	
	@PutMapping
	public Orders updateOrder(@RequestBody Orders order) {
		return ordersService.updateOrder(order);
	}

	@PatchMapping("/{orderId}/status")
	public Orders updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> body) {
		return ordersService.updateOrderStatus(orderId, body.get("status"));
	}
	
	@DeleteMapping("/{orderId}")
	public String deleteOrder(@PathVariable String orderId) {
		ordersService.deleteOrder(orderId);
		return "Order deleted successfully";
	}
	

    @GetMapping("/customer/{customerId}")
    public List<Orders> getOrdersByCustomerId(@PathVariable String customerId) {
        return ordersService.getOrdersByCustomerId(customerId);
    }
    

    @GetMapping("/status/{orderStatus}")
    public List<Orders> getOrdersByStatus(@PathVariable String orderStatus) {
        return ordersService.getOrdersByOrderStatus(orderStatus);
    }
    

    @GetMapping("/restaurant/{resId}")
    public List<Orders> getOrdersByRestaurantId(@PathVariable String resId) {
        return ordersService.getOrdersByRestaurantId(resId);
    }




}

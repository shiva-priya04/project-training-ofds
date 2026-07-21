package com.ofds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {
	
    List<Orders> findByCustomerId(String customerId);
	
	List<Orders> findByOrderStatus(String orderStatus);
	
	List<Orders> findByRestaurantResId(String resId);

}
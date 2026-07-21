package com.ofds.service;

import java.util.List;

import com.ofds.entity.Restaurant;

public interface RestaurantService {
	
	Restaurant addRestaurant(Restaurant restaurant);
	
	Restaurant getRestaurantById(String resId);
	
	List<Restaurant> getAllRestaurants();
	
	Restaurant updateRestaurant(Restaurant restaurant);
	
	void deleteRestaurant(String resId);

}

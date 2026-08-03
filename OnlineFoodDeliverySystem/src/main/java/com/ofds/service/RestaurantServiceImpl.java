package com.ofds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofds.entity.Restaurant;
import com.ofds.exception.RestaurantNotFoundException;
import com.ofds.repository.RestaurantRepository;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
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
	    restaurantRepository.deleteById(resId);	
	}

}

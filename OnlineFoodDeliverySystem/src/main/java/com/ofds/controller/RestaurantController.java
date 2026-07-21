package com.ofds.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ofds.entity.Restaurant;
import com.ofds.service.RestaurantService;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping("/add")
	public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
		return restaurantService.addRestaurant(restaurant);
	}
	
	@GetMapping("/{id}")
	public Restaurant getRestaurant(@PathVariable String id) {
		return restaurantService.getRestaurantById(id);
	}
	
	@GetMapping("/all")
	public List<Restaurant> getAllRestaurants(){ 
		return restaurantService.getAllRestaurants();
	}
	
	@PutMapping("/update")
	public Restaurant updateRestaurant(@RequestBody Restaurant restaurant) {
		return restaurantService.updateRestaurant(restaurant);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteRestaurant(@PathVariable String id) {
		restaurantService.deleteRestaurant(id);
		return "Restaurant Deleted Successfully";
	}

}

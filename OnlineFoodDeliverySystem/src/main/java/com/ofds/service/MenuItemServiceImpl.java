package com.ofds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofds.entity.MenuItem;
import com.ofds.entity.Restaurant;
import com.ofds.repository.MenuItemRepository;
import com.ofds.repository.RestaurantRepository;

@Service
public class MenuItemServiceImpl implements MenuItemService{
	
	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Override
	public MenuItem addMenuItem(MenuItem menuItem) {
		
		String resId = menuItem.getRestaurant().getResId();
		Restaurant restaurant = restaurantRepository.findById(resId).orElseThrow(() -> new RuntimeException("Restaurant not found"));
		menuItem.setRestaurant(restaurant);
		return menuItemRepository.save(menuItem);
	}
	
	@Override
	public MenuItem getMenuItemById(String itemId) {
		return menuItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Menu item not found"));
	}
	
	@Override
	public List<MenuItem> getAllMenuItem(){
		return menuItemRepository.findAll();
	}
	
	@Override
	public List<MenuItem> getMenuItemsByRestaurant(String resId){
		return menuItemRepository.findByRestaurantResId(resId);
	}
	
	@Override
	public MenuItem updateMenuItem(String itemId, MenuItem menuItem) {
		
		MenuItem existingItem = menuItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Menu item not found"));
	    existingItem.setItemName(menuItem.getItemName());
	    existingItem.setDescription(menuItem.getDescription());
	    existingItem.setPrice(menuItem.getPrice());
		return menuItemRepository.save(existingItem);
	}
	
	@Override
	public void deleteMenuItem(String itemId) {
		MenuItem item = menuItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Menu item not found"));
		menuItemRepository.delete(item);
	}

}

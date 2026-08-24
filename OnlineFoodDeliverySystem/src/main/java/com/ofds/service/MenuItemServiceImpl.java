package com.ofds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofds.entity.MenuItem;
import com.ofds.entity.Restaurant;
import com.ofds.exception.ItemNotFoundException;
import com.ofds.exception.MenuItemAlreadyExistException;
import com.ofds.exception.RestaurantNotFoundException;
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
		
		System.out.println("Restaurant = " + menuItem.getRestaurant());
		
		if(menuItemRepository.existsById(menuItem.getItemId())) {
		    throw new MenuItemAlreadyExistException(
		            "Menu item already exists with ID: "
		            + menuItem.getItemId());
		}
		
		if(menuItem.getRestaurant() == null || menuItem.getRestaurant().getResId() == null) {
		    throw new RestaurantNotFoundException("Restaurant ID is required to add a menu item");
		}
		
		String resId = menuItem.getRestaurant().getResId();
		Restaurant restaurant = restaurantRepository.findById(resId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
		menuItem.setRestaurant(restaurant);
		// default veg to true if not provided
		if (menuItem.getVeg() == null) {
			menuItem.setVeg(true);
		}
		return menuItemRepository.save(menuItem);
	}
	
	@Override
	public MenuItem getMenuItemById(String itemId) {
		return menuItemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Menu item not found"));
	}
	
	@Override
	public List<MenuItem> getAllMenuItem(){
		return menuItemRepository.findAll();
	}
	
	@Override
	public List<MenuItem> getMenuItemsByRestaurant(String resId){
		restaurantRepository.findById(resId).orElseThrow(() ->
		            new RestaurantNotFoundException(
		                "Restaurant not found with ID: " + resId));
		return menuItemRepository.findByRestaurantResId(resId);
	}
	
	@Override
	public MenuItem updateMenuItem(String itemId, MenuItem menuItem) {
		
		MenuItem existingItem = menuItemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Menu item not found"));
	    existingItem.setItemName(menuItem.getItemName());
	    existingItem.setDescription(menuItem.getDescription());
	    existingItem.setPrice(menuItem.getPrice());
	    // update veg if provided, otherwise keep existing value
	    if (menuItem.getVeg() != null) {
	        existingItem.setVeg(menuItem.getVeg());
	    }
		return menuItemRepository.save(existingItem);
	}
	
	@Override
	@Transactional
	public void deleteMenuItem(String itemId) {
		MenuItem item = menuItemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Menu item not found"));
		menuItemRepository.deleteOrderMenuLinksByItemId(itemId);
		menuItemRepository.delete(item);
	}

}

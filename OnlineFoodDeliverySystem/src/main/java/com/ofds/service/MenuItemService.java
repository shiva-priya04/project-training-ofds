package com.ofds.service;

import java.util.List;

import com.ofds.entity.MenuItem;

public interface MenuItemService {
	
	MenuItem addMenuItem(MenuItem menuItem);
	
	MenuItem getMenuItemById(String itemId);
	
	List<MenuItem> getAllMenuItem();
	
	List<MenuItem> getMenuItemsByRestaurant(String resId);
	
	MenuItem updateMenuItem(String itemId, MenuItem menuItem);
	
	void deleteMenuItem(String itemId);

}

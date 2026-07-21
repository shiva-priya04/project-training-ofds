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

import com.ofds.entity.MenuItem;
import com.ofds.service.MenuItemService;

@RestController
@RequestMapping("/menu")
public class MenuItemController {
	
	@Autowired
	private MenuItemService menuItemService;
	
	@PostMapping("/add")
	public MenuItem addMenuItem(@RequestBody MenuItem menuItem) {
		
		return menuItemService.addMenuItem(menuItem);
	}
	
	@GetMapping("/{id}")
	public MenuItem getMenuItemById(@PathVariable String id) {
		return menuItemService.getMenuItemById(id);
	}
	
	@GetMapping("/all")
	public List<MenuItem> getAllMenuItem(){
		return menuItemService.getAllMenuItem();
	}
	
	@GetMapping("restaurant/{resId}")
	public List<MenuItem> getMenuItemsByRestaurant(@PathVariable String resId){
		return menuItemService.getMenuItemsByRestaurant(resId);
	}
	
	@PutMapping("/update/{id}")
	public MenuItem updateMenuItem(@PathVariable String id, @RequestBody MenuItem menuItem) {
		return menuItemService.updateMenuItem(id, menuItem);
	}
	
	@DeleteMapping("/delete/{id}")
	public String deleteMenuItem(@PathVariable String id) {
		menuItemService.deleteMenuItem(id);
		return "Menu Item Deleted Syccessfully";
	}

}

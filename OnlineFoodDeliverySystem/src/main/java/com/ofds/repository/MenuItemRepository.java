package com.ofds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
	
	List<MenuItem> findByRestaurantResId(String resId);

}

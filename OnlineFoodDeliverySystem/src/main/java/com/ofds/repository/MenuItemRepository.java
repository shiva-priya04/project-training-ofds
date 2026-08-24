package com.ofds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ofds.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
	
	List<MenuItem> findByRestaurantResId(String resId);

	@Modifying
	@Query(value = "DELETE FROM orders_menu WHERE menuItemId = :itemId", nativeQuery = true)
	void deleteOrderMenuLinksByItemId(@Param("itemId") String itemId);

}

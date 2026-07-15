package com.ofds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
}
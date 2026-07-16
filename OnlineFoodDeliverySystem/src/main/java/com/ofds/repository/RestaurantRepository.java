package com.ofds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.Restaurant;

@Repository
public interface RestaurantRepository
        extends JpaRepository<Restaurant, String> {

}

package com.ofds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.Delivery;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {

}

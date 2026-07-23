package com.ofds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.Delivery;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
	
	List<Delivery> findByAgentAgentId(String agentId);
}

package com.ofds.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ofds.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByOrderId(String orderId);
}
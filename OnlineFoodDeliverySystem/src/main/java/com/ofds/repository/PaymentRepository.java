package com.ofds.repository;

//import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofds.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    //List<Payment> findByOrderId(String orderId);
}
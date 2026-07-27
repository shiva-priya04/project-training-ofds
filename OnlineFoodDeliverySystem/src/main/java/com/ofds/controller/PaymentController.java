package com.ofds.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ofds.entity.Payment;
import com.ofds.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment makePayment(@Valid @RequestBody Payment payment) {
        return paymentService.makePayment(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{payId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String payId) {
        Payment payment = paymentService.getPaymentById(payId);

        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{orderId}")
    public List<Payment> getPaymentsByOrderId(@PathVariable String orderId) {
        return paymentService.getPaymentsByOrderId(orderId);
    }

    @PutMapping("/{payId}/sync-amount")
    public ResponseEntity<Payment> syncPaymentAmount(@PathVariable String payId) {
        Payment payment = paymentService.syncPaymentAmount(payId);

        if (payment != null) {
            return ResponseEntity.ok(payment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{payId}")
    public ResponseEntity<String> deletePayment(@PathVariable String payId) {
        boolean deleted = paymentService.deletePayment(payId);

        if (deleted) {
            return ResponseEntity.ok("Payment deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
}
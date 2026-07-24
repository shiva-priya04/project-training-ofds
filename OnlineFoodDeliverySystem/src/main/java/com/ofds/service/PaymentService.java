package com.ofds.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ofds.entity.Orders;
import com.ofds.entity.Payment;
import com.ofds.repository.OrdersRepository;
import com.ofds.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;

    public PaymentService(PaymentRepository paymentRepository, OrdersRepository ordersRepository) {
        this.paymentRepository = paymentRepository;
        this.ordersRepository = ordersRepository;
    }

    public Payment makePayment(Payment payment) {

        if (paymentRepository.existsById(payment.getPayId())) {
            throw new RuntimeException("Payment ID already exists");
        }

        Optional<Orders> existingOrder = ordersRepository.findById(payment.getOrderId());

        if (existingOrder.isEmpty()) {
            throw new RuntimeException("Order ID does not exist");
        }

        Orders order = existingOrder.get();

        payment.setPayAmt(order.getTotalAmt());

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(String payId) {
        Optional<Payment> payment = paymentRepository.findById(payId);

        if (payment.isPresent()) {
            return payment.get();
        } else {
            return null;
        }
    }

    public List<Payment> getPaymentsByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Payment updatePaymentStatus(String payId, String payStatus) {
        Optional<Payment> existingPayment = paymentRepository.findById(payId);

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();
            payment.setPayStatus(payStatus);

            return paymentRepository.save(payment);
        } else {
            return null;
        }
    }
    
    public Payment syncPaymentAmount(String payId) {

        Optional<Payment> existingPayment = paymentRepository.findById(payId);

        if (existingPayment.isEmpty()) {
            throw new RuntimeException("Payment ID does not exist");
        }

        Payment payment = existingPayment.get();

        Optional<Orders> existingOrder = ordersRepository.findById(payment.getOrderId());

        if (existingOrder.isEmpty()) {
            throw new RuntimeException("Order ID does not exist");
        }

        Orders order = existingOrder.get();

        payment.setPayAmt(order.getTotalAmt());

        return paymentRepository.save(payment);
    }

    public boolean deletePayment(String payId) {
        if (paymentRepository.existsById(payId)) {
            paymentRepository.deleteById(payId);
            return true;
        } else {
            return false;
        }
    }
}
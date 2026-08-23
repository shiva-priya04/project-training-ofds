package com.ofds.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ofds.entity.Customer;
import com.ofds.entity.Delivery;
import com.ofds.entity.Orders;
import com.ofds.entity.Payment;
import com.ofds.repository.CustomerRepository;
import com.ofds.repository.DeliveryRepository;
import com.ofds.repository.OrdersRepository;
import com.ofds.repository.PaymentRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrdersRepository ordersRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;

    public CustomerService(CustomerRepository customerRepository, OrdersRepository ordersRepository,
            DeliveryRepository deliveryRepository, PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.ordersRepository = ordersRepository;
        this.deliveryRepository = deliveryRepository;
        this.paymentRepository = paymentRepository;
    }

    public Customer addCustomer(Customer customer) {

        if (customerRepository.existsById(customer.getCustomerId())) {
            throw new RuntimeException("Customer ID already exists");
        }

        return customerRepository.save(customer);
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);

        if (customer.isPresent()) {
            return customer.get();
        } else {
            return null;
        }
    }

    public Customer updateCustomer(String customerId, Customer updatedCustomer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();

            customer.setCustomerName(updatedCustomer.getCustomerName());
            customer.setCustomerEmail(updatedCustomer.getCustomerEmail());
            customer.setCustomerPhoneNo(updatedCustomer.getCustomerPhoneNo());
            customer.setCustomerAddress(updatedCustomer.getCustomerAddress());

            return customerRepository.save(customer);
        } else {
            return null;
        }
    }

    public boolean deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            return false;
        }

        // Remove the customer's order history first so the FK constraint on
        // orders.customerId does not block deletion. Deliveries and payments
        // tied to those orders are removed too since they reference the order.
        List<Orders> orders = ordersRepository.findByCustomerCustomerId(customerId);
        for (Orders order : orders) {
            List<Delivery> deliveries = deliveryRepository.findByOrder_OrderId(order.getOrderId());
            deliveryRepository.deleteAll(deliveries);

            List<Payment> payments = paymentRepository.findByOrderId(order.getOrderId());
            paymentRepository.deleteAll(payments);
        }
        ordersRepository.deleteAll(orders);

        customerRepository.deleteById(customerId);
        return true;
    }
}
package com.ofds.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ofds.entity.Customer;
import com.ofds.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            return true;
        } else {
            return false;
        }
    }
}
package com.ofds.exception;

public class OrdersNotFoundException extends RuntimeException {

    public OrdersNotFoundException(String message) {
        super(message);
    }
}
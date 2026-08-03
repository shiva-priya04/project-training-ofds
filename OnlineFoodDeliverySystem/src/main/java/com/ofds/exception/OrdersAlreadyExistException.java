package com.ofds.exception;

public class OrdersAlreadyExistException extends RuntimeException {

    public OrdersAlreadyExistException(String message) {
        super(message);
    }
}
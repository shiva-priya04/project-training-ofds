package com.ofds.exception;

public class RestaurantAlreadyExistsException extends RuntimeException{

	public RestaurantAlreadyExistsException(String message) {
		super(message);
	}

}
package com.ofds.exception;

public class MenuItemAlreadyExistException extends RuntimeException{
	public MenuItemAlreadyExistException(String message) {
		super(message);
	}

}

package com.ofds.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController{
	
	@GetMapping("/test")
	public String test() {
		System.out.println("HELL");
		return "Hello";
	}
}
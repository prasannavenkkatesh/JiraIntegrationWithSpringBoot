package com.bluewhale.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Controller {
	
	@GetMapping("/sayHello")
	public Object sayHello() {
		return "Hello There!...";
	}

}

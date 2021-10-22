package com.acme.sales.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")

public class OrdersController {

	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping
	public Iterable<Order> GetAll() {
		return orderRepo.findAll();
	}
}

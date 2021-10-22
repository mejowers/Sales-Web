package com.acme.sales.orderLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/orderLines")
public class OrderLinesController {
	
	@Autowired
	private OrderLineRepository ordLinRepo;
	
	@GetMapping
	public Iterable<OrderLine> GetAll() {
		return ordLinRepo.findAll();
	}
	

}

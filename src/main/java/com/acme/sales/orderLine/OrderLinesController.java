package com.acme.sales.orderLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.sales.order.OrderRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/orderLines")
public class OrderLinesController {
	
	@Autowired
	private OrderLineRepository ordLinRepo;
	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<OrderLine>> GetAll() {
		var orderLines = ordLinRepo.findAll();
		return new ResponseEntity<Iterable<OrderLine>>(orderLines, HttpStatus.OK);
		}
	
	@GetMapping("{id}")
	public ResponseEntity<OrderLine> GetById(@PathVariable int id) {
		var orderLine = ordLinRepo.findById(id);
		if(orderLine.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<OrderLine>(orderLine.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<OrderLine> Insert(@RequestBody OrderLine orderLine) throws Exception {
		if(orderLine == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		orderLine.setId(0);
		var newOrderLine = ordLinRepo.save(orderLine);
		RecalculateOrder(orderLine.getOrder().getId());
		return new ResponseEntity<OrderLine>(newOrderLine, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody OrderLine orderLine) throws Exception {
		if(orderLine.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldOrderLine = ordLinRepo.findById(orderLine.getId());
		if(oldOrderLine.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		ordLinRepo.save(orderLine);
		RecalculateOrder(orderLine.getOrder().getId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity Delete(@PathVariable int id) throws Exception{
		var orderLine = ordLinRepo.findById(id);
		if(orderLine.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ordLinRepo.deleteById(id);
		RecalculateOrder(orderLine.get().getOrder().getId());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	private void RecalculateOrder(int orderId) throws Exception {
		var optOrder = orderRepo.findById(orderId);
		if(optOrder.isEmpty()) {
			throw new Exception("Order id is invalid!");
		}
		var order = optOrder.get();
		var orderLines = ordLinRepo.findOrderLineByOrderId(orderId);
		var total=0;
		for(var orderLine : orderLines) {
			total += orderLine.getQuantity() * orderLine.getPrice();
		}
		order.setTotal(total);
		orderRepo.save(order);
	}
	
}
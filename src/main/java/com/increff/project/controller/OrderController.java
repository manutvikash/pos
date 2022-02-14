package com.increff.project.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.dto.OrderDto;
import com.increff.project.model.OrderData;
import com.increff.project.model.OrderItemData;
import com.increff.project.model.OrderItemForm;
import com.increff.project.service.ApiException;
import com.increff.project.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {

	@Autowired
	private OrderService order_service;


	
	@Autowired
	private OrderDto orderDto;

	@ApiOperation(value = "Adds Order Details")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public OrderData add(@RequestBody OrderItemForm[] forms, HttpServletResponse response)
			throws ApiException, Exception {
		return orderDto.add(forms, response);

	}

	@ApiOperation(value = "Adds an OrderItem to an existing order")
	@RequestMapping(path = "/api/order_item/{order_id}", method = RequestMethod.POST)
	public void addOrderItem(@PathVariable int order_id, @RequestBody OrderItemForm f) throws ApiException {
		orderDto.addOrderItem(order_id, f);
	}

	@ApiOperation(value = "Gets a OrderItem details record by id")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		return orderDto.get(id);
	}

	@ApiOperation(value = "Deletes an Order by id")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void deleteOrder(@PathVariable int id) throws ApiException {
		order_service.deleteOrder(id);
	}

	@ApiOperation(value = "Gets list of Order Items")
	@RequestMapping(path = "/api/order_item", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		return orderDto.getAll();
	}

	@ApiOperation(value = "Gets list of Orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAllOrders() {
		return orderDto.getAllOrders();
	}

	@ApiOperation(value = "Gets list of Order Items of a particular order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderItemsbyOrderId(@PathVariable int id) throws ApiException {
		return orderDto.getOrderItemsbyOrderId(id);
	}

	@ApiOperation(value = "Deletes Order Item record")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		orderDto.delete(id);
	}
	
	@ApiOperation(value = "Updates a OrderItem record")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		orderDto.update(id, f);
	}
	

	

}

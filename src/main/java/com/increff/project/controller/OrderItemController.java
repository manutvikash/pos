package com.increff.project.controller;

import com.increff.project.model.OrderData;
import com.increff.project.model.OrderForm;
import com.increff.project.service.ApiException;
import com.increff.project.dto.OrderDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// for orderItem table
@Api
@RestController
public class OrderItemController {
    @Autowired
    private OrderDto orderDto;

    @RequestMapping(path = "/api/order", method = RequestMethod.POST)
    @ApiOperation(value = "To Add Order")
    public void add(@RequestBody List<OrderForm> orderForms) throws ApiException {
        orderDto.add(orderForms);
    }

    @RequestMapping(path = "/api/order/{barcode}", method = RequestMethod.GET)
    @ApiOperation("Gets the order")
    public OrderData get(@PathVariable String barcode) throws ApiException {

        return orderDto.get(barcode);
    }

    @ApiOperation(value = "Gets list of all orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException {
        return orderDto.getAll();
    }


}

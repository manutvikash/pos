package com.increff.project.service;

import com.increff.project.dao.OrderItemDao;
import com.increff.project.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao orderItemDao;

    protected static void checkOrderQuantity(int quantity) throws ApiException {
        if (quantity <= 0)
            throw new ApiException("Order Quantity cannot be negative");
    }

    protected static void checkSellingPrice(double price) throws ApiException {
        if (price <= 0)
            throw new ApiException("Selling Price cannot be negative");
    }

    @Transactional(rollbackFor = ApiException.class)
    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        checkOrderQuantity(orderItemPojo.getQuantity());
        checkSellingPrice(orderItemPojo.getSellingPrice());
        orderItemDao.insert(orderItemPojo);
    }

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getByOrderId(int id) {
        return orderItemDao.getByOrderId(id);
    }

   
}

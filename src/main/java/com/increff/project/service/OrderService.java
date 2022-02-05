package com.increff.project.service;

import com.increff.project.dao.OrderDao;
import com.increff.project.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

//    protected static void checkNullDate(Date date) throws ApiException {
//        if (date == null) {
//            throw new ApiException("Date is null");
//        }
//    }

    @Transactional(rollbackFor = ApiException.class)
    public OrderPojo add(OrderPojo orderPojo) throws ApiException {
       // checkNullDate(orderPojo.getDate());
    	//orderPojo.setDate(LocalDateTime.now());
        return orderDao.insert(orderPojo);
    }

    @Transactional(readOnly = true)
    public OrderPojo get(int id) throws ApiException {
        return orderDao.select(id);
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    @Transactional(rollbackFor = ApiException.class)
    public void update(int id, OrderPojo orderPojo) throws ApiException {
        OrderPojo newOrderPojo = getCheck(id);
        newOrderPojo.setDate(orderPojo.getDate());
    }

    @Transactional(rollbackFor = ApiException.class, readOnly = true)
    protected OrderPojo getCheck(int id) throws ApiException {
        OrderPojo orderPojo = get(id);
        if (orderPojo == null)
            throw new ApiException("Order with given Id doesn't exist ,id : " + id);
        return orderPojo;
    }

}

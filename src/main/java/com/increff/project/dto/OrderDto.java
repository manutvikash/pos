package com.increff.project.dto;

import com.increff.project.model.OrderData;
import com.increff.project.model.OrderForm;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.InventoryService;
import com.increff.project.service.OrderItemService;
import com.increff.project.service.OrderService;
import com.increff.project.service.ProductService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderDto {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

   
    protected static OrderData addProductToOrderData(ProductPojo productPojo) {
        OrderData orderData = new OrderData();
        orderData.setMrp(productPojo.getMrp());
        orderData.setBarcode(productPojo.getBarcode());
        orderData.setProductName(productPojo.getName());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		String datetime = pojo.getDatetime().format(formatter);
        // Date dateobj = new Date();
        //orderData.setDate(null);;
        return orderData;
    }

   /* protected static String getDate() {
    	DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date dateobj = new Date();
        String datetime=df.format(dateobj);
        return datetime;
    }*/
    @Transactional(rollbackOn = ApiException.class)
    public void add(List<OrderForm> orderForms) throws ApiException {

    	Date dateobj = new Date();
       // orderForms = checkForDuplicates(orderForms);
        List<OrderItemPojo> orderItemPojos = convertToOrderitems(orderForms);// no errros

        OrderPojo orderPojo = new OrderPojo();
//        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//		String datetime = formatter.format(dateobj);
//		
        orderPojo.setDate(dateobj);
        orderService.add(orderPojo);
        
        int orderId = orderPojo.getId();
        if (orderId <= 0)
            throw new ApiException("Order with id :" + orderId + " doestn't exist");

        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            orderItemPojo.setOrderId(orderId);
            //orderItemPojo.setSellingPrice();
            orderItemService.add(orderItemPojo); // first check the conditions and then make the order
        }
        
    }

   
    protected List<OrderItemPojo> convertToOrderitems(List<OrderForm> orderForms) throws ApiException {
        List<OrderItemPojo> orderItemPojos = new ArrayList<OrderItemPojo>();
        for (OrderForm orderForm : orderForms) {
            orderItemPojos.add(convertToOrderitem(orderForm));
        }
        return orderItemPojos;
    }

    protected OrderItemPojo convertToOrderitem(OrderForm orderForm) throws ApiException {
        //orderId is not set
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        if (orderForm.getQuantity() < 0)
            orderForm.setQuantity(0);
        orderItemPojo.setQuantity(orderForm.getQuantity());

        ProductPojo productPojo = productService.get(orderForm.getBarcode());
        if (productPojo == null)
            throw new ApiException("Product doesn't exist ,barcode :" + orderForm.getBarcode());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setSellingPrice(productPojo.getMrp() * orderForm.getQuantity()); // changed to mrp*qty

        InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
        if (inventoryPojo.getQuantity() < orderForm.getQuantity())
            throw new ApiException("Out Of Stock ! Available : " + inventoryPojo.getQuantity() + " but wanted : " + orderForm.getQuantity());
        // no errors then order is made

        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderForm.getQuantity());
        inventoryService.update(productPojo.getId(), inventoryPojo);

        return orderItemPojo;
    }

    public OrderData get(String barcode) throws ApiException {
        return convert(barcode);//orderItemPOjo to orderdata
    }

    protected OrderData convert(String barcode) throws ApiException {
        ProductPojo productPojo = productService.get(barcode);
        if (productPojo == null)
            throw new ApiException("Product doesn't exist ,barcode :" + barcode);
        InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());

        OrderData orderData = addProductToOrderData(productPojo);
        //orderData.getDate((orderpojo.getOrderId()).get);
        orderData.setQuantity(inventoryPojo.getQuantity());
        return orderData;
    }

    @Transactional
    public List<OrderData> getAll() throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAll();

        List<OrderData> orderDatas = new ArrayList<OrderData>();

        for (OrderPojo orderPojo : orderPojos) {
            List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderPojo.getId());
            for (OrderItemPojo orderItemPojo : orderItemPojos) {
                ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
                OrderData orderData = addProductToOrderData(productPojo);
                orderData.setOrderId(orderPojo.getId());
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        		String datetime = formatter.format(orderPojo.getDate());
                orderData.setDate(datetime);
                //orderData.setDate(null);
                orderData.setQuantity(orderItemPojo.getQuantity());
                orderDatas.add(orderData);    // add orderId vise
            }
        }
        return orderDatas;
    }


}

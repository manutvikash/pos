package com.increff.project.dto;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.increff.project.model.OrderData;
import com.increff.project.model.OrderItemData;
import com.increff.project.model.OrderItemForm;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.OrderService;
import com.increff.project.service.ProductService;
import com.increff.project.util.ConversionUtil;

@Component
public class OrderDto {

	@Autowired
	private OrderService order_service;

	@Autowired
	private ProductService product_service;
	
	
	public OrderData add( OrderItemForm[] forms, HttpServletResponse response) throws ApiException{
		Map<String, ProductPojo> barcode_product = product_service.getAllProductPojosByBarcode();
		List<OrderItemPojo> orderitem_list = ConversionUtil.convertOrderItemForms(barcode_product, forms);
		int order_id = order_service.add(orderitem_list);
		return ConversionUtil.convertOrderPojo(order_service.getOrder(order_id));
	}
	
	public void addOrderItem(int order_id,OrderItemForm f) throws ApiException{
		ProductPojo product_pojo = product_service.get(f.getBarcode());
		OrderItemPojo orderitem_pojo = ConversionUtil.convert(product_pojo, f);
		order_service.addOrderItem(order_id, orderitem_pojo);
	}
	
	public OrderItemData get(int id) throws ApiException{
		OrderItemPojo orderitem_pojo = order_service.get(id);
		return ConversionUtil.convert(orderitem_pojo);
	}
	
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> orderitem_pojo_list = order_service.getAll();
		return ConversionUtil.convertOrderItemList(orderitem_pojo_list);
	}
	
	public List<OrderData> getAllOrders() {
		List<OrderPojo> orders_list = order_service.getAllOrders();
		return ConversionUtil.convertOrderList(orders_list);
	}
	
	public List<OrderItemData> getOrderItemsbyOrderId(@PathVariable int id) throws ApiException {
		List<OrderItemPojo> orderitem_pojo_list = order_service.getOrderItems(id);
		return ConversionUtil.convertOrderItemList(orderitem_pojo_list);
	}
	
	public void delete(@PathVariable int id) {
		order_service.delete(id);
	}
	
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		ProductPojo product_pojo = product_service.get(f.getBarcode());
		OrderItemPojo orderitem_pojo = ConversionUtil.convert(product_pojo, f);
		order_service.update(id, orderitem_pojo);
	}
}

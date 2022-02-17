package com.increff.project.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.project.dao.OrderDao;
import com.increff.project.dao.OrderItemDao;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;

@Service
public class OrderService {

	@Autowired
	private OrderDao order_dao;

	@Autowired
	private OrderItemDao order_item_dao;

	@Autowired
	private InventoryService inventory_service;

	/* Adding an order */
	@Transactional(rollbackFor = ApiException.class)
	public int add(List<OrderItemPojo> orderItemPojoList) throws ApiException {
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setDatetime(LocalDateTime.now());
		int order_id = order_dao.insert(orderPojo);
		for (OrderItemPojo orderItemPojo : orderItemPojoList) {
			if (orderItemPojo.getProductpojo() == null) {
				throw new ApiException("An invalid product was entered. Please check");
			}
			orderItemPojo.setOrderpojo(order_dao.select(order_id));
			validate(orderItemPojo);
			order_item_dao.insert(orderItemPojo);
			updateInventory(orderItemPojo, 0);
		}
		return order_id;
	}

	/* Fetching an Order item by id */
	@Transactional
	public OrderItemPojo get(Integer id) throws ApiException {
		OrderItemPojo orderItemPojo = checkIfExists(id);
		return orderItemPojo;
	}

	/* Fetching an Order by id */
	@Transactional
	public OrderPojo getOrder(Integer id) throws ApiException {
		OrderPojo orderPojo = checkIfExistsOrder(id);
		return orderPojo;
	}

	/* Fetch all order items of a particular order */
	@Transactional
	public List<OrderItemPojo> getOrderItems(Integer order_id) throws ApiException {
		
		List<OrderItemPojo> orderItemPojoList = order_item_dao.selectOrder(order_id);
		return orderItemPojoList;
	}

	/* Fetching all order items */
	@Transactional
	public List<OrderItemPojo> getAll() {
		return order_item_dao.selectAll();
	}

	/* Fetching all orders */
	@Transactional
	public List<OrderPojo> getAllOrders() {
		return order_dao.selectAll();
	}

	/* Deletion of order item */
	@Transactional
	public void delete(Integer id) {
		int order_id = order_item_dao.select(id).getOrderpojo().getId();
		order_item_dao.delete(id);
		List<OrderItemPojo> orderItemPojoList = order_item_dao.selectOrder(order_id);
		if (orderItemPojoList.isEmpty()) {
			order_dao.delete(order_id);
		}
	}

	/* Deletion of order */
	@Transactional
	public void deleteOrder(Integer order_id) throws ApiException {
		List<OrderItemPojo> orderitem_list = getOrderItems(order_id);
		for (OrderItemPojo orderitem_pojo : orderitem_list) {
			order_item_dao.delete(orderitem_pojo.getId());
		}
		order_dao.delete(order_id);
	}

	/* Update of an order item */
	@Transactional(rollbackFor = ApiException.class)
	public void update(Integer id, OrderItemPojo p) throws ApiException {

		validate1(p);
		OrderItemPojo orderItemPojo = checkIfExists(id);
		int old_qty = orderItemPojo.getQuantity();
		orderItemPojo.setQuantity(p.getQuantity());
		orderItemPojo.setProductpojo(p.getProductpojo());
		orderItemPojo.setSellingPrice(p.getSellingPrice());
		order_item_dao.update(p);
		updateInventory(p, old_qty);
	}

	/* Adding order item to an existing order */
	@Transactional(rollbackFor = ApiException.class)
	public void addOrderItem(Integer order_id, OrderItemPojo p) throws ApiException {
		List<OrderItemPojo> orderitem_list = getOrderItems(order_id);
		boolean alreadyExists = orderitem_list.stream().anyMatch(
				orderitem -> orderitem.getProductpojo().getBarcode().contentEquals(p.getProductpojo().getBarcode()));
		if (alreadyExists) {
			List<OrderItemPojo> existing_orderitem = orderitem_list.stream().filter(
					orderitem -> orderitem.getProductpojo().getBarcode().contentEquals(p.getProductpojo().getBarcode()))
					.collect(Collectors.toList());
			int old_qty = existing_orderitem.get(0).getQuantity();
			p.setQuantity(p.getQuantity() + old_qty);
			update(existing_orderitem.get(0).getId(), p);
		} else {
			p.setOrderpojo(order_dao.select(order_id));
			order_item_dao.insert(p);
			updateInventory(p, 0);
		}

	}

	/* Checking if a particular order item exists or not */
	@Transactional(rollbackFor = ApiException.class)
	public OrderItemPojo checkIfExists(Integer id) throws ApiException {
		OrderItemPojo orderItemPojo = order_item_dao.select(id);
		if (orderItemPojo == null) {
			throw new ApiException("OrderItem with given ID does not exist, id: " + id);
		}
		return orderItemPojo;
	}

	/* Checking if a particular order exists or not */
	@Transactional(rollbackFor = ApiException.class)
	public OrderPojo checkIfExistsOrder(Integer id) throws ApiException {
		OrderPojo orderPojo = order_dao.select(id);
		if (orderPojo == null) {
			throw new ApiException("Order with given ID does not exist, id: " + id);
		}
		return orderPojo;
	}
	
	@Transactional
	public void updateInvoice(Integer id) {
		OrderPojo orderPojo=order_dao.select(id);
		orderPojo.setInvoiceCreated(true);
	}
	
	@Transactional
	public List<OrderPojo> getByDate(String startDate,String endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startdateTime = LocalDate.parse(startDate, formatter).atStartOfDay();
		LocalDateTime enddateTime = LocalDate.parse(endDate, formatter).atStartOfDay().plusDays(1);
		return order_dao.selectByDate(startdateTime,enddateTime);
	}
//
//	@Transactional
//	public void updateInvoive(int id,OrderPojo p) {
//		OrderPojo newOrderPojo= order_dao.select(id);
//		//newOrderPojo.setInvoiceCreated(p.getInvoiceCreated());
//	}
	/* Updation of inventory when order is created or updated */
	protected void updateInventory(OrderItemPojo p, Integer old_qty) throws ApiException {
		Integer quantity = p.getQuantity();
		Integer quantityInInventory;
		try {
			quantityInInventory = inventory_service.getByProductId(p.getProductpojo().getId()).getQuantity() + old_qty;
		} catch (Exception e) {
			throw new ApiException("Inventory for this item does not exist " + p.getProductpojo().getBarcode());
		}

		if (quantity > quantityInInventory) {
			throw new ApiException(
					"Inventory does not contain this much quantity of product. Existing quantity in inventory: "
							+ quantityInInventory);
		}
		InventoryPojo new_ip = new InventoryPojo();
		new_ip.setQuantity(quantityInInventory - quantity);
		inventory_service.update(inventory_service.getByProductId(p.getProductpojo().getId()).getId(), new_ip);

	}

	/* Validation of order item */
	private void validate(OrderItemPojo orderItemPojo) throws ApiException {
		if (orderItemPojo.getQuantity() <= 0) {
			throw new ApiException("Quantity must be positive");
		}

	}
	private void validate1(OrderItemPojo orderItemPojo) throws ApiException {
		if (orderItemPojo.getQuantity() < 0) {
			throw new ApiException("Quantity must be positive");
		}
		if( orderItemPojo.getSellingPrice()<0) {
			throw new ApiException("Selling price must be positive");
		}

	}

}

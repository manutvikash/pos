package com.increff.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.project.dao.OrderDao;
import com.increff.project.dao.OrderItemDao;
import com.increff.project.model.OrderItemForm;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.util.ConversionUtil;

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
	public int add(List<OrderItemPojo> lis) throws ApiException {
		OrderPojo op = new OrderPojo();
		op.setDatetime(LocalDateTime.now());
		int order_id = order_dao.insert(op);
		for (OrderItemPojo p : lis) {
			if (p.getProductpojo() == null) {
				throw new ApiException("An invalid product was entered. Please check");
			}
			p.setOrderpojo(order_dao.select(order_id));
			validate(p);
			order_item_dao.insert(p);
			updateInventory(p, 0);
		}
		return order_id;
	}

	/* Fetching an Order item by id */
	@Transactional
	public OrderItemPojo get(int id) throws ApiException {
		OrderItemPojo p = checkIfExists(id);
		return p;
	}

	/* Fetching an Order by id */
	@Transactional
	public OrderPojo getOrder(int id) throws ApiException {
		OrderPojo p = checkIfExistsOrder(id);
		return p;
	}

	/* Fetch all order items of a particular order */
	@Transactional
	public List<OrderItemPojo> getOrderItems(int order_id) throws ApiException {
		
		List<OrderItemPojo> lis = order_item_dao.selectOrder(order_id);
		return lis;
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
	public void delete(int id) {
		int order_id = order_item_dao.select(id).getOrderpojo().getId();
		order_item_dao.delete(id);
		List<OrderItemPojo> lis = order_item_dao.selectOrder(order_id);
		if (lis.isEmpty()) {
			order_dao.delete(order_id);
		}
	}

	/* Deletion of order */
	@Transactional
	public void deleteOrder(int order_id) throws ApiException {
		List<OrderItemPojo> orderitem_list = getOrderItems(order_id);
		for (OrderItemPojo orderitem_pojo : orderitem_list) {
			order_item_dao.delete(orderitem_pojo.getId());
		}
		order_dao.delete(order_id);
	}

	/* Update of an order item */
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {

		validate1(p);
		OrderItemPojo ex = checkIfExists(id);
		int old_qty = ex.getQuantity();
		ex.setQuantity(p.getQuantity());
		ex.setProductpojo(p.getProductpojo());
		ex.setSellingPrice(p.getSellingPrice());
		order_item_dao.update(p);
		updateInventory(p, old_qty);
	}

	/* Adding order item to an existing order */
	@Transactional(rollbackFor = ApiException.class)
	public void addOrderItem(int order_id, OrderItemPojo p) throws ApiException {
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
	public OrderItemPojo checkIfExists(int id) throws ApiException {
		OrderItemPojo p = order_item_dao.select(id);
		if (p == null) {
			throw new ApiException("OrderItem with given ID does not exist, id: " + id);
		}
		return p;
	}

	/* Checking if a particular order exists or not */
	@Transactional(rollbackFor = ApiException.class)
	public OrderPojo checkIfExistsOrder(int id) throws ApiException {
		OrderPojo p = order_dao.select(id);
		if (p == null) {
			throw new ApiException("Order with given ID does not exist, id: " + id);
		}
		return p;
	}
	
	@Transactional
	public void updateInvoice(int id) {
		OrderPojo p=order_dao.select(id);
		p.setInvoiceCreated(true);
	}
//
//	@Transactional
//	public void updateInvoive(int id,OrderPojo p) {
//		OrderPojo newOrderPojo= order_dao.select(id);
//		//newOrderPojo.setInvoiceCreated(p.getInvoiceCreated());
//	}
	/* Updation of inventory when order is created or updated */
	protected void updateInventory(OrderItemPojo p, int old_qty) throws ApiException {
		int quantity = p.getQuantity();
		int quantityInInventory;
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
	private void validate(OrderItemPojo p) throws ApiException {
		if (p.getQuantity() <= 0) {
			throw new ApiException("Quantity must be positive");
		}

	}
	private void validate1(OrderItemPojo p) throws ApiException {
		if (p.getQuantity() < 0) {
			throw new ApiException("Quantity must be positive");
		}
		if( p.getSellingPrice()<0) {
			throw new ApiException("Selling price must be positive");
		}

	}

}

package com.increff.project.spring;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.service.InventoryService;
import com.increff.project.service.OrderService;
import com.increff.project.service.ProductService;
import com.increff.project.spring.QaConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {

	@Autowired
	protected BrandService brand_service;

	@Autowired
	protected ProductService product_service;

	@Autowired
	protected InventoryService inventory_service;
	
	@Autowired
	protected OrderService order_service;
	
	protected List<String> barcodes;
	
	protected List<BrandPojo> brands;
	protected List<ProductPojo> products;
	protected List<InventoryPojo> inventories;
	protected List<OrderPojo> orders;
	protected List<OrderItemPojo> orderitems;

	protected int order_id;
	protected void insertPojos() throws ApiException {
		barcodes = new ArrayList<String>();
		brands = new ArrayList<BrandPojo>();
		products = new ArrayList<ProductPojo>();
		inventories = new ArrayList<InventoryPojo>();
		orders = new ArrayList<OrderPojo>();
		orderitems = new ArrayList<OrderItemPojo>();
		
		for(Integer i=0; i<=2; i++) {
			BrandPojo brand = new BrandPojo();
			brand.setBrand("brand");
			brand.setCategory("category"+i);
			brand_service.add(brand);
			brands.add(brand);
			
			ProductPojo product = new ProductPojo();
			product.setBrandpojo(brand);
			product.setName("product"+i);
			product.setMrp(new Double(50));
			product.setBarcode("x"+i);
			product_service.add(product);
			products.add(product);
			barcodes.add(product.getBarcode());
			
			InventoryPojo inventory = new InventoryPojo();
			inventory.setProductPojo(product);
			inventory.setQuantity(20);
			inventory_service.add(inventory);
			inventories.add(inventory);
		}
	
		ProductPojo p = new ProductPojo();
		p=product_service.get("x2");
		ProductPojo product=new ProductPojo();
		product.setBarcode("y"+1);
		product.setBrandpojo(brands.get(0));
		product.setName("product1");
		product.setMrp(new Double(120));
		product_service.add(product);
		products.add(product);
		products.add(p);
		
		List<OrderItemPojo> order_item_list = new ArrayList<OrderItemPojo>();
		for(Integer i=0;i<2;i++) {
			OrderItemPojo orderItem=new OrderItemPojo();
			orderItem.setProductpojo(p);
			orderItem.setQuantity(2);
			orderItem.setSellingPrice(new Double(10));
			order_item_list.add(orderItem);
		}
		
		order_id = order_service.add(order_item_list);
		orders.add(order_service.getOrder(order_id));
		orderitems.addAll(order_item_list);
		
	}

}

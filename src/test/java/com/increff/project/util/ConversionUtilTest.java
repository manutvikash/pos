package com.increff.project.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.model.InvoiceDataList;
import com.increff.project.model.OrderData;
import com.increff.project.model.OrderItemData;
import com.increff.project.model.OrderItemForm;
import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.model.SalesDataList;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.spring.AbstractUnitTest;

public class ConversionUtilTest extends AbstractUnitTest{

	@Before
	public void init() throws ApiException{
		insertPojos();
	}
	
	@Test
	public void testConverttoBrandPojo() {
		BrandForm f=new BrandForm();
		f.setBrand("adidas");
		f.setCategory("shirt");
		BrandPojo brandPojo=ConversionUtil.convert(f);
		assertEquals(f.getBrand(), brandPojo.getBrand());
		assertEquals(f.getCategory(),brandPojo.getCategory());
	}
	@Test
	public void testConverttoBrandData() {
		BrandPojo f=new BrandPojo();
		f.setBrand("adidas");
		f.setCategory("shirt");
		BrandData brandData=ConversionUtil.convert(f);
		assertEquals(f.getBrand(), brandData.getBrand());
		assertEquals(f.getCategory(),brandData.getCategory());
	}
	
	@Test
	public void testConverttoBrandForm() {
		BrandPojo f=new BrandPojo();
		f.setBrand("adidas");
		f.setCategory("shirt");
		BrandForm brandForm=ConversionUtil.convertBrandPojotoBrandForm(f);
		assertEquals(f.getBrand(), brandForm.getBrand());
		assertEquals(f.getCategory(),brandForm.getCategory());
	}
	

	@SuppressWarnings("deprecation")
	@Test
	public void testConverttoProductPojo() throws ApiException{
		ProductForm f=new ProductForm();
		f.setBarcode("123");
		f.setMrp(new Double(2345));
		f.setName("Vikash");
	
		f.setBrand(brands.get(0).getBrand());
		f.setCategory(brands.get(0).getCategory());
		ProductPojo productPojo=ConversionUtil.convert(brands.get(0), f);
		assertEquals(f.getBarcode(),productPojo.getBarcode());
		assertEquals(f.getName(),productPojo.getName());
		assertEquals(f.getMrp(),productPojo.getMrp(),0.0);
		
		assertEquals(f.getBrand(), productPojo.getBrandpojo().getBrand());
		assertEquals(f.getCategory(),productPojo.getBrandpojo().getCategory());
	}
	
	@Test
	public void testConvertProductFormtoBrandPojo()throws ApiException{
		ProductForm f=new ProductForm();
		f.setBarcode("123");
		f.setBrand(brands.get(0).getBrand());
		f.setCategory(brands.get(0).getCategory());
		
		BrandPojo brandPojo=ConversionUtil.convertProductFormtoBrandPojo(f);
		assertEquals(f.getBrand(),brandPojo.getBrand());
		assertEquals(f.getCategory(),brandPojo.getCategory());
	}
	
	@Test
	public void testConvertProductFormToPojo() throws ApiException {

		ProductForm form = new ProductForm();
		form.setBrand("brand");
		form.setCategory("category0");
		form.setMrp(50.00);
		form.setName("milk");
		ProductPojo product_pojo = ConversionUtil.convert(brands.get(0), form);
		assertEquals(form.getBrand(), product_pojo.getBrandpojo().getBrand());
		assertEquals(form.getCategory(), product_pojo.getBrandpojo().getCategory());
		assertEquals(form.getName(), product_pojo.getName());
		assertEquals(form.getMrp(), product_pojo.getMrp(), 0.001);
	}
	
	/* Testing conversion of product pojo to data */
	@Test
	public void testConvertProductPojoToData() throws ApiException {

		ProductPojo product_pojo = new ProductPojo();
		product_pojo.setBarcode("abcdefgh");
		product_pojo.setBrandpojo(brands.get(0));
		product_pojo.setMrp(50.00);
		product_pojo.setName("milk");
		ProductData product_data = ConversionUtil.convert(product_pojo);
		assertEquals(product_data.getBarcode(), product_pojo.getBarcode());
		assertEquals(product_data.getBrand(), product_pojo.getBrandpojo().getBrand());
		assertEquals(product_data.getCategory(), product_pojo.getBrandpojo().getCategory());
		assertEquals(product_data.getName(), product_pojo.getName());
		assertEquals(product_data.getMrp(), product_pojo.getMrp(), 0.001);
	}

	/* Testing conversion of inventory form to pojo */
	@Test
	public void testConvertInventoryFormToPojo() throws ApiException {

		InventoryForm form = new InventoryForm();
		form.setBarcode(products.get(0).getBarcode());
		form.setQuantity(20);
		InventoryPojo inventory_pojo = ConversionUtil.convert(form, products.get(0));
		assertEquals(form.getQuantity(), inventory_pojo.getQuantity());
	}

	/* Testing conversion of inventory pojo to data */
	@Test
	public void testConvertInventoryPojoToData() throws ApiException {

		InventoryPojo pojo = new InventoryPojo();
		pojo.setProductPojo(product_service.get(products.get(0).getBarcode()));
		pojo.setQuantity(20);
		InventoryData inventory_data = ConversionUtil.convert(pojo);
		assertEquals(inventory_data.getBarcode(), pojo.getProductPojo().getBarcode());
		assertEquals(inventory_data.getQuantity(), pojo.getQuantity());
	}

	/* Testing conversion of orderitem form to pojo */
	@Test
	public void testConvertOrderItemFormToPojo() throws ApiException {

		OrderItemForm form = new OrderItemForm();
		form.setBarcode(products.get(0).getBarcode());
		form.setQuantity(2);
//		OrderItemPojo pojo = ConversionUtil.convert(products.get(0), form);
//		assertEquals(form.getBarcode(), pojo.getProductpojo().getBarcode());
//		assertEquals(form.getQuantity(), pojo.getQuantity());
	}

	/* Testing conversion of orderitem pojo to data */
	@Test
	public void testConvertOrderItemPojoToData() throws ApiException {

		OrderItemPojo pojo = new OrderItemPojo();
		pojo.setProductpojo(products.get(0));
		pojo.setOrderpojo(order_service.getOrder(order_id));
		pojo.setQuantity(2);
		pojo.setSellingPrice(products.get(0).getMrp());
		OrderItemData data = ConversionUtil.convert(pojo);
		assertEquals(data.getBarcode(), pojo.getProductpojo().getBarcode());
		assertEquals(data.getOrderId(), pojo.getOrderpojo().getId());
		assertEquals(data.getQuantity(), pojo.getQuantity());
	}

	
	
	/* Testing conversion of list of brand pojos to data */
	@Test
	public void testListBrandPojoToData() {
		List<BrandPojo> brand_list = brand_service.getAll();
		List<BrandData> brand_data_list = ConversionUtil.convert(brand_list);
		assertEquals(brand_list.size(), brand_data_list.size());
		assertEquals(brand_list.get(0).getBrand(), brand_data_list.get(0).getBrand());
		assertEquals(brand_list.get(0).getCategory(), brand_data_list.get(0).getCategory());
	}

	/* Testing conversion of list of product pojos to data */
	@Test
	public void testListProductPojoToData() {
		List<ProductPojo> product_list = product_service.getAll();
		List<ProductData> product_data_list = ConversionUtil.convertProductList(product_list);
		assertEquals(product_list.size(), product_data_list.size());
		assertEquals(product_data_list.get(0).getBarcode(), product_list.get(0).getBarcode());
		assertEquals(product_data_list.get(0).getBrand(), product_list.get(0).getBrandpojo().getBrand());
		assertEquals(product_data_list.get(0).getCategory(), product_list.get(0).getBrandpojo().getCategory());
		assertEquals(product_data_list.get(0).getName(), product_list.get(0).getName());
		assertEquals(product_data_list.get(0).getMrp(), product_list.get(0).getMrp(), 0.001);
	}

	/* Testing conversion of list of inventory pojos to data */
	@Test
	public void testListInventoryPojoToData() {
		List<InventoryPojo> inventory_list = inventory_service.getAll();
		List<InventoryData> inventory_data_list = ConversionUtil.convertInventoryList(inventory_list);
		assertEquals(inventory_data_list.size(), inventory_list.size());
		assertEquals(inventory_data_list.get(0).getBarcode(), inventory_list.get(0).getProductPojo().getBarcode());
		assertEquals(inventory_data_list.get(0).getQuantity(), inventory_list.get(0).getQuantity());
	}

	/* Testing conversion of list of order items to invoice */
	@Test
	public void testOrderItemstoInvoice() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		InvoiceDataList invoice_list = ConversionUtil.convertToInvoiceDataList(order_item_list);
		assertEquals(order_item_list.size(), invoice_list.getItem_list().size());
		assertEquals(invoice_list.getItem_list().get(0).getName(), order_item_list.get(0).getProductpojo().getName());
		assertEquals(invoice_list.getItem_list().get(0).getQuantity(), order_item_list.get(0).getQuantity());
		assertEquals(invoice_list.getItem_list().get(0).getMrp(), order_item_list.get(0).getSellingPrice(), 0.001);
	}

	/* Testing conversion of list of order item pojos to data */
	@Test
	public void testOrderItemsPojotoData() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<OrderItemData> data_list = ConversionUtil.convertOrderItemList(order_item_list);
		assertEquals(order_item_list.size(), data_list.size());
		assertEquals(data_list.get(0).getBarcode(), order_item_list.get(0).getProductpojo().getBarcode());
		assertEquals(data_list.get(0).getOrderId(), order_item_list.get(0).getOrderpojo().getId());
		assertEquals(data_list.get(0).getQuantity(), order_item_list.get(0).getQuantity());
	}

	/* Testing conversion of list of orderitem forms to pojos */
	@Test
	public void testOrderItemsFormtoPojo() throws ApiException {
		OrderItemForm[] order_item_forms = new OrderItemForm[1];
		OrderItemForm form1 = new OrderItemForm();
		form1.setBarcode(products.get(0).getBarcode());
		form1.setQuantity(2);
		order_item_forms[0] = form1;
		Map<String, ProductPojo> barcode_product = new HashMap<String, ProductPojo>();
		barcode_product.put(products.get(0).getBarcode(), products.get(0));
		barcode_product.put(products.get(1).getBarcode(), products.get(1));

//		List<OrderItemPojo> pojo_list = ConversionUtil.convertOrderItemForms(barcode_product, order_item_forms);
//		assertEquals(1, pojo_list.size());
//		assertEquals(order_item_forms[0].getBarcode(), pojo_list.get(0).getProductpojo().getBarcode());
//		assertEquals(order_item_forms[0].getQuantity(), pojo_list.get(0).getQuantity());
	}

	/* Testing conversion of order pojo to data */
	@Test
	public void testConvertOrderPojoToData() throws ApiException {
		OrderPojo pojo = order_service.getOrder(order_id);
		OrderData order_data = ConversionUtil.convertOrderPojo(pojo);
		assertEquals(pojo.getId(), order_data.getId());
	}

	/* Testing conversion of list of order pojos to data */
	@Test
	public void testListOrderPojoToData() throws ApiException {
		OrderPojo pojo = order_service.getOrder(order_id);
		List<OrderPojo> order_list = new ArrayList<OrderPojo>();
		order_list.add(pojo);
		List<OrderData> order_data_list = ConversionUtil.convertOrderList(order_list);
		assertEquals(order_list.size(), order_data_list.size());
		assertEquals(order_list.get(0).getId(), order_data_list.get(0).getId());
	}

	/* Test conversion to inventory report list */
	@Test
	public void testConvertInventoryReportList() throws ApiException {
		Map<BrandPojo, Integer> quantityPerBrandPojo = new HashMap<BrandPojo, Integer>();
		BrandPojo b1 = new BrandPojo();
		b1.setBrand("brand1");
		b1.setCategory("category1");
		BrandPojo b2 = new BrandPojo();
		b2.setBrand("brand2");
		b2.setCategory("category2");
		quantityPerBrandPojo.put(b1, 1);
		quantityPerBrandPojo.put(b2, 2);
		//InventoryReportList inv_list = ConversionUtil.convertInventoryList(quantityPerBrandPojo);
		//assertEquals(2, inv_list.getInventory_list().size());
	}

	/* Test conversion to sales list */
	@Test
	public void testConvertSalesList() {
		Map<BrandPojo, Integer> quantityPerBrandPojo = new HashMap<BrandPojo, Integer>();
		Map<BrandPojo, Double> revenuePerBrandCategory = new HashMap<BrandPojo, Double>();
		BrandPojo b1 = new BrandPojo();
		b1.setBrand("brand1");
		b1.setCategory("category1");
		BrandPojo b2 = new BrandPojo();
		b2.setBrand("brand2");
		b2.setCategory("category2");
		quantityPerBrandPojo.put(b1, 1);
		quantityPerBrandPojo.put(b2, 2);
		revenuePerBrandCategory.put(b1, 100.00);
		revenuePerBrandCategory.put(b2, 200.00);
		SalesDataList sales_list = ConversionUtil.convertSalesList(quantityPerBrandPojo, revenuePerBrandCategory);
		assertEquals(2, sales_list.getSales_list().size());
	}

}

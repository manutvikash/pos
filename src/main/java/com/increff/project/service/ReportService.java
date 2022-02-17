package com.increff.project.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.project.model.InvoiceData;
import com.increff.project.model.InvoiceDataList;
import com.increff.project.model.SalesDataList;
import com.increff.project.model.SalesFilter;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
import com.increff.project.util.ConversionUtil;
import com.increff.project.util.XmlUtil;

@Service
public class ReportService {

	@Autowired
	private BrandService brandService;

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private OrderService order_service;

	// Generating PDF
	public byte[] generatePdfResponse(String type, Object... objects) throws Exception {
		if(type.contentEquals("invoice")) {
			InvoiceDataList idl = generateInvoiceList((Integer) objects[0]);
			XmlUtil.generateXml(new File("invoice.xml"), idl, InvoiceDataList.class);
			return XmlUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
		}else{
			SalesDataList sales_data_list = generateSalesList((SalesFilter) objects[0]);
			if(sales_data_list.getSales_list().isEmpty()) {
				throw new ApiException("No sales was done in this date range for this particular brand and category pair");
			}
			XmlUtil.generateXml(new File("sales_report.xml"), sales_data_list, SalesDataList.class);
			return XmlUtil.generatePDF(new File("sales_report.xml"), new StreamSource("sales_report.xsl"));
		}
	}

	public InvoiceDataList generateInvoiceList(Integer order_id) throws Exception {
		List<OrderItemPojo> lis = order_service.getOrderItems(order_id);
		InvoiceDataList idl = ConversionUtil.convertToInvoiceDataList(lis);
		idl.setOrder_id(lis.get(0).getOrderpojo().getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrderpojo().getDatetime().format(formatter));
		double total = calculateTotal(idl);
		OrderPojo orderPojo=order_service.getOrder(order_id);
		idl.setTotal(total);
		return idl;
	}

	
	/*Generate sales list for sales report */
	public SalesDataList generateSalesList(SalesFilter sales_filter) throws Exception {

		List<OrderItemPojo> order_list = order_service.getAll();
		List<OrderItemPojo> orderItem_list=new ArrayList<OrderItemPojo>();
		List<OrderPojo> orderPojoList=order_service.getByDate(sales_filter.getStartDate(),sales_filter.getEndDate());
		for(OrderPojo order:orderPojoList) {
			Integer id=order.getId();
			List<OrderItemPojo> orderItemPojo=order_service.getOrderItems(id);
			for(OrderItemPojo orderItem:orderItemPojo) {
				orderItem_list.add(orderItem);
			}
		}
//		List<OrderItemPojo> filtered_orderitem_list = filterByDate(sales_filter, order_list);
//		Map<BrandPojo, Integer> quantityPerBrandCategory = getMapQuantity(sales_filter, filtered_orderitem_list);
//		Map<BrandPojo, Double> revenuePerBrandCategory = getMapRevenue(sales_filter, filtered_orderitem_list);
		Map<BrandPojo, Integer> quantityPerBrandCategory = getMapQuantity(sales_filter, orderItem_list);
		Map<BrandPojo, Double> revenuePerBrandCategory = getMapRevenue(sales_filter, orderItem_list);
		SalesDataList salesDataList=ConversionUtil.convertSalesList(quantityPerBrandCategory, revenuePerBrandCategory);
		salesDataList.setEndDate(sales_filter.getEndDate());
		salesDataList.setStartDate(sales_filter.getStartDate());
		return salesDataList;
	}

	/*Getting order items based on date */
	private static List<OrderItemPojo> filterByDate(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDate = LocalDate.parse(sales_filter.getStartDate(), formatter).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(sales_filter.getEndDate(), formatter).atStartOfDay().plusDays(1);
		List<OrderItemPojo> filtered_date_list = orderitem_list.stream()
				.filter(orderitem -> orderitem.getOrderpojo().getDatetime().isAfter(startDate)
						&& orderitem.getOrderpojo().getDatetime().isBefore(endDate))
				.collect(Collectors.toList());
		return filtered_date_list;
	}

	/* Getting quantity sold based on brand category */
	private static Map<BrandPojo, Integer> getMapQuantity(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandPojo, Integer> quantityPerBrandCategory = orderitem_list.stream()
				.filter(order_item -> Equals(order_item.getBrandPojo().getBrand(), sales_filter.getBrand())
						&& Equals(order_item.getBrandPojo().getCategory(), sales_filter.getCategory()))
				.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
						Collectors.summingInt(OrderItemPojo::getQuantity)));
		return quantityPerBrandCategory;
	}

	/*Getting revenue generated based on brand category */
	private static Map<BrandPojo, Double> getMapRevenue(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandPojo, Double> revenuePerBrandCategory = orderitem_list.stream()
				.filter(order_item -> Equals(order_item.getBrandPojo().getBrand(), sales_filter.getBrand())
						&& Equals(order_item.getBrandPojo().getCategory(), sales_filter.getCategory()))
				.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
						Collectors.summingDouble(OrderItemPojo::getRevenue)));;
		return revenuePerBrandCategory;
	}

	/*String equals or empty (used for filtering) */
	private static Boolean Equals(String a, String b) {
		return (a.contentEquals(b) || b.isEmpty());
	}



	/*Calculating total cost of order */
	private static double calculateTotal(InvoiceDataList idl) {
		Double total = 0.00;
		for (InvoiceData i : idl.getItem_list()) {
			total += (i.getMrp() * i.getQuantity());
		}
		return total;
	}



	
}

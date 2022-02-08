package com.increff.project.service;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandList;
import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryReportData;
import com.increff.project.model.InvoiceData;
import com.increff.project.model.InvoiceDataList;
import com.increff.project.model.SalesReportData;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
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
		InvoiceDataList idl = generateInvoiceList((Integer) objects[0]);
		XmlUtil.generateXml(new File("invoice.xml"), idl, InvoiceDataList.class);
		return XmlUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
	}

	public InvoiceDataList generateInvoiceList(int order_id) throws Exception {
		List<OrderItemPojo> lis = order_service.getOrderItems(order_id);
		InvoiceDataList idl = ConversionUtil.convertToInvoiceDataList(lis);
		idl.setOrder_id(lis.get(0).getOrderpojo().getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrderpojo().getDatetime().format(formatter));
		double total = calculateTotal(idl);
		idl.setTotal(total);
		return idl;
	}

	/*Calculating total cost of order */
	private static double calculateTotal(InvoiceDataList idl) {
		double total = 0;
		for (InvoiceData i : idl.getItem_list()) {
			total += (i.getMrp() * i.getQuantity());
		}
		return total;
	}



	
}

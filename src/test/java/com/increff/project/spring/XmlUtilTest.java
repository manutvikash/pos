package com.increff.project.spring;

import java.io.File;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.project.model.BrandList;
import com.increff.project.model.InvoiceDataList;
import com.increff.project.model.SalesDataList;
import com.increff.project.model.SalesFilter;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.ReportService;
import com.increff.project.util.XmlUtil;

public class XmlUtilTest extends AbstractUnitTest{
	@Autowired
	private ReportService report_service;
	
	@Before
	public void init() throws ApiException {
		insertPojos();
	}
	

	// Testing Xml Generation of Sales Report
	@Test
	public void testXmlSales() throws Exception {
		SalesFilter sales_filter = new SalesFilter();
		sales_filter.setBrand("");
		sales_filter.setCategory("category"+0);
		sales_filter.setStartDate("2022-02-01");
		sales_filter.setEndDate("2022-02-31");
		SalesDataList sales_data_list = report_service.generateSalesList(sales_filter);
		XmlUtil.generateXml(new File("sales_report.xml"), sales_data_list, SalesDataList.class);
	}
	
	
	// Testing Pdf Generation
	@Test
	public void testPdf() throws Exception {
		XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));
		
	}
	
	
}

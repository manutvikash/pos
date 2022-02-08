package com.increff.project.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.dto.ReportDto;
import com.increff.project.model.BrandForm;
import com.increff.project.model.SalesFilter;
import com.increff.project.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api
@RestController
public class ReportController {

	
	@Autowired
	private ReportService reportService;
	  @Autowired
	    private ReportDto reportDto;

	  @ApiOperation(value = "Gets Brand report")
	    @RequestMapping(value = "/api/brand-report",method = RequestMethod.GET)
	    public List<BrandForm> getBrandReport(){
	        return reportDto.getBrandReport();
	    }
	  @ApiOperation(value = "Gets Sales Report")
		@RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
		public void getSales(@RequestBody SalesFilter sales_filter, HttpServletResponse response) throws Exception {
			byte[] bytes = reportService.generatePdfResponse("sales", sales_filter);
			createPdfResponse(bytes, response);
		}
	  

	public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
	}
}

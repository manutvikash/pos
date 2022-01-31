package com.increff.project.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.increff.project.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api
@RestController
public class ReportController {

	
	@Autowired
	private ReportService reportService;
	

	/*@ApiOperation(value = "Gets Brand Report")
	@RequestMapping(path = "/api/report/brand", method = RequestMethod.GET)
	public void get(HttpServletResponse response) throws Exception {
		byte[] bytes = reportService.generatePdfResponse("brand");
		createPdfResponse(bytes, response);
	}*/
	
}

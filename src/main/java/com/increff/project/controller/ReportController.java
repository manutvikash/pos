package com.increff.project.controller;

/*import java.io.IOException;

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

	
/*	//@Autowired
	private ReportService reportService;
	

	@ApiOperation(value = "Brand Report")
	@RequestMapping(path = "/api/report/brand", method = RequestMethod.GET)
	public void get(HttpServletResponse response) throws Exception {
		byte[] bytes = reportService.generatePdfResponse("brand");
		createPdfResponse(bytes, response);
	}
	

	public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
	}
}*/

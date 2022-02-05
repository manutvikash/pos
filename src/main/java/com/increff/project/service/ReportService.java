package com.increff.project.service;

import java.io.File;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandList;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.util.ConversionUtil;
import com.increff.project.util.XmlUtil;

@Service
public class ReportService {

	@Autowired
	private BrandService brandService;
	
	//Generating PDF
	public byte[] generatePdfResponse(String type,Object... objects) throws Exception{
	    BrandList brandList=generateBrandList();
	    XmlUtil.generateXml(new File("brand_report.xml"), brandList, BrandList.class);
		
		
	   return XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));
	}
	
	//Getting brandList from brandData for report
	public BrandList generateBrandList() throws Exception {
		List<BrandPojo> brandPojoList=brandService.getAll();
		List<BrandData> brandDataList=ConversionUtil.convert(brandPojoList);
		BrandList brandList=new BrandList();
		brandList.setBrandList(brandDataList);
		return brandList;
	}
}

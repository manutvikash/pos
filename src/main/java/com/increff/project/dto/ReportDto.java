package com.increff.project.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.project.model.BrandForm;
import com.increff.project.model.InventoryReportData;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.service.InventoryService;
//import com.increff.project.service.OrderItemService;
import com.increff.project.service.OrderService;
import com.increff.project.service.ProductService;
import com.increff.project.service.ReportService;
import com.increff.project.util.ConversionUtil;

@Component
public class ReportDto {

	   @Autowired
	    private BrandService brandService;
	    @Autowired
	    private ProductService productService;
	    @Autowired
	    private InventoryService inventoryService;
	    @Autowired
	    private ReportService reportService;
	  //  @Autowired
	   // private OrderItemService orderItemService;
	    @Autowired
	    private OrderService orderService;


	    
	    public List<BrandForm> getBrandReport(){
	    	List<BrandPojo> list=brandService.getAll();
	    	 List<BrandForm> brandFormList = new ArrayList<BrandForm>();
		        for(BrandPojo p:list){
		            brandFormList.add(convertBrandPojotoBrandForm(p));
		        }
	    	return brandFormList;
	    }
	    public static BrandForm convertBrandPojotoBrandForm(BrandPojo p) {
			BrandForm f=new BrandForm();
			f.setBrand(p.getBrand());
			f.setCategory(p.getCategory());
			return f;
			
		}
}

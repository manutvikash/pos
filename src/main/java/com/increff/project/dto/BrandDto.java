package com.increff.project.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.util.ConversionGeneric;
import com.increff.project.util.ConversionUtil;

@Component
public class BrandDto {

	    @Autowired
	    private BrandService brandService;
	    
	    public void add(BrandForm brandForm) throws ApiException{
//	    	BrandPojo brandPojo=ConversionUtil.convert(brandForm);
	    	BrandPojo brandPojo= ConversionGeneric.convert(brandForm, BrandPojo.class);
			brandService.add(brandPojo);
	    }
	    
	    
	    public BrandData get(Integer id) throws ApiException{
	    	BrandPojo brand_pojo = brandService.get(id);
			//return ConversionUtil.convert(brand_pojo);
			return ConversionGeneric.convert(brand_pojo, BrandData.class);
	    }
	    
	    public List<BrandData> getAll() throws ApiException{
	    	List<BrandPojo> brand_pojo_list = brandService.getAll();
			return ConversionUtil.convert(brand_pojo_list);
//			return (List<BrandData>) ConversionGeneric.convert(brand_pojo_list, BrandData.class);
	    }
	    
	    public void update(Integer id, BrandForm brandForm) throws ApiException{
	    	
	    	BrandPojo brand_pojo = ConversionUtil.convert(brandForm);
			brandService.update(brand_pojo,id);
	    }
	    
	    public List<BrandData> search(BrandForm brandForm) throws ApiException{
	    	BrandPojo brand_pojo = ConversionUtil.convert(brandForm);
			List<BrandPojo> brandList=brandService.search(brand_pojo);
			List<BrandData> brandDataList=new ArrayList<BrandData>();
			
		    for(BrandPojo p1:brandList) {
				brandDataList.add(ConversionUtil.convert(p1));
			}
			return brandDataList;
	    }
}

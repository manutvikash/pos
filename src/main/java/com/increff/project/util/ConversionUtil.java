package com.increff.project.util;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;

public class ConversionUtil {

	//Convert to BrandPojo
	public static BrandPojo convert(BrandForm d) {
		BrandPojo p = new BrandPojo();
		p.setBrand(d.getBrand());
		p.setCategory(d.getCategory());
		return p;
	}

	//Convert to Brand Data
	public static BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

	//Convert to Product Pojo
	public static ProductPojo convert(BrandPojo brand_pojo, ProductForm f) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBarcode(f.getBarcode());
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		p.setBrandpojo(brand_pojo);
		return p;
	}

	//Convert to Product Data
	public static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setId(p.getId());
		d.setBrand(p.getBrandpojo().getBrand());
		d.setCategory(p.getBrandpojo().getCategory());
		d.setMrp(p.getMrp());
		d.setName(p.getName());
		d.setBarcode(p.getBarcode());
		return d;
	}

	

	//Convert list of brand pojos to list of brand data
	public static List<BrandData> convert(List<BrandPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	//Convert list of product details pojos to list of product details data
	public static List<ProductData> convertProductList(List<ProductPojo> list) {
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
}

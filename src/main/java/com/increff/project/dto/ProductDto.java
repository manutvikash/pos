package com.increff.project.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.model.ProductFormSearch;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.ProductPojo;
import com.increff.project.service.ApiException;
import com.increff.project.service.BrandService;
import com.increff.project.service.InventoryService;
import com.increff.project.service.ProductService;
import com.increff.project.util.ConversionUtil;

@Component
public class ProductDto {

	@Autowired
	private ProductService productService;
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private InventoryService inventoryService;
	
	public void add(ProductForm productForm) throws ApiException{
		BrandPojo brand_pojo = brandService.getByBrandAndCategory(productForm.getBrand(), productForm.getCategory()); 
		ProductPojo p = ConversionUtil.convert(brand_pojo,productForm);
		productService.add(p);
		
		InventoryPojo inventory=new InventoryPojo();
		inventory.setQuantity(0);
		inventory.setProductPojo(p);
		inventoryService.add(inventory);

	}
	
	
	public ProductData get(Integer id) throws ApiException{
		ProductPojo p=productService.get(id);
		return ConversionUtil.convert(p);
	}
	
	public List<ProductData> getAll() throws ApiException{
		List<ProductPojo> product_pojo_list = productService.getAll();
		return ConversionUtil.convertProductList(product_pojo_list);
	}
	
	public void update(Integer id, ProductForm productForm) throws ApiException{
		BrandPojo brand_pojo = brandService.getByBrandAndCategory(productForm.getBrand(), productForm.getCategory());
		ProductPojo p = ConversionUtil.convert(brand_pojo,productForm);
		productService.update(id, p);
	}
	
	public ProductData search(ProductFormSearch productFormSearch) throws ApiException{
		ProductPojo productPojo=productService.checkbarcode(productFormSearch.getBarcode());
		return ConversionUtil.convert(productPojo);
	}
}

package com.increff.project.util;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.increff.project.model.BrandData;
import com.increff.project.model.BrandForm;
import com.increff.project.model.InventoryData;
import com.increff.project.model.InventoryForm;
import com.increff.project.model.InvoiceData;
import com.increff.project.model.InvoiceDataList;
import com.increff.project.model.OrderData;
import com.increff.project.model.OrderItemData;
import com.increff.project.model.OrderItemForm;
import com.increff.project.model.ProductData;
import com.increff.project.model.ProductForm;
import com.increff.project.model.ProductSearchForm;
import com.increff.project.model.SalesData;
import com.increff.project.model.SalesDataList;
import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.InventoryPojo;
import com.increff.project.pojo.OrderItemPojo;
import com.increff.project.pojo.OrderPojo;
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

	public static BrandForm convertBrandPojotoBrandForm(BrandPojo p) {
		BrandForm f=new BrandForm();
		f.setBrand(p.getBrand());
		f.setCategory(p.getCategory());
		return f;
		
	}
	
	 public static InventoryPojo convertProductPojotoInventoryPojo(ProductPojo p) {
	        InventoryPojo i = new InventoryPojo();
	        i.setProductPojo(p);
	        return i;
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
//convert product form to brand pojo
    public static BrandPojo convertProductFormtoBrandPojo(ProductForm f) {
        BrandPojo b = new BrandPojo();
        b.setCategory(f.getCategory());
        b.setBrand(f.getBrand());
        return b;
    }
	//Convert to Product Data
	public static ProductData convert(ProductPojo p) {
		ProductData d = new ProductData();
		d.setId(p.getId());
		d.setBarcode(p.getBarcode());
		d.setBrand(p.getBrandpojo().getBrand());
		d.setCategory(p.getBrandpojo().getCategory());
		d.setMrp(p.getMrp());
		d.setName(p.getName());
		d.setBarcode(p.getBarcode());
		return d;
	}

	//Convert to Product Data
	public static ProductData convertProductpojotoProductData(ProductPojo p,BrandPojo b) {
		ProductData d = new ProductData();
		d.setId(p.getId());
		d.setBarcode(p.getBarcode());
		d.setBrand(b.getBrand());
		d.setCategory(b.getCategory());
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
	
	//Convert to Inventory Pojo
	public static InventoryPojo convert(InventoryForm f, ProductPojo product_pojo) throws ApiException {
		InventoryPojo p = new InventoryPojo();
		p.setProductPojo(product_pojo);
		p.setQuantity(f.getQuantity());
		return p;
	}

	//Convert to Inventory Data
		public static InventoryData convert(InventoryPojo p) {
			InventoryData d = new InventoryData();
			d.setId(p.getId());
			d.setBarcode(p.getProductPojo().getBarcode());
			d.setName(p.getProductPojo().getName());
			d.setQuantity(p.getQuantity());
			d.setBrand(p.getBrandPojo().getBrand());
			d.setCategory(p.getBrandPojo().getCategory());
			return d;
		}
		
		public static InventoryData convertInventoryPojotoInventoryData(InventoryPojo i, ProductPojo p, BrandPojo b) {

	        InventoryData d = new InventoryData();
	        d.setId(i.getId());
	        d.setName(p.getName());
	        d.setBrand(b.getBrand());
	        d.setCategory(b.getCategory());
	        d.setBarcode(p.getBarcode());
	        d.setQuantity(i.getQuantity());
	        return d;
	    }
		
		//Convert list of inventory pojos to list of inventory data
		public static List<InventoryData> convertInventoryList(List<InventoryPojo> list) {
			List<InventoryData> list2 = new ArrayList<InventoryData>();
			for (InventoryPojo p : list) {
				list2.add(convert(p));
			}
			return list2;
		}
		
		
		//Convert list of order item pojos to list of order item data
		public static List<OrderItemData> convertOrderItemList(List<OrderItemPojo> list) {
			List<OrderItemData> list2 = new ArrayList<OrderItemData>();
			for (OrderItemPojo p : list) {
				list2.add(convert(p));
			}
			return list2;
		}

		//Convert Order Item Forms to Pojo
		public static List<OrderItemPojo> convertOrderItemForms(Map<String,ProductPojo> barcode_product,
				OrderItemForm[] forms) throws ApiException {
			List<OrderItemPojo> list2 = new ArrayList<OrderItemPojo>();
			for (OrderItemForm f : forms) {
				list2.add(convert(barcode_product.get(f.getBarcode()), f));
			}
			return list2;
		}

		//Convert Order Pojo to Order Data
		public static OrderData convertOrderPojo(OrderPojo pojo) {
			OrderData d = new OrderData();
			d.setId(pojo.getId());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			String datetime = pojo.getDatetime().format(formatter);
			d.setDatetime(datetime);
			return d;
		}
		
		//Convert List of Order Pojos to Data
		public static List<OrderData> convertOrderList(List<OrderPojo> list) {
			List<OrderData> list2 = new ArrayList<OrderData>();
			for (OrderPojo p : list) {
				list2.add(convertOrderPojo(p));
			}
			return list2;
		}
		
		//Convert to OrderItem Pojo
		public static OrderItemPojo convert(ProductPojo product_pojo, OrderItemForm f) throws ApiException {
			OrderItemPojo p = new OrderItemPojo();
			p.setProductpojo(product_pojo);
			p.setQuantity(f.getQuantity());
			p.setSellingPrice(f.getSellingPrice());
			if(product_pojo != null) {
				p.setSellingPrice(product_pojo.getMrp());
			}
			
			return p;
		}

		//Convert to OrderItem data
		public static OrderItemData convert(OrderItemPojo p) {
			OrderItemData d = new OrderItemData();
			d.setId(p.getId());
			d.setBarcode(p.getProductpojo().getBarcode());
			d.setQuantity(p.getQuantity());
			d.setOrderId(p.getOrderpojo().getId());
			d.setName(p.getProductpojo().getName());
			d.setMrp(p.getSellingPrice());
			return d;
		}
		
		public static InvoiceDataList convertToInvoiceDataList(List<OrderItemPojo> lis) {
			List<InvoiceData> invoiceLis = new ArrayList<InvoiceData>();
			for (OrderItemPojo p : lis) {
				InvoiceData i = new InvoiceData();
				i.setId(p.getId());
				i.setMrp(p.getProductpojo().getMrp());
				//i.setMrp(p.getSellingPrice());
				i.setName(p.getProductpojo().getName());
				i.setQuantity(p.getQuantity());
				invoiceLis.add(i);
			}
			InvoiceDataList idl = new InvoiceDataList();
			idl.setItem_list(invoiceLis);
			return idl;
		}

		
		//Convert Maps of quantity sold and revenue per BrandPojo to sales list
		public static SalesDataList convertSalesList(Map<BrandPojo, Integer> quantityPerBrandCategory,
				Map<BrandPojo, Double> revenuePerBrandCategory) {
			
			List<SalesData> sales_list = new ArrayList<SalesData>();
			for(BrandPojo brand: quantityPerBrandCategory.keySet()) {
				SalesData sales = new SalesData();
				sales.setBrand(brand.getBrand());
				sales.setCategory(brand.getCategory());
				sales.setQuantity(quantityPerBrandCategory.get(brand));
				sales.setRevenue(revenuePerBrandCategory.get(brand));
				sales_list.add(sales);
			}
			SalesDataList sales_data_list = new SalesDataList();
			sales_data_list.setSales_list(sales_list);
			return sales_data_list;

		}

}

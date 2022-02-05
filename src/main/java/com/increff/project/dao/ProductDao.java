package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.BrandPojo;
import com.increff.project.pojo.ProductPojo;



@Repository
public class ProductDao extends AbstractDao {
  
	@PersistenceContext
	EntityManager em;
	
	private String select_all="select p from ProductPojo p";
	private String select_bar="select p from ProductPojo p where barcode=:barcode";
	private String select_brandpojo="select p from ProductPojo p where p.brandPojo=:brandPojo";
	
	public void insert(ProductPojo p) {
		em.persist(p);
	}
	
	public ProductPojo select(int id) {
		return em.find(ProductPojo.class, id);
	}
	
	public ProductPojo select(String barcode) {
		TypedQuery<ProductPojo> query=getQuery(select_bar,ProductPojo.class);
		query.setParameter("barcode", barcode);
		List<ProductPojo> list=query.getResultList();
		if(list.size()>0) {
			ProductPojo p=list.get(0);
			return p;
		}
		else {
			return null;
		}
	}
	
	//Selecting all products from DB
		public List<ProductPojo> selectAll() {
			TypedQuery<ProductPojo> query = getQuery(select_all,ProductPojo.class);
			return query.getResultList();	
		}
		
		//Update Product Details
		public void update(ProductPojo p) {
			
		}
		
		public List<ProductPojo> selectBrand(String brand) {
			TypedQuery<ProductPojo> query=getQuery(select_brandpojo,ProductPojo.class);
		    query.setParameter("brand", brand);
			return query.getResultList();
		}
	
}

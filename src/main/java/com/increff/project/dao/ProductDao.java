package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.ProductPojo;



@Repository
public class ProductDao extends AbstractDao {
  
	@PersistenceContext
	EntityManager em;
	
	private String SELECT_ALL="select p from ProductPojo p";
	private String SELECT_BARCODE="select p from ProductPojo p where barcode=:barcode";
	private String SELECT_BRANDPOJO="select p from ProductPojo p where p.brandPojo=:brandPojo";
	
	public void insert(ProductPojo p) {
		em.persist(p);
	}
	
	public ProductPojo select(Integer id) {
		return em.find(ProductPojo.class, id);
	}
	
	public ProductPojo select(String barcode) {
		TypedQuery<ProductPojo> query=getQuery(SELECT_BARCODE,ProductPojo.class);
		query.setParameter("barcode", barcode);
		List<ProductPojo> list=query.getResultList();
		if(list.size()>0) {
			ProductPojo productPojo=list.get(0);
			return productPojo;
		}
		else {
			return null;
		}
	}
	
	//Selecting all products from DB
		public List<ProductPojo> selectAll() {
			TypedQuery<ProductPojo> query = getQuery(SELECT_ALL,ProductPojo.class);
			return query.getResultList();	
		}
		
		//Update Product Details
		public void update(ProductPojo productPojo) {
			
		}
		
		public List<ProductPojo> selectBrand(String brand) {
			TypedQuery<ProductPojo> query=getQuery(SELECT_BRANDPOJO,ProductPojo.class);
		    query.setParameter("brand", brand);
			return query.getResultList();
		}
	
}

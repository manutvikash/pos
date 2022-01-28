package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.increff.project.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String select_all="Select p from BrandPojo p";
	private static String select_brand="Select p from BrandPojo p where brand=:brand";
	private static String select_id="Select p from BrandPojo p where id=:id";
	private static String select_brand_category="Select p from BrandPojo p where p.brand= :brand and p.category= :category";
	
	@PersistenceContext
	EntityManager em;
	
	//insert brand
	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}
	
	//Select By Id
	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
       //return em.find(BrandPojo.class, id);
	}
	
	//Select All
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query=getQuery(select_all,BrandPojo.class);
		return query.getResultList();
	}
	
	
	//Select By Brand
	public List<BrandPojo> selectBrand(String brand) {
		TypedQuery<BrandPojo> query=getQuery(select_brand,BrandPojo.class);
	    query.setParameter("brand", brand);
		return query.getResultList();
	}

	//Update Brand
	public void update(BrandPojo p) {
		
	}
	
	public List<BrandPojo> selectBrandCategory(String brand, String category) {
		TypedQuery<BrandPojo> query=getQuery(select_brand_category,BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		
		return query.getResultList();
		
	}
}

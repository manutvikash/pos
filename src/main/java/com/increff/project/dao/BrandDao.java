package com.increff.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.project.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String SELECT_ALL = "Select p from BrandPojo p";
	private static String SELECT_BRAND = "Select p from BrandPojo p where brand=:brand";
	private static String SELECT_ID = "Select p from BrandPojo p where id=:id";
	private static String SELECT_BRAND_CATEGORY = "Select p from BrandPojo p where p.brand= :brand and p.category= :category";
	private static String SELECT_CATEGORY = "Select p from BrandPojo p where p.category=:category";

	@PersistenceContext
	EntityManager em;

	// Select By Id
	public BrandPojo select(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
		query.setParameter("id", id);
		return (BrandPojo) getSingle(query);
		// return em.find(BrandPojo.class, id);
	}

	// Select All
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}

	// Select By Brand
	public List<BrandPojo> selectBrand(String brand) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND, BrandPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}

	// Select by Category
	public List<BrandPojo> selectCategory(String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_CATEGORY, BrandPojo.class);
		query.setParameter("category", category);
		return query.getResultList();

	}

	public List<BrandPojo> selectBrandCategory(String brand, String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND_CATEGORY, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);

		return query.getResultList();

	}
}

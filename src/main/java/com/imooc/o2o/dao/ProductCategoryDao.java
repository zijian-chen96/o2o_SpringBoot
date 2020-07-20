package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * search  shop category by shopId
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategory(long shopId);
	
	/**
	 * add multiple product categories
	 * 
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	/**
	 * delete product category by productCategoryId and shopId
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}

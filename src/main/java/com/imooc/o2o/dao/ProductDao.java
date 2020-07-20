package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Product;

public interface ProductDao {
	
	/**
	 * total number of searched products
	 * 
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * show product by page, search product by use: productName, 
	 * productStatus, shopId, and productCategory 
	 * 
	 * @param productCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * get product info from db by use productId
	 * 
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);

	/**
	 * insert new product
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);

	/**
	 * update product info
	 * 
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
	
	/**
	 * before delete the product needs to set the productCategoryId to null
	 * 
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
}

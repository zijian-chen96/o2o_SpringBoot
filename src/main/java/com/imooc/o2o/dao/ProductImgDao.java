package com.imooc.o2o.dao;

import java.util.List;

import com.imooc.o2o.entity.ProductImg;

public interface ProductImgDao {
	
	/**
	 * 
	 * 
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);
	
	/**
	 * add multiple product images
	 * 
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * delete the product all detail image by use productId
	 * 
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);

}

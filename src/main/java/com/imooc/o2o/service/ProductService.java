package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.exceptions.ProductOperationException;

public interface ProductService {
	
	/**
	 * search product by split page, search product by use: productName, 
	 * productStatus, shopId, and productCategory
	 * 
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);
	
	/**
	 * add product to db
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException;

	/**
	 * search product info by productId
	 * 
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);

	/**
	 * modify product info and images 
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgHolderList
	 * @return
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException;
}

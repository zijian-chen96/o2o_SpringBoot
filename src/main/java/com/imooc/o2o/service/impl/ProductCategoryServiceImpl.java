package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategory(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		if(productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
				
				if(effectedNum <= 0) {
					throw new ProductCategoryOperationException("failed to create product category");
				} else {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
				}
			} catch(Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error:" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}

	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO to set the productCategoryId to null/empty the products
		
		// unbind the relation of productCategoryId with tb_product's product
		try {
			int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
			
			if(effectedNum < 0) {
				throw new ProductCategoryOperationException("failed to update product category");
			}
		} catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.toString());
		}
		
		// to delete the productCategory from db
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			
			if(effectedNum <= 0) {
				throw new ProductCategoryOperationException("failed to delete product category");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}
		
	}

}

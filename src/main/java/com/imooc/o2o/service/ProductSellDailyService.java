package com.imooc.o2o.service;

import java.util.Date;
import java.util.List;

import com.imooc.o2o.entity.ProductSellDaily;

public interface ProductSellDailyService {
	
	/**
	 * a trigger every will calculate out the sells of products under the shop
	 */
	void dailyCalculate();
	
	/**
	 * return product daily sell count list by input search condition
	 * 
	 * @param productSellDailyCondition
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime);
	
}

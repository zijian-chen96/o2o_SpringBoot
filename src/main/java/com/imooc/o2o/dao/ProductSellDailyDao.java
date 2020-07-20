package com.imooc.o2o.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ProductSellDaily;

public interface ProductSellDailyDao {
	
	/**
	 * return total of sells of the day by input search condition
	 * 
	 * @param productSellDailyCondition
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<ProductSellDaily> queryProductSellDailyList(
			@Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * count the products daily sell
	 * 
	 * @return
	 */
	int insertProductSellDaily();
	
	/**
	 * if there is no sells on that day, then needs to set the sell to 0
	 * 
	 * @return
	 */
	int insertDefaultProductSellDaily();

}

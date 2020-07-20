package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * search shop list, search elements: shop name, shop status, shop category,
	 * areaId, owner
	 * 
	 * @param shopCondition
	 * @param rowIndex from the # of rows to search the data
	 * @param pageSize return the # of size
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * return total number of queryShopList 
	 * 
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);

	/**
	 * search shop info by using shop_id
	 * 
	 * @param shop
	 * @return
	 */
	Shop queryByShopId(long shopId);

	/**
	 * insert new shops
	 * 
	 * @param shop
	 * @return
	 */
	int insertShop(Shop shop);

	/**
	 * update shops
	 * 
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);

}

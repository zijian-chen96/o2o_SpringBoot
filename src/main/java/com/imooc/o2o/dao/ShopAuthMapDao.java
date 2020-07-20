package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.ShopAuthMap;

public interface ShopAuthMapDao {

	/**
	 * return shop auth and display split by page
	 * 
	 * @param shopId
	 * @param beginIndex
	 * @param pageSize
	 * @return
	 */
	List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * return the total number of auth under the shop
	 * 
	 * @param shopId
	 * @return
	 */
	int queryShopAuthCountByShopId(@Param("shopId") long shopId);
	
	/**
	 * insert a shop auth
	 * 
	 * @param shopAuthMap
	 * @return
	 */
	int insertShopAuthMap(ShopAuthMap shopAuthMap);
	
	/**
	 * update auth info
	 * 
	 * @param shopAuthMap
	 * @return
	 */
	int updateShopAuthMap(ShopAuthMap shopAuthMap);
	
	/**
	 * remove the auth from employee
	 * 
	 * @param shopAuthId
	 * @return
	 */
	int deleteShopAuthMap(long shopAuthId);
	
	/**
	 * search employee auth info by shopAuthId
	 * 
	 * @param shopAuthId
	 * @return
	 */
	ShopAuthMap queryShopAuthMapById(Long shopAuthId);

}

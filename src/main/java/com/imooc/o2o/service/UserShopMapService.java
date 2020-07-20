package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;

public interface UserShopMapService {
	
	/**
	 * input search condition and display split by pages
	 * 
	 * @param userShopMapCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize);
	
	/**
	 * input userId and shopId to get the user points under the shop
	 * 
	 * @param userId
	 * @param shopId
	 * @return
	 */
	UserShopMap getUserShopMap(long userId, long shopId);

}

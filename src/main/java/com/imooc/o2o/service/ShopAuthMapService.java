package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.exceptions.ShopAuthMapOperationException;

public interface ShopAuthMapService {
	
	/**
	 * input shopId and display shopAuth info split by page
	 * 
	 * @param shopId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);
	
	/**
	 * return auth info by input shopAuthId
	 * 
	 * @param shopAuthId
	 * @return
	 */
	ShopAuthMap getShopAuthMapById(Long shopAuthId);
	
	/**
	 * add auth info 
	 * 
	 * @param shopAuthMap
	 * @return
	 * @throws ShopAuthMapOperationException
	 */
	ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;
	
	/**
	 * update auth info, include title, status and so on...
	 * 
	 * @param shopAuthMap
	 * @return
	 * @throws ShopAuthMapOperationException
	 */
	ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException;
	
}

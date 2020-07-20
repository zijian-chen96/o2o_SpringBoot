package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.exceptions.UserProductMapOperationException;

public interface UserProductMapService {
	
	/**
	 * list out user purchase history by input search condition
	 * 
	 * @param userProductCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize);
	
	/**
	 * add purchase history
	 * 
	 * @param userProductMap
	 * @return
	 * @throws UserProductMapOperationException
	 */
	UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;

}

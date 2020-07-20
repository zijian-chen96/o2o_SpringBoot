package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.exceptions.UserAwardMapOperationException;

public interface UserAwardMapService {

	/**
	 * input search condition and display by page
	 * 
	 * @param userAwardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex, Integer pageSize);

	/**
	 * input search condition and display by page
	 *
	 * @param userAwardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserAwardMapExecution listReceivedUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex,
			Integer pageSize);

	/**
	 * get user award info by input awardId
	 * 
	 * @param userAwardMapId
	 * @return
	 */
	UserAwardMap getUserAwardMapById(long userAwardMapId);

	/**
	 * redeem the award, add awards info
	 * 
	 * @param userAwardMap
	 * @return
	 */
	UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

	/**
	 * modify the info, to change the award usedStatus
	 * 
	 * @param userAwardMap
	 * @return
	 * @throws UserAwardMapOperationException
	 */
	UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

}

package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.UserAwardMap;

public interface UserAwardMapDao {

	/**
	 * search user redeem history by input conditions and display split by pages
	 * 
	 * @param userAwardCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * search user redeem history by input conditions and display split by pages
	 *
	 * @param userAwardCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserAwardMap> queryReceivedUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	/**
	 * return the number of search
	 * 
	 * @param userAwardCondition
	 * @return
	 */
	int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);

	/**
	 * return the redeem info by userAwardId
	 * 
	 * @param userAwardId
	 * @return
	 */
	UserAwardMap queryUserAwardMapById(long userAwardId);

	/**
	 * add redeem info
	 * 
	 * @param userAwardMap
	 * @return
	 */
	int insertUserAwardMap(UserAwardMap userAwardMap);

	/**
	 * update redeem info, main purpose for update status of redeem
	 * 
	 * @param userAwardMap
	 * @return
	 */
	int updateUserAwardMap(UserAwardMap userAwardMap);

}

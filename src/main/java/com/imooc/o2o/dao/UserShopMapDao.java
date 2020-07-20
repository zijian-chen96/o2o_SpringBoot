package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.UserShopMap;

public interface UserShopMapDao {
	
	/**
	 * search user shop points by input condition and display split by page
	 * 
	 * @param userShopConditionMap
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserShopMap> queryUserShopMapList(@Param("userShopCondition") UserShopMap userShopConditionMap,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * return total number of the search
	 * 
	 * @param userShopCondition
	 * @return
	 */
	int queryUserShopMapCount(@Param("userShopCondition") UserShopMap userShopCondition);
	
	/**
	 * search the user's point under the shop by input userId and shopId
	 * 
	 * @param userId
	 * @param shopId
	 * @return
	 */
	UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);
	
	/**
	 * insert a user shop point record
	 * 
	 * @param userShopMap
	 * @return
	 */
	int insertUserShopMap(UserShopMap userShopMap);
	
	/**
	 * update user's points under the shop
	 * 
	 * @param userShopMap
	 * @return
	 */
	int updateUserShopMapPoint(UserShopMap userShopMap);

}

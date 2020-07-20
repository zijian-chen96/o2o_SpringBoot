package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.UserProductMap;

public interface UserProductMapDao {
	
	/**
	 * search user order history and display split by page
	 * 
	 * @param userProductCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<UserProductMap> queryUserProductMapList(@Param("userProductCondition") UserProductMap userProductCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * return the number of search
	 * 
	 * @param userProductCondition
	 * @return
	 */
	int queryUserProductMapCount(@Param("userProductCondition") UserProductMap userProductCondition);
	
	/**
	 * insert one user order record
	 * 
	 * @param userProductMap
	 * @return
	 */
	int insertUserProductMap(UserProductMap userProductMap);
	
}

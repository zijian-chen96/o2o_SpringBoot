package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {
	
	/**
	 * search user by userId
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);
	
	/**
	 * add userInfo
	 * 
	 * @param personInfo
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);

}

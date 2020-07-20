package com.imooc.o2o.service;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoService {
	
	/**
	 * search personInfo by use personId
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);

}

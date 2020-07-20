package com.imooc.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/**
	 * search the userInfo by input username and password, for login use
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

	/**
	 * search localAuth by userId
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalByUserId(@Param("userId") long userId);

	/**
	 * insert localAuth account
	 * 
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * change the password by input userId, username, password
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId, @Param("username") String username,
			@Param("password") String password, @Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);

}

package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {

	/**
	 * get localAuth account info by username and password
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password);

	/**
	 * get localAuth account info by userId
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * binding the wechatAuth account, generate localAuth account
	 * 
	 * @param localAuth
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * edit localAuth account login password
	 * 
	 * @param userId
	 * @param username
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 * @throws LocalAuthOperationException
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword)
			throws LocalAuthOperationException;
}

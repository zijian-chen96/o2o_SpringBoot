package com.imooc.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
		return localAuthDao.queryLocalByUserNameAndPwd(userName, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		// null checker, input localAuth username password,
		// userInfo special userId can not be empty, otherwise return error
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null
				|| localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}

		// search the user already bind localAuth account or not
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());

		if (tempAuth != null) {
			// if already bind a localAuth, then exit, to make sure localAuth account unique
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}

		try {
			// if never bind a localAuth account, then create one localAuth for the user to
			// bind
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());

			// encrypt the password
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);

			// check is the creation success or not
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("failed to bind account");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			throw new LocalAuthOperationException("insertLocalAuth error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword)
			throws LocalAuthOperationException {
		// null checker, input userId, username, old password,
		// new password are not empty, and new/old password can not same,
		// otherwise return error msg
		if (userId != null && userName != null && password != null && newPassword != null
				&& !password.equals(newPassword)) {
			
			try {
				// update password, and encrypt the new password by use MD5
				int effectedNum = localAuthDao.updateLocalAuth(userId, userName, MD5.getMd5(password), MD5.getMd5(newPassword), new Date());
				
				// check update success or not
				if(effectedNum <= 0) {
					throw new LocalAuthOperationException("filed to update password");
				}
				
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
				
			} catch(Exception e) {
				throw new LocalAuthOperationException("failed to update password:" + e.toString());
			}
		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

}

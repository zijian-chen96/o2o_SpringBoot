package com.imooc.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dao.WechatAuthDao;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.exceptions.WechatAuthOperationException;
import com.imooc.o2o.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
		// null checker
		if(wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		
		try {
			// set the create time
			wechatAuth.setCreateTime(new Date());
			
			// if the wechat account's userInfo and its userId is null, 
			// then the user is first time use our web-App(and it is use wechat account login)
			// and need to auto create a user account or userInfo
			if(wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setEnableStatus(1);
					
					PersonInfo personInfo = wechatAuth.getPersonInfo();
					int effectedNum = personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setPersonInfo(personInfo);
					if(effectedNum <= 0) {
						throw new WechatAuthOperationException("failed to add userInfo");
					}
				} catch(Exception e) {
					log.error("insertPersonInfo error:" + e.toString());
					throw new WechatAuthOperationException("insertPersonInfo error:" + e.getMessage());
				}
			}
			
			// create web-App's wechat account
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if(effectedNum <= 0) {
				throw new WechatAuthOperationException("failled to create account");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
			}
			
		} catch(Exception e) {
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatAuthOperationException("insertWechatAuth error;" + e.getMessage());
		}
		
	}
	

}

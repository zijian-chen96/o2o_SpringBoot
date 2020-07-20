package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
	
	/**
	 * user openId to search the web-App wechat account
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);
	
	/**
	 * register a web-app wechat account
	 * 
	 * @param wechatAuth
	 * @return
	 * @throws Wechat
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;
	
}

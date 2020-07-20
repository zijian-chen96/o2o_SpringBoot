package com.imooc.o2o.dao;

import com.imooc.o2o.entity.WechatAuth;

public interface WechatAuthDao {
	
	/**
	 * search our web-App's Wechat account by use openId
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth queryWechatInfoByOpenId(String openId);
	
	/**
	 * add to our web-App Wechat account
	 * 
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);

}

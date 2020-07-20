package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest {
	@Autowired
	private WechatAuthDao wechatAuthDao;
	
	@Test
	public void testAInsertWechatAuth() throws Exception {
		// add new wechat account
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		
		// set the userInfo to wechat account
		wechatAuth.setPersonInfo(personInfo);
		
		// set a openId
		wechatAuth.setOpenId("abcdefghizklmnopqrst");
		wechatAuth.setCreateTime(new Date());
		int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
		assertEquals(1, effectedNum);	
	}
	
	@Test
	public void testBQueryWechatAuthByOpenId() throws Exception {
		WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("abcdefghizklmnopqrst");
		assertEquals("test", wechatAuth.getPersonInfo().getName());
	}
	
}

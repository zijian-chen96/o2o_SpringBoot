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

import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest {
	@Autowired
	private LocalAuthDao localAuthDao;
	private static final String username = "testusername";
	private static final String password = "testpassword";
	
	@Test
	public void testAInsertLocalAuth() throws Exception {
		// add localAuth account info
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		
		// set the personInfo to localAuth
		localAuth.setPersonInfo(personInfo);
		// set the username and password
		localAuth.setUsername(username);
		localAuth.setPassword(password);
		localAuth.setCreateTime(new Date());
		int effectedNum = localAuthDao.insertLocalAuth(localAuth);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testBQueryLocalByUserNameAndPwd() throws Exception {
		// search personInfo by username and password
		LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username, password);
		assertEquals("test", localAuth.getPersonInfo().getName());
	}
	
	@Test
	public void testCQueryLocalByUserId() throws Exception {
		// search personInfo by userId
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		assertEquals("test", localAuth.getPersonInfo().getName());
	}
	
	@Test
	public void testDUpdateLocalAuth() throws Exception {
		// change localAuth password by input userId localAuth account
		Date now = new Date();
		int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, password + "new", now);
		assertEquals(1, effectedNum);
		
		// search the new account info on localAuth
		LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
		
		// output new password
		System.out.println(localAuth.getPassword());
	}
	
}

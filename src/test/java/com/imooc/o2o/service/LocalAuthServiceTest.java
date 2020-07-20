package com.imooc.o2o.service;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.WechatAuthStateEnum;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest {
	
	@Autowired
	private LocalAuthService localAuthService;
	
	@Test
	@Ignore
	public void testABindLocalAuth() {
		// add one localAuth account
		LocalAuth localAuth = new LocalAuth();
		PersonInfo personInfo = new PersonInfo();
		String username = "testusername";
		String password = "testpassword";
		
		// set personInfo to localAuth account
		// set the userId on, means someone create this account
		personInfo.setUserId(1L);
		
		// set the personInfo to localAuth, means bind to the user
		localAuth.setPersonInfo(personInfo);
		
		// set the username
		localAuth.setUsername(username);
		
		// set the password
		localAuth.setPassword(password);
		
		// bind the account
		LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), lae.getState());
		
		// search the localAuth by userId
		localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
		
		System.out.println("userName: " + localAuth.getPersonInfo().getName());
		System.out.println("localAuth password: " + localAuth.getPassword());
	}
	
	@Test
	public void testBModifyLocalAuth() {
		// set the account info
		long userId = 1;
		String username = "testusername";
		String password = "testpassword";
		String newPassword = "testnamepassword";
		
		// modify the account password
		LocalAuthExecution lae = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), lae.getState());
		
		// get account password after modify
		LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
		
		System.out.println(localAuth.getPersonInfo().getName());
	}
	

}

package com.imooc.o2o.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(Alphanumeric.class)
public class UserAwardMapDaoTest {

	@Autowired
	private UserAwardMapDao userAwardMapDao;

	/**
	 * test insert
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAInsertUserAwardMap() throws Exception {
		// create user award info 1
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userAwardMap.setUser(customer);
		userAwardMap.setOperator(customer);
		Award award = new Award();
		award.setAwardId(1L);
		userAwardMap.setAward(award);
		Shop shop = new Shop();
		shop.setShopId(1L);
		userAwardMap.setShop(shop);
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setUsedStatus(1);
		userAwardMap.setPoint(1);

		int effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
		assertEquals(1, effectedNum);

		// create user award info 2
		UserAwardMap userAwardMap2 = new UserAwardMap();
		PersonInfo customer2 = new PersonInfo();
		customer2.setUserId(1L);
		userAwardMap2.setUser(customer2);
		userAwardMap2.setOperator(customer2);
		Award award2 = new Award();
		award2.setAwardId(1L);
		userAwardMap2.setAward(award2);
		userAwardMap2.setShop(shop);
		userAwardMap2.setCreateTime(new Date());
		userAwardMap2.setUsedStatus(0);
		userAwardMap2.setPoint(1);
		
		effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap2);
		assertEquals(1, effectedNum);
	}
	
	/**
	 * test search
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBQueryUserAwardMapList() throws Exception {
		UserAwardMap userAwardMap = new UserAwardMap();
		
		// test queryUserAwardMapList
		List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(3, userAwardMapList.size());
		
		int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
		assertEquals(3, count);
		
		PersonInfo customer = new PersonInfo();
		
		// search by  user name
		customer.setName("test");
		userAwardMap.setUser(customer);
		userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(3, userAwardMapList.size());
		
		count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
		assertEquals(3, count);
		
		// test queryUserAwardMapById assume will return second award info
		userAwardMap = userAwardMapDao.queryUserAwardMapById(userAwardMapList.get(0).getUserAwardId());
		assertEquals("my award", userAwardMap.getAward().getAwardName());
	}
	
	/**
	 * test update
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCUpdateUserAwardMap() throws Exception {
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		
		// search by fuzzy user name
		customer.setName("test");
		userAwardMap.setUser(customer);
		List<UserAwardMap> userAwardMapsList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 1);
		assertTrue(0 == userAwardMapsList.get(0).getUsedStatus(), "Error, point not same");
		userAwardMapsList.get(0).setUsedStatus(1);
		int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMapsList.get(0));
		assertEquals(1, effectedNum);
	}

}

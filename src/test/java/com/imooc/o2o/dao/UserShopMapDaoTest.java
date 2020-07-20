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

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(Alphanumeric.class)
public class UserShopMapDaoTest {

	@Autowired
	private UserShopMapDao userShopMapDao;

	/**
	 * test insert
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAInsertUserShopMap() throws Exception {
		// create user shop point info 1
		UserShopMap userShopMap = new UserShopMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userShopMap.setUser(customer);
		Shop shop = new Shop();
		shop.setShopId(1L);
		userShopMap.setShop(shop);
		userShopMap.setCreateTime(new Date());
		userShopMap.setPoint(1);
		int effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
		assertEquals(1, effectedNum);

		// create user shop point info 2
		UserShopMap userShopMap2 = new UserShopMap();
		PersonInfo customer2 = new PersonInfo();
		customer2.setUserId(3L);
		userShopMap2.setUser(customer2);
		Shop shop2 = new Shop();
		shop2.setShopId(10L);
		userShopMap2.setShop(shop2);
		userShopMap2.setCreateTime(new Date());
		userShopMap2.setPoint(1);
		effectedNum = userShopMapDao.insertUserShopMap(userShopMap2);
		assertEquals(1, effectedNum);
	}
	
	/**
	 * test search 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBQueryUserShopMap() throws Exception {
		UserShopMap userShopMap = new UserShopMap();
		// search all
		List<UserShopMap> userProductMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 2);
		assertEquals(2, userProductMapList.size());
		int count = userShopMapDao.queryUserShopMapCount(userShopMap);
		assertEquals(2, count);
		
		// search shop
		Shop shop = new Shop();
		shop.setShopId(1L);
		userShopMap.setShop(shop);
		userProductMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 3);
		assertEquals(1, userProductMapList.size());
		count = userShopMapDao.queryUserShopMapCount(userShopMap);
		assertEquals(1, count);
		
		// search by userId and shop
		userShopMap = userShopMapDao.queryUserShopMap(1, 1);
		assertEquals("test", userShopMap.getUser().getName());
	}
	
	/**
	 * test update
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCUpdateUserShopMap() throws Exception {
		UserShopMap userShopMap = new UserShopMap();
		userShopMap = userShopMapDao.queryUserShopMap(1, 1);
		assertTrue(1 == userShopMap.getPoint(), "Error, points not same!");
		userShopMap.setPoint(2);
		int effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
		assertEquals(1, effectedNum);
	}
	

}

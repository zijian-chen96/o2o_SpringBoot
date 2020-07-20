package com.imooc.o2o.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.imooc.o2o.entity.ShopAuthMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(Alphanumeric.class)
public class ShopAuthMapDaoTest {

	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	/**
	 * test insert
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAInsertShopAuthMap() throws Exception {
		// create shop auth info 1
		ShopAuthMap shopAuthMap1 = new ShopAuthMap();
		PersonInfo employee = new PersonInfo();
		employee.setUserId(1L);
		shopAuthMap1.setEmployee(employee);
		Shop shop = new Shop();
		shop.setShopId(1L);
		shopAuthMap1.setShop(shop);
		shopAuthMap1.setTitle("CEO");
		shopAuthMap1.setTitleFlag(1);
		shopAuthMap1.setCreateTime(new Date());
		shopAuthMap1.setLastEditTime(new Date());
		shopAuthMap1.setEnableStatus(1);
		int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
		assertEquals(1, effectedNum);
		
		// create shop auth info 2
		ShopAuthMap shopAuthMap2 = new ShopAuthMap();
		shopAuthMap2.setEmployee(employee);
		Shop shop2 = new Shop();
		shop2.setShopId(10L);
		shopAuthMap2.setShop(shop2);
		shopAuthMap2.setTitle("worker");
		shopAuthMap2.setTitleFlag(2);
		shopAuthMap2.setCreateTime(new Date());
		shopAuthMap2.setLastEditTime(new Date());
		shopAuthMap2.setEnableStatus(0);
		effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
		assertEquals(1, effectedNum);
	}

	/**
	 * test search
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBQueryShopAuth() throws Exception {
		// test queryShopAuthMapListByShopId
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 2);
		assertEquals(1, shopAuthMapList.size());
		
		// test queryShopAuthMapById
		ShopAuthMap shopAuth = shopAuthMapDao.queryShopAuthMapById(shopAuthMapList.get(0).getShopAuthId());
		assertEquals("CEO", shopAuth.getTitle());
		System.out.println("employee's name is :" + shopAuth.getEmployee().getName());
		System.out.println("shop name is :" + shopAuth.getShop().getShopName());
		
		// test queryShopAuthCountByShopId
		int count = shopAuthMapDao.queryShopAuthCountByShopId(1);
		assertEquals(1, count);
	}

	/**
	 * test update
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCUpdateShopAuthMap() throws Exception {
		List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 1);
		shopAuthMapList.get(0).setTitle("CCO");
		shopAuthMapList.get(0).setTitleFlag(2);
		int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMapList.get(0));
		assertEquals(1, effectedNum);
	}

	/**
	 * test delete
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteShopAuthMap() throws Exception {
		List<ShopAuthMap> shopAuthMapList1 = shopAuthMapDao.queryShopAuthMapListByShopId(1, 0, 1);
		List<ShopAuthMap> shopAuthMapList2 = shopAuthMapDao.queryShopAuthMapListByShopId(10, 0, 1);
		int effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList1.get(0).getShopAuthId());
		assertEquals(1, effectedNum);
		effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList2.get(0).getShopAuthId());
		assertEquals(1, effectedNum);
	}

}

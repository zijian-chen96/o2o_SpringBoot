package com.imooc.o2o.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(Alphanumeric.class)
public class ProductSellDailyDaoTest {

	@Autowired
	private ProductSellDailyDao productSellDailyDao;

	/**
	 * test insert
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAInsertProductSellDaily() throws Exception {
		// create product daily sell count
		int effectedNum = productSellDailyDao.insertProductSellDaily();
		assertEquals(3, effectedNum);
	}
	
	@Test
	public void testBInsertDefaultProductSellDaily() throws Exception {
		// create product sells
		int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
		assertEquals(8, effectedNum);
	}

	@Test
	public void testCQueryProductSellDaily() throws Exception {
		ProductSellDaily productSellDaily = new ProductSellDaily();

		// add shop to search
		Shop shop = new Shop();
		shop.setShopId(1L);
		productSellDaily.setShop(shop);
		Calendar calendar = Calendar.getInstance();
		// get last day time
		calendar.add(Calendar.DATE, -1);
		Date endTime = calendar.getTime();

		// get 7 day before time
		calendar.add(Calendar.DATE, -6);
		Date beginTime = calendar.getTime();
		List<ProductSellDaily> productSellDailyList = productSellDailyDao.queryProductSellDailyList(productSellDaily,
				beginTime, endTime);
		assertEquals(9, productSellDailyList.size());
	}

}

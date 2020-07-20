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

import com.imooc.o2o.entity.Award;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(Alphanumeric.class)
public class AwardDaoTest {

	@Autowired
	private AwardDao awardDao;
	
	/**
	 * test create award
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAInsertAward() throws Exception {
		long shopId = 1;

		// create award 1
		Award award1 = new Award();
		award1.setAwardName("test 1");
		award1.setAwardImg("testImg 1");
		award1.setPoint(5);
		award1.setPriority(1);
		award1.setEnableStatus(1);
		award1.setCreateTime(new Date());
		award1.setLastEditTime(new Date());
		award1.setShopId(shopId);
		int effectedNum = awardDao.insertAward(award1);
		assertEquals(1, effectedNum);

		// create award 2
		Award award2 = new Award();
		award2.setAwardName("test 2");
		award2.setAwardImg("testImg 2");
		award2.setPoint(2);
		award2.setPriority(2);
		award2.setEnableStatus(0);
		award2.setCreateTime(new Date());
		award2.setLastEditTime(new Date());
		award2.setShopId(shopId);
		effectedNum = awardDao.insertAward(award2);
		assertEquals(1, effectedNum);
	}
	
	/**
	 * test search list 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBQueryAwardList() throws Exception {
		Award award = new Award();
		List<Award> awarList = awardDao.queryAwardList(award, 0, 3);
		assertEquals(3, awarList.size());
		
		int count = awardDao.queryAwardCount(award);
		assertEquals(4, count);
		
		award.setAwardName("test");
		awarList = awardDao.queryAwardList(award, 0, 3);
		assertEquals(2, awarList.size());
		
		count = awardDao.queryAwardCount(award);
		assertEquals(2, count);		
	}
	
	/**
	 * test search by id
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCQueryAwardByAwardId() throws Exception {
		Award awardConditionAward = new Award();
		awardConditionAward.setAwardName("test 1");
		// search by name
		List<Award> awarlList = awardDao.queryAwardList(awardConditionAward, 0, 1);
		assertEquals(1, awarlList.size());
		
		// search by awardId
		Award award = awardDao.queryAwardByAwardId(awarlList.get(0).getAwardId());
		assertEquals("test 1", award.getAwardName());
	}
	
	/**
	 * test update
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDUpdateAward() throws Exception {
		Award awardCondition = new Award();
		awardCondition.setAwardName("test 1");
		
		// search by name
		List<Award> awarlList = awardDao.queryAwardList(awardCondition, 0, 1);
		
		// edit product name
		awarlList.get(0).setAwardName("first test 1");
		int effectedNum = awardDao.updateAward(awarlList.get(0));
		assertEquals(1, effectedNum);
		
		// search the edited award and test
		Award award = awardDao.queryAwardByAwardId(awarlList.get(0).getAwardId());
		assertEquals("first test 1", award.getAwardName());
	}
	
	/**
	 * test delete 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEDeleteAward() throws Exception {
		Award awardCondition = new Award();
		awardCondition.setAwardName("test");
		
		// search all test awards and delete
		List<Award> awardList = awardDao.queryAwardList(awardCondition, 0, 2);
		assertEquals(2, awardList.size());
		
		for(Award award : awardList) {
			int effectedNum = awardDao.deleteAward(award.getAwardId(), award.getShopId());
			assertEquals(1, effectedNum);
		}
	}

}

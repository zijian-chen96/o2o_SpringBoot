package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Award;

public interface AwardDao {
	
	/**
	 * search award info by input condition and display by split pages
	 * 
	 * @param awardCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<Award> queryAwardList(@Param("awardCondition") Award awardCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	
	/**
	 * return the total number of search result
	 * 
	 * @param awardCondition
	 * @return
	 */
	int queryAwardCount(@Param("awardCondition") Award awardCondition);
	
	/**
	 * search awardInfo by input awardId
	 * 
	 * @param awardId
	 * @return
	 */
	Award queryAwardByAwardId(long awardId);
	
	/**
	 * add new award info
	 * 
	 * @param award
	 * @return
	 */
	int insertAward(Award award);
	
	/**
	 * update awardInfo 
	 * 
	 * @param award
	 * @return
	 */
	int updateAward(Award award);
	
	/**
	 * delete awardInfo
	 * 
	 * @param awardId
	 * @param shopId
	 * @return
	 */
	int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);
	
}

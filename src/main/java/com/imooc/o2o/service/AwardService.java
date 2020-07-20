package com.imooc.o2o.service;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;

public interface AwardService {
	
	/**
	 * input search condition, return awards by pages
	 * 
	 * @param awardCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize);
	
	/**
	 * search award info by input awardId
	 * 
	 * @param awardId
	 * @return
	 */
	Award getAwardById(long awardId);
	
	/**
	 * add new award info, and add award image
	 * 
	 * @param award
	 * @param thumbnail
	 * @return
	 */
	AwardExecution addAward(Award award, ImageHolder thumbnail);
	
	/**
	 * modify the input award info, if input new image will delete old image
	 * 
	 * @param award
	 * @param thumbnail
	 * @return
	 */
	AwardExecution modifyAward(Award award, ImageHolder thumbnail);

}

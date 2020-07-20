package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.AwardDao;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.exceptions.AwardOperationException;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class AwardServiceImpl implements AwardService {

	@Autowired
	private AwardDao awardDao;

	@Override
	public AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize) {
		// convert page
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);

		// input search condition get awards and display by pages
		List<Award> awardList = awardDao.queryAwardList(awardCondition, rowIndex, pageSize);

		// return total number
		int count = awardDao.queryAwardCount(awardCondition);
		AwardExecution ae = new AwardExecution();
		ae.setAwardList(awardList);
		ae.setCount(count);
		return ae;
	}

	@Override
	public Award getAwardById(long awardId) {
		return awardDao.queryAwardByAwardId(awardId);
	}

	@Override
	@Transactional
	// 1.handle viewImage, get viemImage relative path and set the award
	// 2.insert award info to tb_award
	public AwardExecution addAward(Award award, ImageHolder thumbnail) {
		// null checker
		if (award != null && award.getShopId() != null) {
			// set default value to award
			award.setCreateTime(new Date());
			award.setLastEditTime(new Date());
			
			// award default enableStatus to 1, it will shows on front-end system
			award.setEnableStatus(1);
		
			if (thumbnail != null) {
				// if there is image stream needs add image
				addThumbnail(award, thumbnail);
			}
			try {
				// insert award info
				int effectedNum = awardDao.insertAward(award);
				if (effectedNum <= 0) {
					throw new AwardOperationException("failed to create awards");
				}
			} catch (Exception e) {
				throw new AwardOperationException("failed to create awards: " + e.toString());
			}
			return new AwardExecution(AwardStateEnum.SUCCESS, award);
		} else {
			return new AwardExecution(AwardStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	// 1. if have viewImage and it need to process the viewImage first
	// if already have viewImage need to delete it and add new viewImages
	// 2. update tb_award info
	public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {
		// null checker
		if (award != null && award.getAwardId() != null) {
			award.setLastEditTime(new Date());

			if (thumbnail != null) {
				// get object by input awardId
				Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());

				// if there is image, then need to delete the old images
				if (tempAward.getAwardImg() != null) {
					ImageUtil.deleteFileOrPath(tempAward.getAwardImg());
				}
				// store the image, and get relative path
				addThumbnail(award, thumbnail);
			}
			try {
				// input the object to update the info
				int effectedNum = awardDao.updateAward(award);

				if (effectedNum <= 0) {
					throw new AwardOperationException("failed to update award info");
				}
				return new AwardExecution(AwardStateEnum.SUCCESS, award);
			} catch (Exception e) {
				throw new AwardOperationException("failed to update award info:" + e.toString());
			}
		} else {
			return new AwardExecution(AwardStateEnum.EMPTY);
		}
	}

	/**
	 * store the image to relative path, and return the address
	 * 
	 * @param award
	 * @param thumbnail
	 */
	private void addThumbnail(Award award, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(award.getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		award.setAwardImg(thumbnailAddr);
	}

}

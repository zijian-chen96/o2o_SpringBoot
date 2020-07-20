package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.UserAwardMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.exceptions.UserAwardMapOperationException;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {

	@Autowired
	private UserAwardMapDao userAwardMapDao;

	@Autowired
	private UserShopMapDao userShopMapDao;

	@Override
	public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex,
			Integer pageSize) {
		// null checker
		if (userAwardCondition != null && pageIndex != null && pageSize != null) {
			// convert page
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);

			// input search condition to get award by page(user award list)
			List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardCondition, beginIndex,
					pageSize);

			// return total number
			int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
			UserAwardMapExecution ue = new UserAwardMapExecution();
			ue.setUserAwardMapList(userAwardMapList);
			ue.setCount(count);
			return ue;
		} else {
			return null;
		}
	}

	@Override
	public UserAwardMapExecution listReceivedUserAwardMap(UserAwardMap userAwardCondition, Integer pageIndex,
			Integer pageSize) {
		// null checker
		if (userAwardCondition != null && pageIndex != null && pageSize != null) {
			// convert page
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);

			// input search condition to get award by page(user award list)
			List<UserAwardMap> userAwardMapList = userAwardMapDao.queryReceivedUserAwardMapList(userAwardCondition, beginIndex,
					pageSize);

			// return total number
			int count = userAwardMapDao.queryUserAwardMapCount(userAwardCondition);
			UserAwardMapExecution ue = new UserAwardMapExecution();
			ue.setUserAwardMapList(userAwardMapList);
			ue.setCount(count);
			return ue;
		} else {
			return null;
		}
	}

	@Override
	public UserAwardMap getUserAwardMapById(long userAwardMapId) {
		return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
	}

	@Override
	@Transactional
	public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
		// null checker, userId and shopId cannot be empty
		if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null
				&& userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
			// set the default
			userAwardMap.setCreateTime(new Date());
			userAwardMap.setUsedStatus(0);
			try {
				int effectedNum = 0;
				// if the redeem the award need cost the points, then need minus the user points
				// from tb_user_shop_map
				if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
					// get the user's points by userId and shopId
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),
							userAwardMap.getShop().getShopId());
					// check the user have points under the shop or not
					if (userShopMap != null) {
						// if there is points, and make sure the points needs large than the award
						// points
						if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
							// count the remaining points
							userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
							// update the points info
							effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
							if (effectedNum <= 0) {
								throw new UserAwardMapOperationException("failed to update points");
							}
						} else {
							throw new UserAwardMapOperationException("failed points not enough");
						}
					} else {
						// no exist points in the current shop, and throw exception
						throw new UserAwardMapOperationException("no enough points, cannot process redeem awards");
					}
				}
				// insert award redeem info
				effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
				if (effectedNum <= 0) {
					throw new UserAwardMapOperationException("failed to redeem the awards");
				}
				return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
			} catch (Exception e) {
				throw new UserAwardMapOperationException("failed to redeem the awards: " + e.toString());
			}
		} else {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_INFO);
		}
	}

	@Override
	@Transactional
	public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
		// null checker, userAwardId and usedStatus can not be empty
		if (userAwardMap == null || userAwardMap.getUserAwardId() == null || userAwardMap.getUsedStatus() == null) {
			return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERAWARD_ID);
		} else {
			try {
				// update usedStatus
				int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
				if (effectedNum <= 0) {
					return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
				} else {
					return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
				}
			} catch (Exception e) {
				throw new UserAwardMapOperationException("modifyUserAwardMap error: " + e.getMessage());
			}
		}
	}

}

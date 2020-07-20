package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.UserProductMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.exceptions.UserProductMapOperationException;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class UserProductMapServiceImpl implements UserProductMapService {

	@Autowired
	private UserProductMapDao userProductMapDao;
	
	@Autowired
	private UserShopMapDao userShopMapDao;

	@Override
	public UserProductMapExecution listUserProductMap(UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize) {
		// null checker
		if (userProductCondition != null && pageIndex != null && pageSize != null) {
			// convert page
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);

			// display by page input search condition
			List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductCondition,
					beginIndex, pageSize);

			// get total number
			int count = userProductMapDao.queryUserProductMapCount(userProductCondition);
			UserProductMapExecution se = new UserProductMapExecution();
			se.setUserProductMapList(userProductMapList);
			se.setCount(count);

			return se;
		} else {
			return null;
		}
	}

	/**
	 * add purchase history
	 */
	@Override
	@Transactional
	public UserProductMapExecution addUserProductMap(UserProductMap userProductMap)
			throws UserProductMapOperationException {
		// null checker, customerId, shopId, operatorId cannot be null
		if (userProductMap != null && userProductMap.getUser().getUserId() != null
				&& userProductMap.getShop().getShopId() != null && userProductMap.getOperator().getUserId() != null) {
			// set default
			userProductMap.setCreateTime(new Date());
			
			try {
				// add purchase history
				int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
				if (effectedNum <= 0) {
					throw new UserProductMapOperationException("failed to insert purchase history");
				}

				// if this shopping can get points
				if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
					// search the customer did not purchase before
					UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userProductMap.getUser().getUserId(),
							userProductMap.getShop().getShopId());
					
					if (userShopMap != null && userShopMap.getUserShopId() != null) {
						// if already purchase, and have points record, then need update the points
						userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
						effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
						
						if (effectedNum <= 0) {
							throw new UserProductMapOperationException("failed to update points info");
						}
					} else {
						// if the customer no purchase history, then add new shop point record info(like initial membership)
						userShopMap = compactUserShopMap4Add(userProductMap.getUser().getUserId(),
								userProductMap.getShop().getShopId(), userProductMap.getPoint());
						effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
						
						if (effectedNum <= 0) {
							throw new UserProductMapOperationException("filled to create points info");
						}
					}
				}
				return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
			} catch (Exception e) {
				throw new UserProductMapOperationException("failed to add info: " + e.toString());
			}
		} else {
			return new UserProductMapExecution(UserProductMapStateEnum.NULL_USERPRODUCT_INFO);
		}
	}

	/**
	 * binding customer points info
	 * 
	 * @param userId
	 * @param shopId
	 * @param point
	 * @return
	 */
	private UserShopMap compactUserShopMap4Add(Long userId, Long shopId, Integer point) {
		UserShopMap userShopMap = null;
		// null checker
		if (userId != null && shopId != null) {
			userShopMap = new UserShopMap();
			PersonInfo customer = new PersonInfo();
			customer.setUserId(userId);
			Shop shop = new Shop();
			shop.setShopId(shopId);
			userShopMap.setUser(customer);
			userShopMap.setShop(shop);
			userShopMap.setCreateTime(new Date());
			userShopMap.setPoint(point);
		}
		return userShopMap;
	}

}

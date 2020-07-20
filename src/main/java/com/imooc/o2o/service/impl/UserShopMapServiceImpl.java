package com.imooc.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {

	@Autowired
	private UserShopMapDao userShopMapDao;

	@Override
	public UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex, int pageSize) {
		// null checker
		if (userShopMapCondition != null && pageIndex != -1 && pageSize != -1) {
			// convert page
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
			// input search condition and display
			List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMapCondition, beginIndex,
					pageSize);
			// get total count
			int count = userShopMapDao.queryUserShopMapCount(userShopMapCondition);
			UserShopMapExecution ue = new UserShopMapExecution();
			ue.setUserShopMapList(userShopMapList);
			ue.setCount(count);
			return ue;
		} else {
			return null;
		}
	}

	@Override
	public UserShopMap getUserShopMap(long userId, long shopId) {
		return userShopMapDao.queryUserShopMap(userId, shopId);
	}

}

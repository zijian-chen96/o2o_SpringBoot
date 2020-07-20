package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.exceptions.ShopAuthMapOperationException;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.util.PageCalculator;

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {

	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Override
	public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
		// null checker
		if (shopId != null && pageIndex != null && pageSize != null) {
			// convert page
			int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);

			// search the shopAuth and return
			List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, beginIndex,
					pageSize);

			// return total number
			int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
			ShopAuthMapExecution se = new ShopAuthMapExecution();
			se.setShopAuthMapList(shopAuthMapList);
			se.setCount(count);
			return se;
		} else {
			return null;
		}
	}

	@Override
	public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
		return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
	}

	@Override
	@Transactional
	public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {
		// null checker, check shopId and employeeId
		if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
				&& shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
			shopAuthMap.setCreateTime(new Date());
			shopAuthMap.setLastEditTime(new Date());
			shopAuthMap.setEnableStatus(1);
			
			try {
				// add auth info
				int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
				if(effectNum <= 0) {
					throw new ShopAuthMapOperationException("failed to insert auth info");
				}
				return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
			} catch (Exception e) {
				throw new ShopAuthMapOperationException("failed to insert auth info: " + e.toString());
			}
		} else {
			return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_INFO);
		}
	}

	@Override
	@Transactional
	public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap)
			throws ShopAuthMapOperationException {
		// null checker, check authId
		if(shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
			return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOPAUTH_ID);
		} else {
			try {
				int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
				if(effectedNum <= 0) {
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
				} else {
					// update success
					return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
				}
			} catch (Exception e) {
				throw new ShopAuthMapOperationException("modifyShopAuthMap error: " + e.toString());
			}
		}
	}

}

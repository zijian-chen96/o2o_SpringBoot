package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {
	
	private final static Logger log = LoggerFactory.getLogger(ShopServiceImpl.class);

	@Autowired
	private ShopDao shopDao;
	
	@Autowired
	private ShopAuthMapDao shopAuthMapDao;

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
		// check the null shop
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// edit shop default
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// add shop info
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				log.error("during insert shop Info, it return 0 update");
				throw new ShopOperationException("failed create shop");
			} else {
				if (thumbnail.getImage() != null) {
					// save image
					try {
						addShopImg(shop, thumbnail);
					} catch (Exception e) {
						log.error("addShopImg error: " + e.getMessage());
						throw new ShopOperationException("Failed to Insert images");
					}
					// update shop image addr
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						log.error("updateShopImg error");
						throw new ShopOperationException("Failed to Insert images");

					}
					
					// add shopAuthMap action
					ShopAuthMap shopAuthMap = new ShopAuthMap();
					shopAuthMap.setEmployee(shop.getOwner());
					shopAuthMap.setShop(shop);
					shopAuthMap.setTitle("Owner");
					shopAuthMap.setTitleFlag(0);
					shopAuthMap.setCreateTime(new Date());
					shopAuthMap.setLastEditTime(new Date());
					shopAuthMap.setEnableStatus(1);
					
					try {
						effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
						if(effectedNum <= 0) {
							log.error("addShop: failed to create authorization");
							throw new ShopOperationException("failed to create authorization");
						}
					} catch (Exception e) {
						log.error("insertShopAuthMap error: " + e.getMessage());
						throw new ShopOperationException("failed to create authorization");
					}
				}
			}

		} catch (Exception e) {
			log.error("addShop error:" + e.getMessage());
			throw new ShopOperationException("failed to create shop, please contact IT support");
		}

		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, ImageHolder thumbnail) {
		// get shopImg relative path
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(long shopId) {
		return shopDao.queryByShopId(shopId);
	}

	@Override
	public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail)
			throws ShopOperationException {

		if(shop == null || shop.getShopId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else {
			// 1.to check is it needs to modify images
			try {
				if(thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if(tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					addShopImg(shop, thumbnail);
				}
				// 2.update shopInfo
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if(effectedNum <= 0) {
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.SUCCESS, shop);
				}
			} catch(Exception e) {
				throw new ShopOperationException("modifyShop error: " + e.getMessage());
			}
		}
		
	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int count = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if(shopList != null) {
			se.setShopList(shopList);
			se.setCount(count);
		} else {
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		
		return se;
	}

}

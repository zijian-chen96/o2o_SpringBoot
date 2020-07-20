package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@RestController
@RequestMapping("/frontend")
public class MyShopPointController {
	
	@Autowired
	private UserShopMapService userShopMapService;

	/**
	 * list out user all points info
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listusershopmapsbycustomer", method = RequestMethod.GET)
	private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		// get customer info from session
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		
		// null checker
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			userShopMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			
			if (shopId > -1) {
				// if input shopId is not empty, then get the points under the shop
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userShopMapCondition.setShop(shop);
			}
			
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			if (shopName != null) {
				// if productName is not empty, then fuzzy search the productName
				Shop shop = new Shop();
				shop.setShopName(shopName);
				userShopMapCondition.setShop(shop);
			}
			
			// get the customer points by input search condition
			UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
			modelMap.put("userShopMapList", ue.getUserShopMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}

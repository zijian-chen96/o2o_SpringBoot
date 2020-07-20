package com.imooc.o2o.web.shopadmin;

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
@RequestMapping(value = "/shopadmin")
public class UserShopManagementController {
	
	@Autowired
	private UserShopMapService userShopMapService;
	
	@RequestMapping(value = "/listusershopmapbyshop", method = RequestMethod.GET)
	private Map<String, Object> listUserShopMapByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		// get shop info from session
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		
		// null checker
		if((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			
			// input search condition
			userShopMapCondition.setShop(currentShop);
			String userName = HttpServletRequestUtil.getString(request, "userName");
			
			if(userName != null) {
				// if there is userName, then it needs fuzzy search userName
				PersonInfo customer = new PersonInfo();
				customer.setName(userName);
				userShopMapCondition.setUser(customer);
			}
			
			// get the customer point under the shop, and split by page
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

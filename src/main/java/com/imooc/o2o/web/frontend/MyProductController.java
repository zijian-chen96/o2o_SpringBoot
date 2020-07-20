package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@RestController
@RequestMapping(value = "/frontend")
public class MyProductController {
	
	@Autowired
	private UserProductMapService userProductMapService;

	/**
	 * list all product purchase history of the customer
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
	private Map<String, Object> listUserProductMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		// get the customer info from the session
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		
		// null checker
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != -1)) {
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			
			if (shopId > -1) {
				// input the shop, and list the customer purchase history under the shop
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userProductMapCondition.setShop(shop);
			}
			
			String productName = HttpServletRequestUtil.getString(request, "productName");
			if (productName != null) {
				// if productName is not empty, then need fuzzy search
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}

			// get the customer purchase info by pages
			UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex,
					pageSize);
			modelMap.put("userProductMapList", ue.getUserProductMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}

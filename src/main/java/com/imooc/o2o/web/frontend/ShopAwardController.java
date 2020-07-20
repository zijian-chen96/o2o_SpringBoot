package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@RestController
@RequestMapping("/frontend")
public class ShopAwardController {

	@Autowired
	private AwardService awardService;

	@Autowired
	private UserShopMapService userShopMapService;

	/**
	 * list the awards under the shop
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
	private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get the page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// get the shopId from session
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// null checker
		if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
			// get the award name from front-end for fuzzy search
			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			Award awardCondition = compactAwardCondition4Search(shopId, awardName);
			// input search condition to get award info
			AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
			modelMap.put("awardList", ae.getAwardList());
			modelMap.put("count", ae.getCount());
			modelMap.put("success", true);
			// get user info from session, to show the points the user own under the shop
			PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
			// null checker
			if (user != null && user.getUserId() != null) {
				// get the points the user own under the shop
				UserShopMap userShopMap = userShopMapService.getUserShopMap(user.getUserId(), shopId);
				if (userShopMap == null) {
					modelMap.put("totalPoint", 0);
				} else {
					modelMap.put("totalPoint", userShopMap.getPoint());
				}
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}

		return modelMap;
	}

	/**
	 * binding the searching condition
	 * 
	 * @param shopId
	 * @param awardName
	 * @return
	 */
	private Award compactAwardCondition4Search(long shopId, String awardName) {
		Award awardCondition = new Award();
		awardCondition.setShopId(shopId);
		// get the enable awards
		awardCondition.setEnableStatus(1);
		if (awardName != null) {
			awardCondition.setAwardName(awardName);
		}
		return awardCondition;
	}
	
}

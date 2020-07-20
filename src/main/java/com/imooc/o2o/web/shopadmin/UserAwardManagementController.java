package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@RestController
@RequestMapping("/shopadmin")
public class UserAwardManagementController {

	@Autowired
	private UserAwardMapService userAwardMapService;

	@Autowired
	private PersonInfoService personInfoService;

	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/listuserawardmapbyshop", method = RequestMethod.GET)
	private Map<String, Object> listUserAwardMapByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get the shop info from session
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		// null checker
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			UserAwardMap userAwardMap = new UserAwardMap();
			userAwardMap.setShop(currentShop);
			// get awardName from request
			String awardName = HttpServletRequestUtil.getString(request, "awardName");

			if (awardName != null) {
				// if needs fuzzy search awardName
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMap.setAward(award);
			}

			// return by pages
			UserAwardMapExecution ue = userAwardMapService.listReceivedUserAwardMap(userAwardMap, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/**
	 *  operator scan the QRCode and give award, and prove customer received
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/exchangeaward", method = RequestMethod.GET)
	private String exchangeAward(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get the operator info
		WechatAuth auth = getOperatorInfo(request);
		if (auth != null) {
			// get user info by userId
			PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
			// set the user info to session
			request.getSession().setAttribute("user", operator);
			// decode the Wechat's response default state data
			String qrCodeinfo = new String(
					URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			try {
				// replace the aaa to \ and convert it to WechatInfo object class
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}

			// verify the QRCode time is expired or not
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			
			// get the userAwardId
			Long userAwardId = wechatInfo.getUserAwardId();

			// get customerId
			Long customerId = wechatInfo.getCustomerId();
			
			// combine customer and operator info into userAwardMap
			UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId, userAwardId, operator);
			if (userAwardMap != null) {
				try {
					// check the operator's privileges
					if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
						return "shop/operationfail";
					}
					// modify the award usedStatus
					UserAwardMapExecution se = userAwardMapService.modifyUserAwardMap(userAwardMap);
					if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
						return "shop/operationsuccess";
					}
				} catch (RuntimeException e) {
					return "shop/operationfail";
				}

			}
		}
		return "shop/operationfail";
	}

	/**
	 * get the operator info
	 * 
	 * @param request
	 * @return
	 */
	private WechatAuth getOperatorInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				String openId = token.getOpenId();
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return auth;
	}

	/**
	 * determine the createTime is it expired or not, max time 10 minutes
	 * 
	 * @param wechatInfo
	 * @return
	 */
	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		// null checker
		if (wechatInfo != null && wechatInfo.getUserAwardId() != null && wechatInfo.getCustomerId() != null
				&& wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if ((nowTime - wechatInfo.getCreateTime()) <= 600000) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * combine the userAward to Object class for scan use,
	 *  and change the usedStatus
	 * 
	 * @param customerId
	 * @param userAwardId
	 * @return
	 */
	private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
		UserAwardMap userAwardMap = null;
		// null checker
		if (customerId != null && userAwardId != null && operator != null) {
			// get the userAwardMap info
			userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
			userAwardMap.setUsedStatus(1);
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			userAwardMap.setUser(customer);
			userAwardMap.setOperator(operator);
		}
		return userAwardMap;
	}

	/**
	 * check the operator have privileges or not
	 * 
	 * @param userId
	 * @param userAwardMap
	 * @return
	 */
	private boolean checkShopAuth(long userId, UserAwardMap userAwardMap) {
		// get the auth info under the shop
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 1, 1000);
		// for loop, to check operator's privileges
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
			if (shopAuthMap.getEmployee().getUserId() == userId && shopAuthMap.getEnableStatus() == 1) {
				return true;
			}
		}
		return false;
	}

}

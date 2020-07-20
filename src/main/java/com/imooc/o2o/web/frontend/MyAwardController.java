package com.imooc.o2o.web.frontend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;

@RestController
@RequestMapping("/frontend")
public class MyAwardController {

	@Autowired
	private UserAwardMapService userAwardMapService;

	@Autowired
	private AwardService awardService;

	@Autowired
	private PersonInfoService personInfoService;

	/**
	 * get the customer award info by awardId
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
	private Map<String, Object> getAwardbyId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get userAwardId from front-end
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// null checker
		if (userAwardId > -1) {
			// get user awards info by userAwardId
			UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);

			// get award info by awardId
			Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());

			// send the award info back to front-end
			modelMap.put("award", award);
			modelMap.put("usedStatus", userAwardMap.getUsedStatus());
			modelMap.put("userAwardMap", userAwardMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}

	/**
	 * get customer redeem award list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
	private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// get user info from session
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

		// null checker, userId cannot be empty
		if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
			UserAwardMap userAwardMapCondition = new UserAwardMap();
			userAwardMapCondition.setUser(user);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");

			if (shopId > -1) {
				// if shopId is not empty, then add it to search condition,
				// and search the user redeem info under the shop
				Shop shop = new Shop();
				shop.setShopId(shopId);
				userAwardMapCondition.setShop(shop);
			}

			String awardName = HttpServletRequestUtil.getString(request, "awardName");
			if (awardName != null) {
				// if award name is not empty, then need add it into search condition for fuzzy
				// search
				Award award = new Award();
				award.setAwardName(awardName);
				userAwardMapCondition.setAward(award);
			}

			// get the user awards info and display by pages
			UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
			modelMap.put("userAwardMapList", ue.getUserAwardMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
		}
		return modelMap;
	}

	/**
	 * redeem the awards
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
	private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get user info from session
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// get awardId from request session
		Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
		// binding the user awards object
		UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);
		// null checker
		if (userAwardMap != null) {
			try {
				// add award info
				UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMap);
				if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please select your awards");
		}
		return modelMap;
	}

	// get Wechat userInfo api prefix
	private static String urlPrefix;

	// get Wechat userInfo api middle
	private static String urlMiddle;

	// get Wechat userInfo api suffix
	private static String urlSuffix;

	// response from Wechat to add user award info URL
	private static String exchangeUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		MyAwardController.urlPrefix = urlPrefix;
	}

	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		MyAwardController.urlMiddle = urlMiddle;
	}

	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		MyAwardController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.exchange.url}")
	public void setExchangeUrl(String exchangeUrl) {
		MyAwardController.exchangeUrl = exchangeUrl;
	}

	/**
	 * create award redeem QRCode for operator scan, to scan the QRCode will link to
	 * the URL
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
	private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
		// get userAwardId from front-end
		long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
		// get the userAward object class by input Id
		UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
		// get customer info from session
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		
		// null checker
		if (userAwardMap != null && user != null && user.getUserId() != null
				&& userAwardMap.getUser().getUserId() == user.getUserId()) {
			// get the current timeStamp, to make sure the QRCode are not expired, unit is ms
			long timpStamp = System.currentTimeMillis();
			
			// set shopId and timeStamp to content, and set into state, once Wechat got
			// those info it will response to add auth
			// add aaa is for adding info to replace those info to use
			String content = "{aaauserAwardIdaaa:" + userAwardId + ",aaacustomerIdaaa:" + user.getUserId()
					+ ",aaacreateTimeaaa:" + timpStamp + "}";
			try {
				// encode the content to base64 avoid special character, then concat URL
				String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				
				// convert longURL to shortURL
				String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
				
				// generate QR code util tool, input shortURL, generate QR Code
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				
				// send QR code image stream to front-end
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * binding the user awards object
	 * 
	 * @param user
	 * @param awardId
	 * @return
	 */
	private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
		UserAwardMap userAwardMap = null;
		// null checker
		if (user != null && user.getUserId() != null && awardId != -1) {
			userAwardMap = new UserAwardMap();
			// get user info by userId
			PersonInfo personInfo = personInfoService.getPersonInfoById(user.getUserId());
			// get award info by awardId
			Award award = awardService.getAwardById(awardId);
			userAwardMap.setUser(personInfo);
			userAwardMap.setAward(award);
			Shop shop = new Shop();
			shop.setShopId(award.getShopId());
			userAwardMap.setShop(shop);
			// set the points
			userAwardMap.setPoint(award.getPoint());
			userAwardMap.setCreateTime(new Date());
			// set the redeem useStatus to used
			userAwardMap.setUsedStatus(1);
		}
		return userAwardMap;
	}

}

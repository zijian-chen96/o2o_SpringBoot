package com.imooc.o2o.web.shopadmin;

import java.util.List;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@RestController
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {

	@Autowired
	private ShopAuthMapService shopAuthMapService;
	
	@Autowired
	private PersonInfoService personInfoService;
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	@RequestMapping(value = "/listshopauthmapsbyshop", method = RequestMethod.GET)
	private Map<String, Object> listShopAuthMapByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		// get shop info from session
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		
		// null checker
		if((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// get shop auth info and split to pages
			ShopAuthMapExecution se = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(), pageIndex, pageSize);
			
			modelMap.put("shopAuthMapList", se.getShopAuthMapList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopauthmapbyid", method = RequestMethod.GET)
	private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// null checker
		if(shopAuthId != null && shopAuthId > -1) {
			// user front-end input shopAuthId to search the authInfo
			ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
			
			modelMap.put("shopAuthMap", shopAuthMap);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopAuthId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/modifyshopauthmap", method = RequestMethod.POST)
	private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// use it during edit auth, and delete/renew auth action
		// if edit auth needs verify code, others no need for verify
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		
		// verify code
		if(!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code");
			return modelMap;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		ShopAuthMap shopAuthMap = null;
		
		try {
			// convert the front-end input json string to shopAuthMap object
			shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		// null checker
		if(shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
			try {
				// check the user is the shop owner or not, shopOwner not support modify himself
				if(!checkPermission(shopAuthMap.getShopAuthId())) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "can not edit shop owner(already highest privilege)");
				}
				
				ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
				if(se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
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
			modelMap.put("errMsg", "please enter the auth information");
		}
		
		return modelMap;
	}	
	
	private boolean checkPermission(Long shopAuthId) {
		ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
		if(grantedPerson.getTitleFlag() == 0) {
			// if is shop owner, not allow to action
			return false;
		} else {
			return true;
		}
	}
	
	// get Wechat userInfo api prefix
	private static String urlPrefix;
	
	// get Wechat userInfo api middle
	private static String urlMiddle;
	
	// get Wechat userInfo api suffix
	private static String urlSuffix;
	
	// response from Wechat add auth info url
	private static String authUrl;

	@Value("${wechat.prefix}")
	public void setUrlPrefix(String urlPrefix) {
		ShopAuthManagementController.urlPrefix = urlPrefix;
	}
	
	@Value("${wechat.middle}")
	public void setUrlMiddle(String urlMiddle) {
		ShopAuthManagementController.urlMiddle = urlMiddle;
	}
	
	@Value("${wechat.suffix}")
	public void setUrlSuffix(String urlSuffix) {
		ShopAuthManagementController.urlSuffix = urlSuffix;
	}

	@Value("${wechat.auth.url}")
	public void setAuthUrl(String authUrl) {
		ShopAuthManagementController.authUrl = authUrl;
	}
	
	/**
	 * generated out QR code, Wechat scan it will link its URL inside
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/generateqrcode4shopauth", method = RequestMethod.GET)
	private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {
		// get session from current shop
		Shop shop = (Shop) request.getSession().getAttribute("currentShop");
		
		if(shop != null && shop.getShopId() != null) {
			// get current timeStamp, to make sure QR Code is usable, unit is ms
			long timeStamp = System.currentTimeMillis();
			
			// set shopId and timeStamp to content, and set into state, once Wechat got those info it will response to add auth
			// add aaa is for adding info to replace those info to use
			String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timeStamp + "}";
			
			try {
				// encode the content to base64 avoid special character, then concat URL
				String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
				
				// convert longURL to shortURL
				String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
				
				// generate QR code util tool, input shortURL, generate QR Code
				BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
				
				// send QR code image stream to front-end
				MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * add shop auth info by Wechat responses
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/addshopauthmap", method = RequestMethod.GET)
	private String addShopAuthMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get wechat user info from request
		WechatAuth auth = getEmployeeInfo(request);
		
		if(auth != null) {
			// get user info from userId
			PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
			
			// add userInfo to user
			request.getSession().setAttribute("user", user);
			
			// decode Wechat's response state
			String qrCodeinfo = new String(URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			
			try {
				// replace include aaa prefix content, convert to WechatInfo object
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			
			// verify QR Code status time
			if(!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			
			// avoid repeat add auth info
			ShopAuthMapExecution allMapList = shopAuthMapService.listShopAuthMapByShopId(wechatInfo.getShopId(), 1, 999);
			List<ShopAuthMap> shopAuthList = allMapList.getShopAuthMapList();
			for(ShopAuthMap sm : shopAuthList) {
				if(sm.getEmployee().getUserId() == user.getUserId())
					return "shop/operationfail";
			}
 			
			try {
				// add Wechat authInfo
				ShopAuthMap shopAuthMap = new ShopAuthMap();
				Shop shop = new Shop();
				shop.setShopId(wechatInfo.getShopId());
				shopAuthMap.setShop(shop);			
				shopAuthMap.setEmployee(user);
				shopAuthMap.setTitle("employee");
				shopAuthMap.setTitleFlag(1);
				ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
				
				if(se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
					return "shop/operationsuccess";
				} else {
					return "shop/operationfail";
				}
			} catch (RuntimeException e) {
				return "shop/operationfail";
			}
		}
		
		return "shop/operationfail";
	}
	
	/**
	 * get userInfo from Wechat response's code
	 * 
	 * @param request
	 * @return
	 */
	private WechatAuth getEmployeeInfo(HttpServletRequest request) {
		String code = request.getParameter("code");
		WechatAuth auth = null;
		
		if(code != null) {
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
	 * check QR Code's createTime to determine is it over 10 mins, over 10 mins means expired
	 * 
	 * @param wechatInfo
	 * @return
	 */
	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if(wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
			long nowTime = System.currentTimeMillis();
			if((nowTime - wechatInfo.getCreateTime()) <= 600000) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	
}

package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.EchartSeries;
import com.imooc.o2o.dto.EchartXAxis;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.dto.WechatInfo;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.service.ProductSellDailyService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;

@RestController
@RequestMapping("/shopadmin")
public class UserProductManagementController {

	@Autowired
	private UserProductMapService userProductMapService;

	@Autowired
	private ProductSellDailyService productSellDailyService;

	@Autowired
	private WechatAuthService wechatAuthService;

	@Autowired
	private ShopAuthMapService shopAuthMapService;

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/listuserproductmapbyshop", method = RequestMethod.GET)
	private Map<String, Object> listUserProductMapByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		// get current shop info
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

		// null checker, to make sure shopId not empty
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// add search condition
			UserProductMap userProductMapCondition = new UserProductMap();
			userProductMapCondition.setShop(currentShop);
			String productName = HttpServletRequestUtil.getString(request, "productName");

			if (productName != null) {
				// if front-end want search by fuzzy name, then needs input productName
				Product product = new Product();
				product.setProductName(productName);
				userProductMapCondition.setProduct(product);
			}

			// get product sells by input condition
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

	@RequestMapping(value = "/listproductselldailyinfobyshop", method = RequestMethod.GET)
	private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// get current shop info
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

		// null checker, shopId cannot empty
		if ((currentShop != null) && (currentShop.getShopId() != null)) {
			// add search condition
			ProductSellDaily productSellDailyCondition = new ProductSellDaily();
			productSellDailyCondition.setShop(currentShop);
			Calendar calendar = Calendar.getInstance();

			// get last day time
			calendar.add(Calendar.DATE, -1);
			Date endTime = calendar.getTime();

			// get 7 day before time
			calendar.add(Calendar.DATE, -6);
			Date beginTime = calendar.getTime();

			// search the product sells by input condition
			List<ProductSellDaily> productSellDailyList = productSellDailyService
					.listProductSellDaily(productSellDailyCondition, beginTime, endTime);

			// set the Date format
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			// product list, make sure unique
			HashSet<String> legendData = new HashSet<String>();

			// x-Axis data
			HashSet<String> xData = new HashSet<String>();

			// series data
			List<EchartSeries> series = new ArrayList<EchartSeries>();

			// daily sells list
			List<Integer> totalList = new ArrayList<Integer>();

			// currentProductName, default empty
			String currentProductName = "";
			for (int i = 0; i < productSellDailyList.size(); i++) {
				ProductSellDaily productSellDaily = productSellDailyList.get(i);

				// avoid duplicate
				legendData.add(productSellDaily.getProduct().getProductName());
				xData.add(sdf.format(productSellDaily.getCreateTime()));

				if (!currentProductName.equals(productSellDaily.getProduct().getProductName())
						&& !currentProductName.isEmpty()) {
					// if currentProductNam not equals to got productName, or it loop to end of list
					// if loop to next product, needs store the data into series
					// it include productName and product sells
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);

					// reset totalList
					totalList = new ArrayList<Integer>();

					// change currentProductId to current productId
					currentProductName = productSellDaily.getProduct().getProductName();

					// continue add new value
					totalList.add(productSellDaily.getTotal());
				} else {
					// if still under the same productId it will continue add new value
					totalList.add(productSellDaily.getTotal());
					currentProductName = productSellDaily.getProduct().getProductName();
				}

				if (i == productSellDailyList.size() - 1) {
					EchartSeries es = new EchartSeries();
					es.setName(currentProductName);
					es.setData(totalList.subList(0, totalList.size()));
					series.add(es);
				}
			}

			modelMap.put("series", series);
			modelMap.put("legendData", legendData);

			// concat xAxis
			List<EchartXAxis> xAxis = new ArrayList<EchartXAxis>();
			EchartXAxis exa = new EchartXAxis();
			exa.setData(xData);
			xAxis.add(exa);

			modelMap.put("xAxis", xAxis);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}

		return modelMap;
	}

	@RequestMapping(value = "/adduserproductmap", method = RequestMethod.GET)
	private String addUserProductMap(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get Wechat auth info
		WechatAuth auth = getOperatorInfo(request);
		
		if (auth != null) {
			PersonInfo operator = auth.getPersonInfo();
			request.getSession().setAttribute("user", operator);
			// get QRCode's state content data and decode the information
			String qrCodeinfo = new String(
					URLDecoder.decode(HttpServletRequestUtil.getString(request, "state"), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			WechatInfo wechatInfo = null;
			
			try {
				// replace the aaa to \ after decode the QRCode, and convert it to WechatInfo object class
				wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
			} catch (Exception e) {
				return "shop/operationfail";
			}
			
			// check the QRCode and verify the time is expired or not
			if (!checkQRCodeInfo(wechatInfo)) {
				return "shop/operationfail";
			}
			
			// get purchase record value and create UserProductMap instance
			Long productId = wechatInfo.getProductId();
			Long customerId = wechatInfo.getCustomerId();
			UserProductMap userProductMap = compactUserProductMap4Add(customerId, productId, auth.getPersonInfo());
			
			// null checker
			if (userProductMap != null && customerId != -1) {
				try {
					if (!checkShopAuth(operator.getUserId(), userProductMap)) {
						return "shop/operationfail";
					}
					// add purchase history record
					UserProductMapExecution se = userProductMapService.addUserProductMap(userProductMap);
					if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
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
	 * get UserAccessToken from code, and from token to get openId Wechat user info
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
	 *  to check the QRCode createTime and determine did it over 10 minutes
	 *  if over 10 minutes means expired
	 * 
	 * @param wechatInfo
	 * @return
	 */
	private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
		if (wechatInfo != null && wechatInfo.getProductId() != null && wechatInfo.getCustomerId() != null
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
	 * create user purchase history record by input customerId, productId, and operator info
	 * 
	 * @param customerId
	 * @param productId
	 * @param operator
	 * @return
	 */
	private UserProductMap compactUserProductMap4Add(Long customerId, Long productId, PersonInfo operator) {
		UserProductMap userProductMap = null;
		if (customerId != null && productId != null) {
			userProductMap = new UserProductMap();
			PersonInfo customer = new PersonInfo();
			customer.setUserId(customerId);
			// to get product points
			Product product = productService.getProductById(productId);
			userProductMap.setProduct(product);
			userProductMap.setShop(product.getShop());
			userProductMap.setUser(customer);
			userProductMap.setPoint(product.getPoint());
			userProductMap.setCreateTime(new Date());
			userProductMap.setOperator(operator);
		}
		return userProductMap;
	}

	/**
	 * check the scanner operator have action privileges or not
	 * 
	 * @param userId
	 * @param userProductMap
	 * @return
	 */
	private boolean checkShopAuth(long userId, UserProductMap userProductMap) {
		// get the shop's all auth info
		ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService
				.listShopAuthMapByShopId(userProductMap.getShop().getShopId(), 1, 1000);
		for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
			// to check the operator privileges
			if (shopAuthMap.getEmployee().getUserId() == userId) {
				return true;
			}
		}
		return false;
	}

}

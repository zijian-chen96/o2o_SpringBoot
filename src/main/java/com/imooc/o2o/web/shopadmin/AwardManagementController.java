package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@RestController
@RequestMapping("/shopadmin")
public class AwardManagementController {

	@Autowired
	private AwardService awardService;

	/**
	 * get the awards info by input the shopId
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listawardbyshop", method = RequestMethod.GET)
	private Map<String, Object> listAwardByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// get page info
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		// get shopId from session
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

		// null checker
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// if there is awardName get from front-end request
			String awardName = HttpServletRequestUtil.getString(request, "awardName");

			// combine the searching condition
			Award awardCondition = compactAwardCondition4Search(currentShop.getShopId(), awardName);

			// input same search condition get the total number
			AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
			modelMap.put("awardList", ae.getAwardList());
			modelMap.put("count", ae.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}

	/**
	 * get awards info by awardId
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getawardbyid", method = RequestMethod.GET)
	private Map<String, Object> getAwardbyId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// get awardId from request
		long awardId = HttpServletRequestUtil.getLong(request, "awardId");

		// null checker
		if (awardId > -1) {
			// input awardId and return award info
			Award award = awardService.getAwardById(awardId);
			modelMap.put("award", award);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty awardId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addaward", method = RequestMethod.POST)
	private Map<String, Object> addAward(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// verify code
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code");
			return modelMap;
		}

		// accept the data from front-end，include award，viewImage
		ObjectMapper mapper = new ObjectMapper();
		Award award = null;
		String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
		ImageHolder thumbnail = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		
		// our request have multi words, and interceptor is use to block the outside
		// image steam, we have image null checker
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			// convert the awardStr to award object
			award = mapper.readValue(awardStr, Award.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		// null checker
		if (award != null && thumbnail != null) {
			try {
				// get currentShop from session
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				award.setShopId(currentShop.getShopId());
				
				// add award
				AwardExecution ae = awardService.addAward(award, thumbnail);
				if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter award information");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyaward", method = RequestMethod.POST)
	private Map<String, Object> modifyAward(HttpServletRequest request) {
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// determine if it need verify code or not
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code");
			return modelMap;
		}

		// accept the date from front-end, include awards and viewImage
		ObjectMapper mapper = new ObjectMapper();
		Award award = null;
		ImageHolder thumbnail = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		// our request have multi words, and interceptor is use to block the outside
		// image steam, we have image null checker
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}

		try {
			String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
			// convert the front-end string to product object
			award = mapper.readValue(awardStr, Award.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}

		// null checker
		if (award != null) {
			try {
				// get the shopId from session and set to award
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				award.setShopId(currentShop.getShopId());
				AwardExecution pe = awardService.modifyAward(award, thumbnail);
				if (pe.getState() == AwardStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter award info");
		}
		return modelMap;
	}

	/**
	 * combine the search condition to award object
	 * 
	 * @param shopId
	 * @param awardName
	 * @return
	 */
	private Award compactAwardCondition4Search(long shopId, String awardName) {
		Award awardCondition = new Award();
		awardCondition.setShopId(shopId);
		if (awardName != null) {
			awardCondition.setAwardName(awardName);
		}
		return awardCondition;
	}

	/**
	 * image handler
	 * 
	 * @param request
	 * @param thumbnail
	 * @param productImgList
	 * @return
	 * @throws IOException
	 */
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// get image stream from request and create ImageHolder object and return
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if (thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		}
		return thumbnail;
	}

}

package com.imooc.o2o.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private HeadLineService headLineService;
	
	/**
	 * initial front-end UI system, include get root shopCategory and headLineList
	 * 
	 * @return
	 */
	@RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listMainPageInfo() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			// get root shopCategoryList(parentId is null)
			shopCategoryList = shopCategoryService.getShopCategoryList(null);
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			// get status 1(enable to use) headLine
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getheadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
			
		modelMap.put("success", true);
		return modelMap;
	}
	
}

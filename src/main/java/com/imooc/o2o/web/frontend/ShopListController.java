package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * return productList shopCategoryList(level 2 or level 1), and areaList
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// request parentId from front-end
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		
		if(parentId != -1) {
			// if parentId exist, then token out all level 2 ShopCategory list 
			// under the level 1 ShopCategory
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			try {
				// if parentId doesn't exist, then token out all 
				// level 1 ShopCategory(use choose all shopList on the main page)
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			} catch(Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		
		try {
			// request areaList info
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}		
		return modelMap;
	}
	
	/**
	 * get shopList by setting search role
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// get pageIndex
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		
		// get number of shows on a single page
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		// null checker
		if((pageIndex > -1) && (pageSize > -1)) {
			// try to get level 1 categoryId
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			
			// try to get level 2 categoryId
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			
			// try to get areaId
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			
			// try to get name for fuzzy search
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			
			// set all conditions got above and use to do searching
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			
			// use the condition and (pageIndex & pageSize) to request the shopList and return total numbers
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	
	/**
	 * combination of search role, it will take all exist role, and
	 * store into ShopCondition object and return the object type
	 * 
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		
		if(parentId != -1L) {
			// search level 2 ShopCategory all shopList under the level 1 ShopCategory
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		if(shopCategoryId != -1L) {
			// search all shopList under the level 2 ShopCategory
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if(areaId != -1) {
			// search all shopList under the areaId
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		if(shopName != null) {
			// search all shopList under the shopName
			shopCondition.setShopName(shopName);
		}
		
		// front-end only shows the shops are permit to show status = 1
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
	
}

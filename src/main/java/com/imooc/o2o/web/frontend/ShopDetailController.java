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

import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * get shopInfo and the productCategory under the shop
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// get shopId from the front-end
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		
		if(shopId != -1) {
			// get shopInfo with shopId
			shop = shopService.getByShopId(shopId);
			
			// get productCategory under the shop
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			modelMap.put("shop", shop);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		
		return modelMap;
	}
	
	/**
	 * depends on the searching condition display products 
	 * by pageSize/pageIndex and its under the shop
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/listproductsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// get pageIndex
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		
		// get pageSize number of display on a single page
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		// get shopId
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		
		// null checker
		if((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
			// get the productCategoryId
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			
			// get the fuzzy productName use for searching condition
			String productName = HttpServletRequestUtil.getString(request, "productName");
			
			// compact the searching condition together
			Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
			
			// use the searching condition to do search and will return 
			// the productList and total number, and its display by pageIndex/pageSize
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "pageIndex, pageSize, or shopId is empty");
			return modelMap;
		}
		
		return modelMap;
	}

	/**
	 * to compact the search condition together and use to do the searching
	 * 
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		
		if(productCategoryId != -1L) {
			// search all products under the productCategory
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		if(productName != null) {
			// search the products its name that include productName
			productCondition.setProductName(productName);
		}
		
		productCondition.setEnableStatus(1);
		return productCondition;
	}
	
	
}

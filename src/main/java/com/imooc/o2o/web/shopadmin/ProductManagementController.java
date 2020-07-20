package com.imooc.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	// max support product images
	private static final int IMAGEMAXCOUNT = 6;

	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// check the verify code
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "wrong verify code!");
			return modelMap;
		}

		// default the variables for accept the data from front-end,
		// include product, view images, and detail images
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		try {
			// if the request include files, then token out the files(view images and detail
			// images)
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handImage(request, thumbnail, productImgList);

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "upload image cannot be empty");
				return modelMap;
			}

		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}

		try {
			// get the string from front-end, and convert it to Product object
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}

		// if product, view image, and detail image list is not null, then will start
		// adding product to db
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {
				// to get current shopId from the session, then set it to product
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);

				// process the query to add product to db
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);

				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter the product info!");
		}

		return modelMap;
	}

	private ImageHolder handImage(HttpServletRequest request, ImageHolder thumbnail,List<ImageHolder> productImgList) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		// token out the view images and create ImageHolder object
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
		if(thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		}
		
		// token out the detail images and create List<ImageHolder> object, max support 6 images
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
					.getFile("productImg" + i);

			if (productImgFile != null) {
				// if i-th product image file is not null, then add to List
				ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(productImg);

			} else {
				// if i-th product image file is null, then break out the loop
				break;
			}
		}
		return thumbnail;
	}

	/**
	 * get product info by use productId
	 * 
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// null checker
		if (productId > -1) {
			// get product info
			Product product = productService.getProductById(productId);

			// get current shop's product category list
			List<ProductCategory> productCategoryList = productCategoryService
					.getProductCategoryList(product.getShop().getShopId());

			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}

	/**
	 * modify product
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();

		// check is it modify product or show/not show the product
		// if is modify product needs verify code, other don't need verify code
		boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");

		// check verify code
		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "worng verify code!");
			return modelMap;
		}

		// accept the data from front-end, include product info, view image, and detail
		// images
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		// if is file, token out the view images and detail images
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handImage(request, thumbnail, productImgList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		try {
			String productStr = HttpServletRequestUtil.getString(request, "productStr");
			// try to get data from front-end and convert it to Product object
			product = mapper.readValue(productStr, Product.class);
		} catch(Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		// check null
		if(product != null) {
			try {
				// get current shopId and store to product from session
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				
				// start modify product info
				ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
				if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.toString());
				}
			} catch(RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "please enter product info");
		}
		return modelMap;
	}
	
	/**
	 * use shopId to get all products under this shop
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductListByShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		// get pageIndex from front-end
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// get pageSize form front-end
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		// get shop info from session
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		
		// null checker
		if((pageIndex > -1) && (pageSize > -1) && (currentShop != null) && (currentShop.getShopId() != null)) {
			// get the search condition, include search info on some shop's product list
			// sort out the search condition
			long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
			String productName = HttpServletRequestUtil.getString(request, "productName");
			Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);
			
			// get the product list and total number by insert the search condition and pageIndex, pageSize
			ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);	
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		
		return modelMap;
	}
	
	/**
	 * store the product search condition into Product object
	 * 
	 * @param shopId
	 * @param productCategoryId
	 * @param productName
	 * @return
	 */
	private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(shopId);
		productCondition.setShop(shop);
		
		// if there is categroyId then insert into
		if(productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);		
		}
		
		// if there is productName then insert into
		if(productName != null) {
			productCondition.setProductName(productName);
		}
		
		return productCondition;
	}

}

package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private String index() {
		return "frontend/index";
	}

	@RequestMapping(value = "shoplist", method = RequestMethod.GET)
	private String showShopList() {
		return "frontend/shoplist";
	}

	@RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
	private String showShopDetail() {
		return "frontend/shopdetail";
	}

	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	private String showProductDetail() {
		return "frontend/productdetail";
	}
	
	/**
	 * shop award list web-page routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/awardlist", method = RequestMethod.GET)
	private String showAwardList() {
		return "frontend/awardlist";
	}
	
	/**
	 * award redeem list web-page routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
	private String showPointRecord() {
		return "frontend/pointrecord";
	}
	
	/**
	 * award detail web-page routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
	private String showMyAwardDetail() {
		return "frontend/myawarddetail";
	}
	
	/**
	 * purchase history web-page routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/myrecord", method = RequestMethod.GET)
	private String showMyRecord() {
		return "frontend/myrecord";
	}
	
	/**
	 * user points on the shops web-page routing
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mypoint", method = RequestMethod.GET)
	private String showMyPoint() {
		return "frontend/mypoint";
	}
	
}

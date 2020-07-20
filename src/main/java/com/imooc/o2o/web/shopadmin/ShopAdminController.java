package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="shopadmin", method= {RequestMethod.GET})
public class ShopAdminController {
	
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		// go to shop operation web-site
		return "shop/shopoperation";
	}
	
	@RequestMapping(value="/shoplist")
	public String shopList() {
		// go to shop list web-site
		return "shop/shoplist";
	}
	
	@RequestMapping(value="/shopmanagement")
	public String shopManagement() {
		// go to shop management web-site
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value="/productcategorymanagement")
	public String productCategoryManagement() {
		// go to product category management web-site
		return "shop/productcategorymanagement";
	}
	
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		// go to product add/modify web-site
		return "shop/productoperation";
	}
	
	@RequestMapping(value="/productmanagement")
	public String productManagement() {
		// go to product management web-site
		return "shop/productmanagement";
	}
	
	@RequestMapping(value = "/shopauthmanagement")
	public String shopAuthManagement() {
		// go to shop auth web-site
		return "shop/shopauthmanagement";
	}
	
	@RequestMapping(value = "/shopauthedit")
	public String shopAuthEdit() {
		// go to auth info modify web-site
		return "shop/shopauthedit";
	}
	
	@RequestMapping(value = "/operationsuccess")
	public String operationSuccess() {
		// go to operation success web-site
		return "shop/operationsuccess";
	}
	
	@RequestMapping(value = "/operationfail")
	public String operationFail() {
		// go to operation fail web-site
		return "shop/operationfail";
	}
	
	@RequestMapping(value = "/productbuycheck")
	public String productBuyCheck() {
		// go to shop order history web-site
		return "shop/productbuycheck";
	}
	
	@RequestMapping(value = "/awardmanagement")
	public String awardManagement() {
		// go to award management web-site
		return "shop/awardmanagement";
	}
	
	@RequestMapping(value = "/awardoperation")
	public String awardEdit() {
		// go to award modify web-site
		return "shop/awardoperation";
	}
	
	@RequestMapping(value = "/usershopcheck")
	public String userShopCheck() {
		// go to user points web-site
		return "shop/usershopcheck";
	}
	
	@RequestMapping(value = "/awarddelivercheck")
	public String awardDeliverCheck() {
		// go to shop user points awards web-site
		return "shop/awarddelivercheck";
	}
	
}

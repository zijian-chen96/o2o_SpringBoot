package com.imooc.o2o.interceptor.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.imooc.o2o.entity.Shop;

/**
 * shop management system verify interceptor
 * 
 * @author chen
 *
 */
public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * preHandle, user doing action before do the interceptor, 
	 * modify the preHandle logic to intercept the user action
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// get currentShop info from session
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		
		
		// from sessino get shoplist under the user 
		@SuppressWarnings("unchecked")
		List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
		
		if(currentShop != null && shopList != null) {
			// for loop the shoplist
			for(Shop shop : shopList) {
				// if currentShop in the shopList then return true, which means pass the verify allow the user continue action
				 if(shop.getShopId() == currentShop.getShopId()) {
					 return true;
				 }
			}
		}
		
		// if not pass login verify return false, and stop user action
		return false;
	}
	
}

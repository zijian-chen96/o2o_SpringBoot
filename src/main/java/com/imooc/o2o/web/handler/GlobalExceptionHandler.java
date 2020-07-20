package com.imooc.o2o.web.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.exceptions.AreaOperationException;
import com.imooc.o2o.exceptions.HeadLineOperationException;
import com.imooc.o2o.exceptions.LocalAuthOperationException;
import com.imooc.o2o.exceptions.ProductCategoryOperationException;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.exceptions.ShopAuthMapOperationException;
import com.imooc.o2o.exceptions.ShopCategoryOperationException;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.exceptions.WechatAuthOperationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, Object> handle(Exception e) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("success", false);

		if (e instanceof AreaOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof HeadLineOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof LocalAuthOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof ProductCategoryOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof ProductOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof ShopAuthMapOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof ShopCategoryOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof ShopOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else if (e instanceof WechatAuthOperationException) {
			modelMap.put("errMsg", e.getMessage());
		} else {
			log.error("system error: ", e.getMessage());
			modelMap.put("errMsg", "unknown error, please contact IT support");
		}

		return modelMap;
	}

}

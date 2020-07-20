package com.imooc.o2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.exceptions.ShopCategoryOperationException;
import com.imooc.o2o.service.ShopCategoryService;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

	@Autowired
	private ShopCategoryDao shopCategoryDao;

	@Autowired
	private JedisUtil.Keys jedisKeys;

	@Autowired
	private JedisUtil.Strings jedisStrings;

	private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

	@Override
	@Transactional
	public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {
		// set redis key
		String key = SCLISTKEY;

		// set the accept object
		List<ShopCategory> shopCategoryList = null;

		// JSON data converter
		ObjectMapper mapper = new ObjectMapper();

		// concat the Redis key
		if (shopCategoryCondition == null) {
			// if search condition is empty, then list out all first level category,
			// parentId is null
			key = key + "_allfirstlevel";
		} else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
				&& shopCategoryCondition.getParent().getShopCategoryId() != null) {
			// if parentId is not null, then list out all child category under this parentId
			key = key + "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
		} else if (shopCategoryCondition != null) {
			// list out all child category, all category
			key = key + "_allsecondlevel";
		}

		// check is the key exists
		if (!jedisKeys.exists(key)) {
			// if not exists, then get from database
			shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);

			// convert object to String, and store into Redis corresponding key
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(shopCategoryList);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}

			jedisStrings.set(key, jsonString);

		} else {
			// if key exists, then get data from Redis server
			String jsonString = jedisStrings.get(key);
			
			// set the convert type for the string
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
			try {
				// convert the value string to object set
				shopCategoryList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new ShopCategoryOperationException(e.getMessage());
			}
		}
		return shopCategoryDao.queryShopCategory(shopCategoryCondition);
	}

}

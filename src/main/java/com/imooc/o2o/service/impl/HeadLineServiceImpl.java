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
import com.imooc.o2o.dao.HeadLineDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.exceptions.HeadLineOperationException;
import com.imooc.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService{
	
	@Autowired
	private HeadLineDao headLineDao;
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Autowired
	private JedisUtil.Strings jedisStrings;
		
	private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);
	
	@Override
	@Transactional
	public List<HeadLine> getheadLineList(HeadLine headLineCondition) throws IOException {
		// set redis key
		String key = HLLISTKEY;
		
		// set the accept object
		List<HeadLine> headLineList = null;
		
		// JSON data converter
		ObjectMapper mapper = new ObjectMapper();
		
		// concat the Redis key
		if(headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		
		// check the key is it exists
		if(!jedisKeys.exists(key)) {
			// if not exists, then needs go to database get data.
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			
			// convert object to String, and store into Redis corresponding key
			String jsonString;
			try {
				jsonString = mapper.writeValueAsString(headLineList);
			} catch(JsonProcessingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
			
			jedisStrings.set(key, jsonString);
			
		} else {
			// if key exists, then get data from Redis server
			String jsonString = jedisStrings.get(key);
			
			// set the convert type for the string
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try {
				// convert the value string to object set
				headLineList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
	}

}

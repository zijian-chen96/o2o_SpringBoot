package com.imooc.o2o.service;

public interface CacheService {
	
	/**
	 * delete all match key-value data, if input shopcategory, 
	 * then shopcategory_* will delete all value start with shopcategory_
	 * 
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);

}

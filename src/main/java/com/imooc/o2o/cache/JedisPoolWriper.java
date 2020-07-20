package com.imooc.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWriper {
	// Redis connection pool obejct
	private JedisPool jedisPool;
	
	public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host, final int port) {
		
		try {
			jedisPool = new JedisPool(poolConfig, host, port);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get Redis connection pool object
	 * 
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	/**
	 * set the Redis connection pool object
	 * 
	 * @param jedisPool
	 */
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
}

package com.imooc.o2o.cache;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.SafeEncoder;

public class JedisUtil {
	// operating key
	public Keys KEYS;
	
	// use to dealing String type
	public Strings STRINGS;
	
	// Redis connection pool object
	private JedisPool jedisPool;
	
	/**
	 * get redis connection pool
	 * 
	 * @return
	 */
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
	/**
	 * set the redis connection pool
	 * 
	 * @param jedisPoolWriper
	 */
	public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
		this.jedisPool = jedisPoolWriper.getJedisPool();
	}
	
	/**
	 * get jedis object from jedis connection pool
	 * 
	 * @return
	 */
	public Jedis getJedis() {
		return jedisPool.getResource();
	}
	
	
	//************ Keys ************//
	public class Keys{
		
		/**
		 * clear all key
		 */
		public String flushAll() {
			Jedis jedis = getJedis();
			String stata = jedis.flushAll();
			jedis.close();
			return stata;
		}
		
		/**
		 * delete keys value, and it can have many keys
		 * 
		 * @param String
		 * 		... keys
		 * @return delete record number
		 */
		public long del(String... keys) {
			Jedis jedis = getJedis();
			long count = jedis.del(keys);
			jedis.close();
			return count;
		}
		
		/**
		 * check is the key exists or not
		 * 
		 * @param String
		 * 			key
		 * @return boolean
		 */
		public boolean exists(String key) {
			try {
				// ShardedJedis sjedis = getShardedJedis();
				Jedis sjedis = getJedis();
				boolean exis = sjedis.exists(key);
				sjedis.close();
				return exis;
			} catch (Exception e) {
				return false;
			}
			
		}
		
		/**
		 * search the pattern keys
		 * 
		 * @param String
		 * 			key regex, * multiple, ? one
		 * @return
		 */
		public Set<String> keys(String pattern) {
			Jedis jedis = getJedis();
			Set<String> set = jedis.keys(pattern);
			jedis.close();
			return set;
		}
	}
	
	
	//************ Strings ************//
	public class Strings {
		
		/**
		 * get data by use key
		 * 
		 * @param key
		 * @return
		 */
		public String get(String key) {
			// ShardedJedis sjedis = getShardedJedis();
			Jedis sjedis = getJedis();
			String value = sjedis.get(key);
			sjedis.close();
			return value;
		}
		
		/**
		 * add data, if the data already have value in Redis, then needs recover the data
		 * 
		 * @param String
		 * 		key
		 * @param String
		 * 		value
		 * @return status code
		 */
		public String set(String key, String value) {
			return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
		}
		
		/**
		 * add data, the data needs be in byte array.
		 * if the data already have value in Redis, then needs recover the data.
		 * 
		 * @param byte[] 
		 * 		key
		 * @param byte[] 
		 * 		value
		 * @return status code
		 */
		public String set(byte[] key, byte[] value) {
			Jedis jedis = getJedis();
			String status = jedis.set(key, value);
			jedis.close();
			return status;
		}
	}
	
	
}

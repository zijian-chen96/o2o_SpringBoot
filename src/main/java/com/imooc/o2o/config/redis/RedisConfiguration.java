package com.imooc.o2o.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.cache.JedisPoolWriper;
import com.imooc.o2o.cache.JedisUtil;

import redis.clients.jedis.JedisPoolConfig;

/**
 * spring-redis.xml configuration
 * 
 * @author chen
 *
 */
@Configuration
public class RedisConfiguration {
	
	@Value("${redis.hostname}")
	private String hostname;
	
	@Value("${redis.port}")
	private int port;
	
	@Value("${redis.pool.maxActive}")
	private int maxTotal;
	
	@Value("${redis.pool.maxIdle}")
	private int maxIdle;
	
	@Value("${redis.pool.maxWait}")
	private long maxWaitMillis;
	
	@Value("${redis.pool.testOnBorrow}")
	private boolean testOnBorrow;
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
	@Autowired
	private JedisPoolWriper jedisWritePool;
	
	@Autowired
	private JedisUtil jedisUtil;
	
	
	/**
	 * create Redis connection pool configuration
	 * 
	 * @return
	 */
	@Bean(name = "jedisPoolConfig")
	public JedisPoolConfig createJedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		
		// control a pool can have how many jedis instances
		jedisPoolConfig.setMaxTotal(maxTotal);
		
		// connection pool maxIdle connections, here set 20
		// means no client use, the connection pool allow to keep 20 connections connecting
		jedisPoolConfig.setMaxIdle(maxIdle);
		
		// max wait time: when no can use connection
		// connection pool waiting for connect max wait time(unit ms), over time will throws exception
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		
		// check validation when get connection
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		
		return jedisPoolConfig;
	}
	
	/**
	 * create Redis connection pool, and set the configuration
	 * 
	 * @return
	 */
	@Bean(name = "jedisWritePool")
	public JedisPoolWriper createJedisPoolWriper() {
		JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
		return jedisPoolWriper;
	}
	
	/**
	 * create Redis tool util, contain the Redis connection and actions
	 * 
	 * @return
	 */
	@Bean(name = "jedisUtil")
	public JedisUtil createJedisUtil() {
		JedisUtil jedisUtil = new JedisUtil();
		jedisUtil.setJedisPool(jedisWritePool);
		return jedisUtil;
	}
	
	/**
	 * Redis Key action
	 * 
	 * @return
	 */
	@Bean(name = "jedisKeys")
	public JedisUtil.Keys createJedisKeys() {
		JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
		return jedisKeys;
	}
	
	/**
	 * Redis String action
	 * 
	 * @return
	 */
	@Bean(name = "jedisStrings")
	public JedisUtil.Strings createJedisStrings() {
		JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
		return jedisStrings;
	}
	
}

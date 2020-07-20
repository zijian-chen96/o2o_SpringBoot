package com.imooc.o2o.config.dao;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.imooc.o2o.util.DESUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * setting data source to IOC container
 * 
 * @author chen
 *
 */
@Configuration
// setting mybatis mapper scan address
@MapperScan("com.imooc.o2o.dao")
public class DataSourceConfiguration {
	
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	
	@Value("${jdbc.url}")
	private String jdbcUrl;
	
	@Value("${jdbc.username}")
	private String jdbcUsername;
	
	@Value("${jdbc.password}")
	private String jdbcPassword;

	/**
	 * generate spring-dao.xml bean dataSource
	 * @throws PropertyVetoException 
	 * 
	 */
	@Bean(name = "dataSource")
	public ComboPooledDataSource createDataSource() throws PropertyVetoException {
		// create a dataSource object
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		
		// set the data value like the setting file
		// driver
		dataSource.setDriverClass(jdbcDriver);
		
		// database connection URL
		dataSource.setJdbcUrl(jdbcUrl);
		
		// set username
		dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
		
		// set user password
		dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));
		
		// set the c3p0 connection pool
		// connection pool max pool size
		dataSource.setMaxPoolSize(30);
		
		// connection pool min pool size
		dataSource.setMinPoolSize(10);
		
		// connection pool initial pool size
		dataSource.setInitialPoolSize(10);
		
		// after close connection not auto commit 
		dataSource.setAutoCommitOnClose(false);
		
		// connection time out
		dataSource.setCheckoutTimeout(10000);
		
		// connection failed re-try times
		dataSource.setAcquireRetryAttempts(2);
		
		return dataSource;
	}
	
}

package com.imooc.o2o.config.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * corresponding to spring-service's transactionManager
 * implements TransactionManagementConfigurer is for annotation-driven
 * 
 * @author chen
 *
 */
@Configuration
// annotation @EnableTransactionManagement is for enable the supports
// at Service add @Transactional annotation
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {
	
	// inject DataSourceConfiguration's dataSource, use creteDataSource() to get
	@Autowired
	private DataSource dataSource;
	
	/**
	 * use for manage trasaction, needs return PlatformTransactionManager
	 */
	@Override
	public TransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}

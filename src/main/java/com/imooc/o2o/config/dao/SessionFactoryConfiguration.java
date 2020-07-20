package com.imooc.o2o.config.dao;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

@Configuration
public class SessionFactoryConfiguration {
	
	// mybatis-config.xml setting file address
	private static String mybatisConfigFile;
	@Value("${mybatis_config_file}")
	public void setMybatisConfigFile(String mybatisConfigFile) {
		SessionFactoryConfiguration.mybatisConfigFile = mybatisConfigFile;
	}
	
	// mybatis mapper file path
	private static String mapperPath;
	@Value("${mapper_path}")
	public void setMapperPath(String mapperPath) {
		SessionFactoryConfiguration.mapperPath = mapperPath;
	}

	// entity class package path
	@Value("${type_alias_package}")
	private String typeAliasPackage;
	
	@Autowired
	private DataSource dataSource;
	
	/**
	 * create sqlSessionFactoryBean object and set the configtion and mapper
	 * set datasource
	 * @throws IOException 
	 * 
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean createSqlSessionFactoryBean() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		
		// setting mybatis configuration scan path
		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigFile));
		
		// add mapper scan path
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();	
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperPath;
		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
		
		// set dataSource
		sqlSessionFactoryBean.setDataSource(dataSource);
		
		// set typeAlias package
		sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasPackage);
		
		return sqlSessionFactoryBean;
	}
	

}

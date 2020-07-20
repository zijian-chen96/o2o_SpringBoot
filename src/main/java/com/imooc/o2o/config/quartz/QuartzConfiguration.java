package com.imooc.o2o.config.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.imooc.o2o.service.ProductSellDailyService;

@Configuration
public class QuartzConfiguration {
	
	@Autowired
	private ProductSellDailyService productSellDailyService;
	
	@Autowired
	private MethodInvokingJobDetailFactoryBean jobDeatilFactory;
	
	@Autowired
	private CronTriggerFactoryBean productSellDailyTriggerFactory;
	
	/**
	 * create jobDeatilFactory and return
	 * 
	 * @return
	 */
	@Bean(name = "jobDeatilFactory")
	public MethodInvokingJobDetailFactoryBean createJobDeatil() {
		// new a JobDetailFactory object, create a jobDeatil
		// to create a trigger is to run a function, so use factory is much easy
		MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
		// set jobDetail name
		jobDetailFactoryBean.setName("product_sell_daily_job");
		// set jobDetail group name
		jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
		// if set up multiple triggers, it may cause when the trigger not finish first one and just start second trigger
		// so it need to set the concurrent to false, not allow concurrency program
		jobDetailFactoryBean.setConcurrent(false);
		// set the running target object
		jobDetailFactoryBean.setTargetObject(productSellDailyService);
		// set the running function
		jobDetailFactoryBean.setTargetMethod("dailyCalculate");
		
		return jobDetailFactoryBean;	
	}
	
	/**
	 * create cronTriggerFactory and return
	 * 
	 * @return
	 */
	@Bean("productSellDailyTriggerFactory")
	public CronTriggerFactoryBean createProductSellDailyTrigger() {
		// create triggerFactory instance for create trigger
		CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
		// set triggerFactory name
		triggerFactory.setName("product_sell_daily_trigger");
		// set triggerFactory group name
		triggerFactory.setGroup("job_product_sell_daily_group");
		// binding the jobDetail
		triggerFactory.setJobDetail(jobDeatilFactory.getObject());
		// set cron expression
		triggerFactory.setCronExpression("0 0 0 * * ? *");
		
		return triggerFactory;
	}
	
	/**
	 * create schedulerFactory and return
	 * 
	 * @return
	 */
	@Bean("schedulerFactory")
	public SchedulerFactoryBean createSchedulerFactory() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setTriggers(productSellDailyTriggerFactory.getObject());		
		return schedulerFactory;
	}
	
}

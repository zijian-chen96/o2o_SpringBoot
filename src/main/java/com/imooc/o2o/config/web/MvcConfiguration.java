package com.imooc.o2o.config.web;

import javax.servlet.ServletException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.imooc.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.imooc.o2o.interceptor.shopadmin.ShopPermissionInterceptor;

/**
 * start Mvc, auto inject spring container. WebMvcConfigurerAdapter: viewResovler
 * api: ApplicationContextAware, then easy to get ApplicationContext all beans
 * 
 * @author chen
 *
 */
@Configuration
// same to <mvc:annotation-driven>
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer, ApplicationContextAware {
	
	// spring container
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * static resource configuration
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resource/");
		registry.addResourceHandler("/upload/**").addResourceLocations("file:/Users/chen/projectimage/upload/");
	}
	
	/**
	 * default request handler
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	/**
	 * create viewResolver
	 * 
	 * @return
	 */
	@Bean(name = "viewResolver")
	public ViewResolver createViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		
		// set spring container
		viewResolver.setApplicationContext(this.applicationContext);
		
		// cancel cache
		viewResolver.setCache(false);
		
		// set resolver prefix
		viewResolver.setPrefix("/WEB-INF/html/");
		
		// set resolver suffix
		viewResolver.setSuffix(".html");
		
		return viewResolver;
	}
	
	/**
	 * file upload resolver
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver createMultipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("utf-8");
		// 1024 * 1024 * 20 = 20M
		multipartResolver.setMaxUploadSize(20971520);
		multipartResolver.setMaxInMemorySize(20971520);
		return multipartResolver;
	}
	
	/**
	 * Verify Code
	 */
	@Value("${kaptcha.border}")
	private String border;

	@Value("${kaptcha.textproducer.font.color}")
	private String fcolor;

	@Value("${kaptcha.image.width}")
	private String width;

	@Value("${kaptcha.textproducer.char.string}")
	private String cString;

	@Value("${kaptcha.image.height}")
	private String height;

	@Value("${kaptcha.textproducer.font.size}")
	private String fsize;

	@Value("${kaptcha.noise.color}")
	private String nColor;

	@Value("${kaptcha.textproducer.char.length}")
	private String clength;

	@Value("${kaptcha.textproducer.font.names}")
	private String fnames;
	
	/**
	 * because web.xml not works, so we need to configure Kaptcha verify code Servlet
	 * 
	 * @return
	 * @throws ServletException
	 */
	@Bean
	public ServletRegistrationBean<KaptchaServlet> servletRegistrationBean() throws ServletException {
		ServletRegistrationBean<KaptchaServlet> servlet = new ServletRegistrationBean<KaptchaServlet>(new KaptchaServlet(), "/Kaptcha");
		
		servlet.addInitParameter("kaptcha.border", border);// image border
		servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor); // text color
		servlet.addInitParameter("kaptcha.image.width", width);// image width
		servlet.addInitParameter("kaptcha.textproducer.char.string", cString);// which text generate verify code
		servlet.addInitParameter("kaptcha.image.height", height);// image height
		servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);// font size
		servlet.addInitParameter("kaptcha.noise.color", nColor);// effect color
		servlet.addInitParameter("kaptcha.textproducer.char.length", clength);// number of string
		servlet.addInitParameter("kaptcha.textproducer.font.names", fnames);// font style
		
		return servlet;
	}
	
	/**
	 * interceptor config
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String interceptPath = "/shopadmin/**";
		
		// regist interceptor
		InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
		
		// set interceptor path
		loginIR.addPathPatterns(interceptPath);
		
		/** shopauthmanagement page **/
		loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
		
		// also can regist other interceptor
		InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
		
		// set interceptor path
		permissionIR.addPathPatterns(interceptPath);
		
		// set not intercept path
		/** shoplist page **/
		permissionIR.excludePathPatterns("/shopadmin/shoplist");
		permissionIR.excludePathPatterns("/shopadmin/getshoplist");
		/** shopregister page **/
		permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
		permissionIR.excludePathPatterns("/shopadmin/registershop");
		permissionIR.excludePathPatterns("/shopadmin/shopoperation");
		/** shopmanage page **/
		permissionIR.excludePathPatterns("/shopadmin/shopmanagement");
		permissionIR.excludePathPatterns("/shopadmin/getshopmanagementinfo");
		/** shopauthmanagement page **/
		permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");
		
	}
	
}

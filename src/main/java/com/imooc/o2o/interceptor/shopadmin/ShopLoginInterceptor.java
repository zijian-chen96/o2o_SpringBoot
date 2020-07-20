package com.imooc.o2o.interceptor.shopadmin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.imooc.o2o.entity.PersonInfo;

/**
 * shop management system login verify interceptor
 * 
 * @author chen
 *
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * preHandle, user doing action before do the interceptor, 
	 * modify the preHandle logic to intercept the user action
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// get user info from session
		Object userObj = request.getSession().getAttribute("user");
		
		if(userObj != null) {
			// if user not empty, then convert the user info to PersonInfo object
			PersonInfo user = (PersonInfo) userObj;
			// null checker, to make sure userId is not null and account status is 1(legal account), also the user is shop owner
			if(user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1)
				return true;
		}
		
		// if not pass login verify, then goto the account login page
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<script>");
		out.println("window.open ('" + request.getContextPath() + "/local/login?usertype=2','_self')");
		out.println("</script>");
		out.println("</html>");
		
		return false;
	}

}

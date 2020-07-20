package com.imooc.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.wechat.WechatUtil;

/**
 * to get Wechat users info api, if on wechat visit 
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc6c83296454f9999&redirect_uri=http://119.23.215.109/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * this will get codo, and we can use code to get access_token, then to get users info
 * 
 * @author chen
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {
	
	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	
	@Autowired
	private PersonInfoService personInfoService;
	
	@Autowired
	private WechatAuthService wechatAuthService;
	
	private static final String FRONTEND = "1";
	
	private static final String SHOPEND = "2";
	
	@RequestMapping(value = "/logincheck", method = {RequestMethod.GET})
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("wechat login get...");
		
		// get the code from wechat, use code to get access_token, then get user info
		String code = request.getParameter("code");
		
		// the state can use to transfer the self-define info, easy for function call, at here we can ignore
		String roleType = request.getParameter("state");
		
		log.debug("wechat login code:" + code);
		
		WechatUser user = null;
		String openId = null;
		WechatAuth auth = null;
		
		
		if(code != null) {
			UserAccessToken token;
			
			try {
				// get access_token from code
				token = WechatUtil.getUserAccessToken(code);
				
				log.debug("wechat login toekn:" + token.toString());
				
				// get accessToken from token
				String accessToken = token.getAccessToken();
				
				// get openId from token
				openId = token.getOpenId();
				
				//get userInfo by use access_token and openId
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("wechat login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
				
			} catch(IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId:" + e.toString());
				e.printStackTrace();
			}
		}
	
		// if we get the openId, and we can go to database to check is the user already have account on our web-site
		// if is not, and we can auto create new one for the user
		if(auth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			
			if(FRONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			} else {
				personInfo.setUserType(2);
			}
			
			auth.setPersonInfo(personInfo);
			WechatAuthExecution we = wechatAuthService.register(auth);
			
			if(we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			} else {
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.getSession().setAttribute("user", personInfo);
			}
		}
		
		// if user click front-end then goto front-end system else goto shop manage system
		if(FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else {
			return "shopadmin/shoplist";
		}

	}
	
}

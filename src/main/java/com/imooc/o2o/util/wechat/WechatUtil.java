package com.imooc.o2o.util.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;

/**
 * wechat tool util
 * 
 * @author chen
 *
 */
public class WechatUtil {

	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	
	/**
	 * convert the WechatUser info to PersonInfo and return PersonInfo object
	 * 
	 * @param user
	 * @return
	 */
	public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex() + "");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		return personInfo;
	}

	/**
	 * to get UserAccessToken object class
	 * 
	 * @param code
	 * @return
	 */
	public static UserAccessToken getUserAccessToken(String code) throws IOException {
		// test account appId
		String appId = "wxc6c83296454f9999";
		log.debug("appId:" + appId);

		// test account appsecret
		String appsecret = "1d272929aa43da9d9f6061364b44c538";
		log.debug("secret:" + appsecret);

		// use the data from code to concat the wechat api URL
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
				+ "&code=" + code + "&grant_type=authorization_code";

		// sent request to the url and get token json string
		String tokenStr = httpsRequest(url, "GET", null);
		log.debug("userAccessToken:" + tokenStr);

		UserAccessToken token = new UserAccessToken();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// convert JSON string to object type
			token = objectMapper.readValue(tokenStr, UserAccessToken.class);
		} catch (JsonParseException e) {
			log.error("failed to ge user accessToken:" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("failed to ge user accessToken:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("failed to ge user accessToken:" + e.getMessage());
			e.printStackTrace();
		}

		if (token == null) {
			log.error("failed to ge user accessToken");
			return null;
		}

		return token;
	}

	public static WechatUser getUserInfo(String accessToken, String openId) {
		// user accessToken and openId to concat URL api to get userInfo
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
				+ "&lang=zh_CN";
		
		// visit the URL and get userInfo json string
		String userStr = httpsRequest(url, "GET", null);
		log.debug("user info:" + userStr);
		
		WechatUser user = new WechatUser();
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			// convert JSON string to object type
			user = objectMapper.readValue(userStr, WechatUser.class);
		} catch (JsonParseException e) {
			log.error("failed to ge user infomation:" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("failed to ge user infomation:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("failed to ge user infomation:" + e.getMessage());
			e.printStackTrace();
		}

		if (user == null) {
			log.error("failed to ge user infomation");
			return null;
		}

		return user;
	}
	
	/**
	 * send https request and get the response
	 * 
	 * 		request address
	 * @param requestUrl
	 * 		request method (GET, POST)
	 * @param requestMethod
	 * 		request data
	 * @param outputStr
	 * 
	 * @return JSON string
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		
		try {
			// create SSLContext object, and use us define trust manager
			TrustManager[] tm = {new MyX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			
			// from the above SSLContext object get SSLSocketFactory object
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			
			// setup the request method (GET, POST)
			httpUrlConn.setRequestMethod(requestMethod);
			
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();
			
			// if have data needs submit
			if(outputStr != null) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				
				// set the encoding format
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			
			// convert the return data to string
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			
			String str = null;
			while((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			// free the resource
			bufferedReader.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.debug("https buffer:" + buffer.toString());
		} catch(ConnectException ce) {
			log.error("wechat server connection timed out");
		} catch(Exception e) {
			log.error("https request error:{}", e);
		}
		
		return buffer.toString();
	}
	
	

}

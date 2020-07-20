package com.imooc.o2o.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatShortUrl;
import com.imooc.o2o.util.wechat.WechatUtil;

public class ShortNetAddressUtil {
	
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);
	
	static String CREATE_API = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";

	static String ACCESS_TOKEN = null;
			
	public static void main(String[] args) throws IOException {
		String longUrl = "https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login";
		
		System.out.println(getShortURL(longUrl));
		
	}
	
	public static String getShortURL(String longUrl) throws IOException {
		String params = "{\"action\":\"long2short\",\"long_url\":\"" + longUrl + "\"}";
		
		ACCESS_TOKEN = getUserAccessToken();
		
		// concat the full url
		String url = CREATE_API + ACCESS_TOKEN;
		
		// send the request to wechat server for getting the short url json string
		String tokenStr = WechatUtil.httpsRequest(url, "POST", params);
		
		// create instance to getting the elements from json string
		WechatShortUrl shortUrl = new WechatShortUrl();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// convert JSON string to object type
			shortUrl = objectMapper.readValue(tokenStr, WechatShortUrl.class);
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

		if (shortUrl == null) {
			log.error("failed to ge user accessToken");
			return null;
		}

		return shortUrl.getShortUrl();
	}
	
	public static String getUserAccessToken() {
		// test account appId
		String appId = "wxc6c83296454f9999";
		log.debug("appId:" + appId);

		// test account appsecret
		String appsecret = "1d272929aa43da9d9f6061364b44c538";
		log.debug("secret:" + appsecret);

		// use the data from code to concat the wechat api URL
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appsecret;

		// sent request to the url and get token json string
		String tokenStr = WechatUtil.httpsRequest(url, "GET", null);
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

		return token.getAccessToken();
	}

}

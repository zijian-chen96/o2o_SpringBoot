package com.imooc.o2o.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * user certificate token
 * 
 * @author chen
 *
 */
public class UserAccessToken {
	
	// get the access certification
	@JsonProperty("access_token")
	private String accessToken;
	
	// expire time, unit : second
	@JsonProperty("expires_in")
	private String expiresIn;
	
	// refresh the token, use to update and get the next visit token, not very useful in here
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	// the userId on this web-site, unique for the wechat account
	@JsonProperty("openid")
	private String openId;
	
	// privileges scope
	@JsonProperty("scope")
	private String scope;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
}

package com.imooc.o2o.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatShortUrl implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -764549503771142302L;

	@JsonProperty("errcode")
	private int code;
	
	@JsonProperty("errmsg")
	private String errMsg;
	
	@JsonProperty("LongUrl")
	private String longUrl;
	
	@JsonProperty("short_url")
	private String shortUrl;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

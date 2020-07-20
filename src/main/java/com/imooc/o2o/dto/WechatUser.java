package com.imooc.o2o.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * wechat user class
 * 
 * @author chen
 *
 */
public class WechatUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7007139907874311826L;
	
	// openId, unique id for the account
	@JsonProperty("openid")
	private String openId;
	
	// user nickname
	@JsonProperty("nickname")
	private String nickName;
		
	// sex
	@JsonProperty("sex")
	private String sex;
	
	// province
	@JsonProperty("province")
	private String province;
	
	// city
	@JsonProperty("city")
	private String city;
	
	// city area
	@JsonProperty("country")
	private String country;
	
	// head image url
	@JsonProperty("headimgurl")
	private String headimgurl;
	
	// language
	@JsonProperty("language")
	private String language;
	
	// user privilege, not useful in here
	@JsonProperty("privilege")
	private String[] privilege;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String[] getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

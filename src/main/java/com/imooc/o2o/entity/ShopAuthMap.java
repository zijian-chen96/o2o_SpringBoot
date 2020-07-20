package com.imooc.o2o.entity;

import java.util.Date;

// shop authorization
public class ShopAuthMap {
	
	// primary key id
	private Long shopAuthId;
	
	// position title
	private String title;
	
	// position flag (use for control employee privileges)
	private Integer titleFlag;
	
	// authorization status. 0.illegal, 1.legal
	private Integer enableStatus;
	
	// create time
	private Date createTime;
	
	// last edit or update time
	private Date lastEditTime;
	
	// employee info object status
	private PersonInfo employee;
	
	// shop object class
	private Shop shop;

	public Long getShopAuthId() {
		return shopAuthId;
	}

	public void setShopAuthId(Long shopAuthId) {
		this.shopAuthId = shopAuthId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTitleFlag() {
		return titleFlag;
	}

	public void setTitleFlag(Integer titleFlag) {
		this.titleFlag = titleFlag;
	}

	public Integer getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public PersonInfo getEmployee() {
		return employee;
	}

	public void setEmployee(PersonInfo employee) {
		this.employee = employee;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}

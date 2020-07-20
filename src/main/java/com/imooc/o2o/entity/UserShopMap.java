package com.imooc.o2o.entity;

import java.util.Date;

// customer shop points
public class UserShopMap {
	
	// primary key Id
	private Long userShopId;
	
	// create time
	private Date createTime;
	
	// customer' points under the shop
	private Integer point;
	
	// customer info object class
	private PersonInfo user;
	
	// shop info object class
	private Shop shop;

	public Long getUserShopId() {
		return userShopId;
	}

	public void setUserShopId(Long userShopId) {
		this.userShopId = userShopId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public PersonInfo getUser() {
		return user;
	}

	public void setUser(PersonInfo user) {
		this.user = user;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	
}

package com.imooc.o2o.entity;

import java.util.Date;

// customer already redeem awards
public class UserAwardMap {
	
	// primary key id
	private Long userAwardId;
	
	// create time
	private Date createTime;
	
	// use status. 0.not redeem, 1.redeemed
	private Integer usedStatus;
	
	// award points the redeem needs points
	private Integer point;
	
	// customer info object class
	private PersonInfo user;
	
	// award info object class
	private Award award;
	
	// shop info object class
	private Shop shop;
	
	// operator info object class
	private PersonInfo operator;

	public Long getUserAwardId() {
		return userAwardId;
	}

	public void setUserAwardId(Long userAwardId) {
		this.userAwardId = userAwardId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUsedStatus() {
		return usedStatus;
	}

	public void setUsedStatus(Integer usedStatus) {
		this.usedStatus = usedStatus;
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

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public PersonInfo getOperator() {
		return operator;
	}

	public void setOperator(PersonInfo operator) {
		this.operator = operator;
	}
	
}

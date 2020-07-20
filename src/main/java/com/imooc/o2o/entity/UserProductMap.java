package com.imooc.o2o.entity;

import java.util.Date;

// customer purchase product
public class UserProductMap {
	
	// primary key id
	private Long userProductId;
	
	// create time
	private Date createTime;
	
	// purchase product get points
	private Integer point;
	
	// customer info object class
	private PersonInfo user;
	
	// product info object class
	private Product product;
	
	// shop info object class
	private Shop shop;
	
	// operator info object class
	private PersonInfo operator;

	public Long getUserProductId() {
		return userProductId;
	}

	public void setUserProductId(Long userProductId) {
		this.userProductId = userProductId;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

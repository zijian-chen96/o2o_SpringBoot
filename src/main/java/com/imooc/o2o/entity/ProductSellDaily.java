package com.imooc.o2o.entity;

import java.util.Date;

// customer purchase product
public class ProductSellDaily {
	
	// primary key
	private Long productSellDailyId;

	// which day sells, unit is day
	private Date createTime;
	
	// total sells
	private Integer total;
	
	// product info object class
	private Product product;
	
	// shop info object class
	private Shop shop;
	
	public Long getProductSellDailyId() {
		return productSellDailyId;
	}

	public void setProductSellDailyId(Long productSellDailyId) {
		this.productSellDailyId = productSellDailyId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
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
	
}

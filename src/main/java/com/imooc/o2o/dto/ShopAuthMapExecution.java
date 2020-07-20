package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;

public class ShopAuthMapExecution {
	// status
	private int state;

	// status info
	private String stateInfo;

	// Auth number
	private int count;

	// processing shopAuthMap
	private ShopAuthMap shopAuthMap;

	// shopAuthMap list (search use)
	private List<ShopAuthMap> shopAuthMapList;

	public ShopAuthMapExecution() {
	}

	// failed execution
	public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// success execution
	public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, ShopAuthMap shopAuthMap) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopAuthMap = shopAuthMap;
	}

	// list success execution
	public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, List<ShopAuthMap> shopAuthMapList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopAuthMapList = shopAuthMapList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ShopAuthMap getShopAuthMap() {
		return shopAuthMap;
	}

	public void setShopAuthMap(ShopAuthMap shopAuthMap) {
		this.shopAuthMap = shopAuthMap;
	}

	public List<ShopAuthMap> getShopAuthMapList() {
		return shopAuthMapList;
	}

	public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
		this.shopAuthMapList = shopAuthMapList;
	}
	
}

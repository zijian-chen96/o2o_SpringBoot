package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserShopMapStateEnum;

public class UserShopMapExecution {
	// status
	private int state;

	// status info
	private String stateInfo;

	// total number
	private int count;

	private UserShopMap userShopMap;

	private List<UserShopMap> userShopMapList;

	public UserShopMapExecution() {

	}

	// fail constructor
	public UserShopMapExecution(UserShopMapStateEnum stateEnum) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
		}

	// success constructor
	public UserShopMapExecution(UserShopMapStateEnum stateEnum, UserShopMap userShopMap) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.userShopMap = userShopMap;
		}

	// list success constructor
	public UserShopMapExecution(UserShopMapStateEnum stateEnum, List<UserShopMap> userShopMapList) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.userShopMapList = userShopMapList;
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

	public UserShopMap getUserShopMap() {
		return userShopMap;
	}

	public void setUserShopMap(UserShopMap userShopMap) {
		this.userShopMap = userShopMap;
	}

	public List<UserShopMap> getUserShopMapList() {
		return userShopMapList;
	}

	public void setUserShopMapList(List<UserShopMap> userShopMapList) {
		this.userShopMapList = userShopMapList;
	}

}

package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;

public class UserAwardMapExecution {
	// state status
	private int state;

	// state info
	private String stateInfo;

	// total number
	private Integer count;

	// instance UserAwardMap
	private UserAwardMap userAwardMap;

	// user award list（use for search）
	private List<UserAwardMap> userAwardMapList;

	public UserAwardMapExecution() {
	
	}
	
	// fail constructor
	public UserAwardMapExecution(UserAwardMapStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// success constructor
	public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, UserAwardMap userAwardMap) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.userAwardMap = userAwardMap;
	}

	// success constructor for list
	public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, List<UserAwardMap> userAwardMapList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.userAwardMapList = userAwardMapList;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public UserAwardMap getUserAwardMap() {
		return userAwardMap;
	}

	public void setUserAwardMap(UserAwardMap userAwardMap) {
		this.userAwardMap = userAwardMap;
	}

	public List<UserAwardMap> getUserAwardMapList() {
		return userAwardMapList;
	}

	public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
		this.userAwardMapList = userAwardMapList;
	}
	
}

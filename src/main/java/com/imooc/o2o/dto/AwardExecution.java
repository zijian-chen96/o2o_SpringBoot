package com.imooc.o2o.dto;

import java.util.List;

import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;

public class AwardExecution {
	
	// state status
	private int state;

	// state info
	private String stateInfo;

	// total number
	private int count;

	// instance award（use for add/modify/delete）
	private Award award;

	// get award list (use for search awards)
	private List<Award> awardList;

	public AwardExecution() {
		
	}

	// failed constructor
	public AwardExecution(AwardStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// success constructor
	public AwardExecution(AwardStateEnum stateEnum, Award award) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.award = award;
	}

	// success constructor for list
	public AwardExecution(AwardStateEnum stateEnum, List<Award> awardList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.awardList = awardList;
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

	public Award getAward() {
		return award;
	}

	public void setAward(Award award) {
		this.award = award;
	}

	public List<Award> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<Award> awardList) {
		this.awardList = awardList;
	}
	
}

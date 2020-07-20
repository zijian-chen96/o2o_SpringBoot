package com.imooc.o2o.enums;

public enum UserAwardMapStateEnum {
	SUCCESS(1, "success"), INNER_ERROR(-1001, "system error failed"), NULL_USERAWARD_ID(-1002, "empty UserAwardId"),
	NULL_USERAWARD_INFO(-1003, "empty information");

	private int state;

	private String stateInfo;

	private UserAwardMapStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static UserAwardMapStateEnum stateOf(int index) {
		for (UserAwardMapStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}

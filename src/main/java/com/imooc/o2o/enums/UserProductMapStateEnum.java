package com.imooc.o2o.enums;

public enum UserProductMapStateEnum {
	SUCCESS(1, "success"), INNER_ERROR(-1001, "system error failed"), NULL_USERPRODUCT_ID(-1002, "empty UserProductId"),
	NULL_USERPRODUCT_INFO(-1003, "empty information");

	private int state;

	private String stateInfo;

	private UserProductMapStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static UserProductMapStateEnum stateOf(int index) {
		for (UserProductMapStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}

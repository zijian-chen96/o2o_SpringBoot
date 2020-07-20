package com.imooc.o2o.enums;

public enum UserShopMapStateEnum {
	SUCCESS(1, "success"), INNER_ERROR(-1001, "system error failed"), NULL_USERSHOP_ID(-1002, "empty UserShopId"),
	NULL_USERSHOP_INFO(-1003, "empty information");

	private int state;

	private String stateInfo;

	private UserShopMapStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static UserShopMapStateEnum stateOf(int index) {
		for (UserShopMapStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}

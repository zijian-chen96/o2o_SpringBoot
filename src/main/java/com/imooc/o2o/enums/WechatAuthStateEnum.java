package com.imooc.o2o.enums;

public enum WechatAuthStateEnum {
	LOGINFAIL(-1, "wrong openId"), SUCCESS(0, "success"), NULL_AUTH_INFO(-1006, "empty info");

	private int state;

	private String stateInfo;

	private WechatAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static WechatAuthStateEnum stateOf(int index) {
		for (WechatAuthStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}

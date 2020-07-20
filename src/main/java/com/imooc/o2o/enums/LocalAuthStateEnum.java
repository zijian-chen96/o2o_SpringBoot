package com.imooc.o2o.enums;

public enum LocalAuthStateEnum {
	LOGINFAIL(-1, "wrong password or username"), SUCCESS(0, "success"),
	NULL_AUTH_INFO(-1006, "empty account information"), 
	ONLY_ONE_ACCOUNT(-1007, "only allow link one local account");

	private int state;

	private String stateInfo;

	private LocalAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static LocalAuthStateEnum stateOf(int index) {
		for (LocalAuthStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}

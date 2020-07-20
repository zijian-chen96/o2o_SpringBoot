package com.imooc.o2o.enums;

public enum ShopStateEnum {
	CHECK(0, "processing"), OFFLNE(-1, "illegal"), SUCCESS(1, "successed"), PASS(2, "passed"),
	INNER_ERROR(-1001, "system error failed"), NULL_SHOPID(-1002, "null shopId"), NULL_SHOP(-1003, "null shop");

	private int state;
	private String stateInfo;

	private ShopStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * return enum number
	 */
	public static ShopStateEnum stateOf(int state) {
		for (ShopStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}

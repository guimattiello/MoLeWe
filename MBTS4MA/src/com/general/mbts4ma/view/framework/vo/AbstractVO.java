package com.general.mbts4ma.view.framework.vo;

import java.util.UUID;

public abstract class AbstractVO {

	protected String id;

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

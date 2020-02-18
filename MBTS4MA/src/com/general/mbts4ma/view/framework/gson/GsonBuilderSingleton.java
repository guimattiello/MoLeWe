package com.general.mbts4ma.view.framework.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonBuilderSingleton {

	public static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
	public static final String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";

	private static GsonBuilderSingleton instance = null;

	private GsonBuilder gsonBuilder = null;

	private GsonBuilderSingleton() {
		this.gsonBuilder = this.createGsonBuilder();
	}

	private GsonBuilder createGsonBuilder() {
		GsonBuilder gsonBuilder = new GsonBuilder();

		gsonBuilder.setDateFormat(DATE_FORMAT);
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
		gsonBuilder.disableHtmlEscaping();
		gsonBuilder.enableComplexMapKeySerialization();
		gsonBuilder.setPrettyPrinting();
		gsonBuilder.setVersion(1.0);

		return gsonBuilder;
	}

	public static GsonBuilderSingleton getInstance() {
		if (instance == null) {
			instance = new GsonBuilderSingleton();
		}

		return instance;
	}

	public Gson getGson() {
		return this.gsonBuilder.create();
	}

}

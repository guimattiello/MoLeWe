package com.general.mbts4ma;

import java.util.ArrayList;

public class Parameter {
	
	private String type;
	private String value;
	private String name;
		
	public Parameter() {}
	
	public Parameter(String type, String value, String name) {
		this.type = type;
		this.value = value;
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "Parameter = type: " + this.type + " value: " + this.value + " name: " + this.name;
	}
	
	public String toStringArray(ArrayList<Parameter> lp) {
		StringBuilder sb = new StringBuilder();
		for (Parameter p : lp) {
			sb.append(p.toString() + "\n");
		}
		return sb.toString();
	}

}

package com.general.mbts4ma;

import java.util.ArrayList;

public class EventInstance {
	private String id;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

	//For web applications
	private String testCaseMethodName;
	private boolean createdAutomatically;
	
	public EventInstance() {}
	
	public EventInstance(int qtde) {
		Parameter p = null;
		for (int i = 0; i < qtde; i++) {
			p = new Parameter("t"+i, "v"+i, "n"+i);
			this.parameters.add(p);
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	public String getTestCaseMethodName() {
		return testCaseMethodName;
	}
	
	public void setTestCaseMethodName(String testCaseMethodName) {
		this.testCaseMethodName = testCaseMethodName;
	}
	
	public boolean getCreatedAutomatically() {
		return createdAutomatically;
	}
	
	public void setCreatedAutomatically(boolean createdAutomatically) {
		this.createdAutomatically = createdAutomatically;
	}
	
	public String toString() {
		String retorno = "";
		for (Parameter p : this.getParameters()) {
			retorno += p.toString();
		}
		return retorno;
	}
}

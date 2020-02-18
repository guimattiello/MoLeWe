package com.general.mbts4ma.view.framework.util;

import java.io.File;

public class DatabaseRegression {

	private String dbHost;
	private String dbName;
	private String dbUser;
	private String dbPassword;
	
	private String regressionScriptPath;
	private String regressionScriptContent;
	

	public String getDbHost() {
		return this.dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	
	public String getDbName() {
		return this.dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public String getDbUser() {
		return this.dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	
	public String getDbPassword() {
		return this.dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public String getRegressionScriptPath() {
		return this.regressionScriptPath;
	}

	public void setRegressionScriptPath(String regressionScriptPath) {
		this.regressionScriptPath = regressionScriptPath;
	}
	
	public String getRegressionScriptContent() {
		return this.regressionScriptContent;
	}

	public void setRegressionScriptContent(String regressionScriptContent) {
		this.regressionScriptContent = regressionScriptContent;
	}
	
	public String getContentByPath(String path) {
		String fileContent = FileUtil.readFile(new File(path));
		
		return fileContent;
	}
	
	public void refreshContentByPath() {
		String fileContent = FileUtil.readFile(new File(this.regressionScriptPath));
		
		this.regressionScriptContent = fileContent;
	}
	
}

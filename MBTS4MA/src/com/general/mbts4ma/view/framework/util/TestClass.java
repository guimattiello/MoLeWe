package com.general.mbts4ma.view.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import spoon.Launcher;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;

public class TestClass {

	private String className;
	private String path;
	private String content;
	
	public TestClass() {
		
	}
	
	public TestClass(String className, String path) {
		this.className = className;
		this.path = path;
		this.content = this.getContentByPath(path);		
	}
	
	public String getClassName(){
		return this.className;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public String getContent(){
		return this.content;
	}
		
	public String getContentByPath(String path) {
		String fileContent = FileUtil.readFile(new File(path));
		
		return fileContent;
	}
	
	public void refreshContentByPath() {
		String fileContent = FileUtil.readFile(new File(this.path));
		
		this.content = fileContent;
	}
	
}

package com.general.mbts4ma.view.framework.util;

import java.io.File;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

public class PageObject {

	private String className;
	private String qualifiedClassName;
	private String path;
	private String content;	
	private CtClass parsedClass;
	private boolean createdByUser;

	/*public PageObject(String className, CtClass parsedClass) {
		this.className = className;
		this.parsedClass = parsedClass;		
	}*/
	
	public PageObject(String className, String content, String qualifiedClassName, boolean createdByUser) {
		this.className = className;
		this.qualifiedClassName = qualifiedClassName;
		this.content = content;
		this.createdByUser = createdByUser;
	}
	
	public String getClassName(){
		return this.className;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public void setContent(String content){
		this.content = content;
		CtClass clazz = SpoonUtil.getCtClassFromClassContent(this.content);
		this.parsedClass = clazz;
	}
	
	public String getContent(){
		return this.content;
	}
	
	public String getQualifiedClassName(){
		return this.qualifiedClassName;
	}
	
	public boolean getCreatedByUser(){
		return this.createdByUser;
	}
	
	/*public String getContentByPath(String path) {
		String fileContent = FileUtil.readFile(new File(path));
		this.parsedClass = Launcher.parseClass(fileContent);
		
		return fileContent;
	}*/
	
	public void refreshContentByPath() {
		String fileContent = FileUtil.readFile(new File(this.path));
		
		this.content = fileContent;
	}

	public CtClass getParsedClass() {
		if (this.parsedClass == null) {
			CtClass clazz = SpoonUtil.getCtClassFromClassContent(this.content);
			this.parsedClass = clazz;
			return clazz;
		} else {
			return this.parsedClass;
		}
		
	}
	
	public void setParsedClass(CtClass clazz) {
		this.parsedClass = clazz;
	}
	
	public void createNewAbstractMethod(CtMethod method) {
		
		CtClass clazz = this.getParsedClass();
		
		clazz.addMethod(method);
		
		this.content = clazz.toString();
	}
	
}

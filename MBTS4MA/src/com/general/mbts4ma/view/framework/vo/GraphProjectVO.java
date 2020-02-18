package com.general.mbts4ma.view.framework.vo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import com.general.mbts4ma.*;
import com.general.mbts4ma.erunner.Event;
import com.general.mbts4ma.view.framework.util.DatabaseRegression;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.util.TestClass;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;

public class GraphProjectVO extends AbstractVO implements Serializable {

	private transient String fileSavingPath;

	private String name;
	private String description;
	private String androidProjectPath;

	/** BEGIN WEB PROJECT VARIABLES **/	
	private String webProjectURL;
	private String webProjectPageObject;
	private boolean isWebProject;
	private List<PageObject> pageObjects;
	private List<TestClass> testClasses;
	private String webProjectDirTestPath;
	private DatabaseRegression databaseRegression;
	
	private Launcher launcher;
	
	private String dbhost;
	private String dbname;
	private String dbuser;
	private String dbpassword;
	
	private ArrayList<String> verticesCreatedByUser;
	private Map<String, ArrayList<String>> edgesCreatedByUser;
	private ArrayList<String> importedTestCaseNames;	
	private Map<String, ArrayList<String>> eventInstanceToVertexRestrictions;
	private Map<String, ArrayList<String>> edgesToVertexRestrictions;
	
	public int countAbstractMethods;
	public int countConcreteMethods;
	
	/** END WEB PROJECT VARIABLES **/
	
	private String graphXML;
	
	private boolean itsAndroidProject = false;
	
	private String framework;

	private String applicationPackage;
	private String mainTestingActivity;

	private Map<String, String> methodTemplatesByVertices;
	private Map<String, Map<String, String>> methodTemplatesPropertiesByVertices;
	private Map<String, String> edgeTemplates;
	
	private Map<String, ArrayList<EventInstance>> eventInstanceByVertice;
	private ArrayList<EventInstance> eventInstance;
	
	private String user;
	private Date lastDate;

	public GraphProjectVO() {
		super();
		this.id = generateUUID();
		this.countAbstractMethods = 0;
		this.countConcreteMethods = 0;
				
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAndroidProjectPath() {
		return this.androidProjectPath;
	}

	public void setAndroidProjectPath(String androidProjectPath) {
		this.androidProjectPath = androidProjectPath;
	}
	
	public String getWebProjectURL() {
		return this.webProjectURL;
	}
	
	public void setWebProjectURL(String webProjectURL) {
		this.webProjectURL = webProjectURL;
	}
	
	public String getWebProjectPageObject() {
		return this.webProjectPageObject;
	}
	
	public void setWebProjectPageObject(String webProjectPageObject) {
		this.webProjectPageObject = webProjectPageObject;
	}
	
	public String getWebProjectDirTestPath() {
		return this.webProjectDirTestPath;
	}

	public void setWebProjectDirTestPath(String webProjectDirTestPath) {
		this.webProjectDirTestPath = webProjectDirTestPath;
	}	
	
	public DatabaseRegression getDatabaseRegression() {
		return this.databaseRegression;
	}
	
	public void setDatabaseRegression(DatabaseRegression databaseRegression) {
		this.databaseRegression = databaseRegression;
	}
	
	public boolean getIsWebProject(){
		return this.isWebProject;
	}
	
	public void setIsWebProject(boolean isWebProject){
		this.isWebProject = isWebProject;
	}
	
	public void setPageObjects(List<PageObject> pageObjects){
		this.pageObjects = pageObjects;
	}

	public List<PageObject> getPageObjects(){
		return this.pageObjects;
	}
	
	public void setLauncher(Launcher launcher){
		this.launcher = launcher;
	}

	public Launcher getLauncher(){
		return this.launcher;
	}
	
	public void setVerticesCreatedByUser(ArrayList<String> verticesCreatedByUser){
		this.verticesCreatedByUser = verticesCreatedByUser;
	}

	public ArrayList<String> getVerticesCreatedByUser(){
		if (this.verticesCreatedByUser == null) {
			this.verticesCreatedByUser = new ArrayList<String>();
		}
		
		return this.verticesCreatedByUser;
	}
	
	public void setEdgesCreatedByUser(Map<String, ArrayList<String>> edgesCreatedByUser){
		this.edgesCreatedByUser = edgesCreatedByUser;
	}

	public Map<String, ArrayList<String>> getEdgesCreatedByUser(){
		if (this.edgesCreatedByUser == null) {
			this.edgesCreatedByUser = new LinkedHashMap<String, ArrayList<String>>();
		}
		
		return this.edgesCreatedByUser;
	}
	
	public void removeEdgesCreatedByUser(String id){
		if (this.edgesCreatedByUser != null && !this.edgesCreatedByUser.isEmpty() && this.edgesCreatedByUser.get(id) != null) {
			this.edgesCreatedByUser.remove(id);
		}
	}
	
	public void removeEdgesToVertexRestriction(String id){
		if (this.edgesToVertexRestrictions != null && !this.edgesToVertexRestrictions.isEmpty() && this.edgesToVertexRestrictions.get(id) != null) {
			this.edgesToVertexRestrictions.remove(id);
		}
	}
	
	public ArrayList<String> getImportedTestCaseNames(){
		if (this.importedTestCaseNames == null) {
			this.importedTestCaseNames = new ArrayList<String>();
		}
		
		return this.importedTestCaseNames;
	}
	
	public Map<String, ArrayList<String>> getEventInstanceToVertexRestrictions(){
		if (this.eventInstanceToVertexRestrictions == null) {
			this.eventInstanceToVertexRestrictions = new LinkedHashMap<String,ArrayList<String>>();
		}
		
		return this.eventInstanceToVertexRestrictions;
	}
	
	public Map<String, ArrayList<String>> getEdgesToVertexRestrictions(){
		if (this.edgesToVertexRestrictions == null) {
			this.edgesToVertexRestrictions = new LinkedHashMap<String,ArrayList<String>>();
		}
		
		return this.edgesToVertexRestrictions;
	}
	
	public ArrayList<String> getDistinctGroupNameOfEventInstances() {
		ArrayList<String> distinctGroupNames = new ArrayList<String>();
		for (Map.Entry<String, ArrayList<EventInstance>> entry : this.eventInstanceByVertice.entrySet()) {
			for (EventInstance ei : entry.getValue()) {
				if (!ei.getCreatedAutomatically() && !distinctGroupNames.contains(ei.getTestCaseMethodName())) {
					distinctGroupNames.add(ei.getTestCaseMethodName());
				}
			}
		}
		return distinctGroupNames;
	}
	
	public void setTestClasses(List<TestClass> testClasses){
		this.testClasses = testClasses;
	}

	public List<TestClass> getTestClasses(){
		return this.testClasses;
	}

	public String getGraphXML() {
		return this.graphXML;
	}

	public void setGraphXML(String graphXML) {
		this.graphXML = graphXML;
	}
	
	public String getFramework(){
		return this.framework;
	}
	
	public void setFramework(String framework){
		this.framework = framework;
	}
	
	public boolean getItsAndroidProject() {
		return this.itsAndroidProject;
	}

	public void setItsAndroidProject(boolean itsAndroidProject) {
		this.itsAndroidProject = itsAndroidProject;
	}

	public String getApplicationPackage() {
		return this.applicationPackage;
	}

	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}

	public String getMainTestingActivity() {
		return this.mainTestingActivity;
	}

	public void setMainTestingActivity(String mainTestingActivity) {
		this.mainTestingActivity = mainTestingActivity;
	}

	public Map<String, String> getMethodTemplatesByVertices() {
		if (this.methodTemplatesByVertices == null) {
			this.methodTemplatesByVertices = new LinkedHashMap<String, String>();
		}

		return this.methodTemplatesByVertices;
	}
	
	// EI - GET
	public ArrayList<EventInstance> getEventInstanceByVertice(String verticeId){
		return this.getEventInstanceByVertices().get(verticeId);
	}

	public void updateMethodTemplateByVertice(String verticeId, String methodTemplate) {
		this.getMethodTemplatesByVertices().put(verticeId, methodTemplate);
	}

	public void removeMethodParametersByVertices(String verticeId) {
		this.getMethodTemplatesByVertices().remove(verticeId);
	}
	
	// EI - REMOVE (ESPEC�FICO)
	public void removeEventInstance(EventInstance ei) {
		this.eventInstance.remove(ei);
	}

	public void setMethodTemplatesByVertices(Map<String, String> methodTemplatesByVertices) {
		this.methodTemplatesByVertices = methodTemplatesByVertices;
	}

	public Map<String, Map<String, String>> getMethodTemplatesPropertiesByVertices() {
		if (this.methodTemplatesPropertiesByVertices == null) {
			this.methodTemplatesPropertiesByVertices = new LinkedHashMap<String, Map<String, String>>();
		}

		return this.methodTemplatesPropertiesByVertices;
	}
	
	// EI -- GET
	public Map<String, ArrayList<EventInstance>> getEventInstanceByVertices() {
		if (this.eventInstanceByVertice == null) {
			this.eventInstanceByVertice = new LinkedHashMap<String, ArrayList<EventInstance>>();
		}

		return this.eventInstanceByVertice;
	}

	public void updateMethodTemplatePropertiesByVertice(String verticeId, Map<String, String> properties) {
		this.getMethodTemplatesPropertiesByVertices().put(verticeId, properties);
	}
	
	// EI - UPDATE
	public void updateEventInstanceByVertices(String verticeId, ArrayList<EventInstance> properties) {
		this.getEventInstanceByVertices().put(verticeId, properties);
	}

	public void removeMethodTemplatePropertiesByVertice(String verticeId) {
		this.getMethodTemplatesPropertiesByVertices().remove(verticeId);
	}
	
	// EI - REMOVE
	public void removeEventInstanceByVertices(String verticeId) {
		this.getEventInstanceByVertices().remove(verticeId);
	}
	
	// remove Parameters
	public void removeMethodParametersPropertiesByVertices(String verticeId) {
		this.getMethodTemplatesPropertiesByVertices().remove(verticeId);
	}

	public void setMethodTemplatesPropertiesByVertices(Map<String, Map<String, String>> methodTemplatesPropertiesByVertices) {
		this.methodTemplatesPropertiesByVertices = methodTemplatesPropertiesByVertices;
	}
	
	// EI - SET
	public void setEventInstanceByVertices(Map<String, ArrayList<EventInstance>> eventInstanceByVertice) {
		this.eventInstanceByVertice = eventInstanceByVertice;
	}

	public Map<String, String> getEdgeTemplates() {
		if (this.edgeTemplates == null) {
			this.edgeTemplates = new LinkedHashMap<String, String>();
		}

		return this.edgeTemplates;
	}

	public void updateEdgeTemplateByVertice(String verticeId, String edgeTemplate) {
		this.getEdgeTemplates().put(verticeId, edgeTemplate);
	}

	public void removeEdgeTemplateByVertice(String verticeId) {
		this.getEdgeTemplates().remove(verticeId);
	}

	public void setEdgeTemplates(Map<String, String> edgeTemplates) {
		this.edgeTemplates = edgeTemplates;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getLastDate() {
		return this.lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getFileSavingPath() {
		return this.fileSavingPath;
	}

	public String getFileSavingDirectory() {
		if (this.hasFileSavingPath()) {
			return this.fileSavingPath.substring(0, this.fileSavingPath.lastIndexOf(File.separator));
		}

		return System.getProperty("user.home");
	}
	
	public boolean hasFileSavingPath() {
		return this.fileSavingPath != null && !"".equalsIgnoreCase(this.fileSavingPath);
	}

	public void setFileSavingPath(String fileSavingPath) {
		this.fileSavingPath = fileSavingPath;
	}
	
	public void pageObjectsRefresh () {
		
		//List<CtType<?>> classesList = this.getLauncher().getFactory().Class().getAll();
		for (PageObject po : this.pageObjects) {
			
			/*if (!po.getCreatedByUser()) {
				for (CtType<?> clazz : classesList) {
					
					if (po.getQualifiedClassName().equals(clazz.getQualifiedName())) {
						
						this.pageObjects.get(this.pageObjects.indexOf(po)).setContent(clazz.toString());
						
					}
				}
			} else {*/
				if (po.getParsedClass() != null && !po.getParsedClass().equals("")) {
					po.setContent(po.getParsedClass().toString());
				}
			//}
			
		}
	}

}

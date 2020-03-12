package com.general.mbts4ma.view.framework.bo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.jgraph.graph.Edge;
import org.jgrapht.alg.DijkstraShortestPath;
import org.w3c.dom.Document;

import com.general.mbts4ma.EventInstance;
import com.general.mbts4ma.Parameter;
import com.general.mbts4ma.view.MainView;
import com.general.mbts4ma.view.dialog.EventPropertiesDialog;
import com.general.mbts4ma.view.dialog.ExtractCESsDialog;
import com.general.mbts4ma.view.dialog.NewTestCasesDialog;
import com.general.mbts4ma.view.framework.gson.GsonBuilderSingleton;
import com.general.mbts4ma.view.framework.util.FileUtil;
import com.general.mbts4ma.view.framework.util.MapUtil;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.util.SpoonUtil;
import com.general.mbts4ma.view.framework.util.StringUtil;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.github.eta.esg.Vertex;
import com.mxgraph.analysis.mxConstantCostFunction;
import com.mxgraph.analysis.mxDistanceCostFunction;
import com.mxgraph.analysis.mxGraphAnalysis;
import com.mxgraph.analysis.mxICostFunction;
import com.mxgraph.costfunction.mxConstCostFunction;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;

import spoon.Launcher;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtTypeReference;

public class GraphProjectBO implements Serializable {

	public static synchronized void updateGraph(GraphProjectVO graphProject, mxGraph graph) {
		graphProject.setGraphXML(mxXmlUtils.getXml(new mxCodec().encode(graph.getModel())));
	}

	public static synchronized String generateJSON(GraphProjectVO graphProject) {
		if (graphProject != null) {
			return GsonBuilderSingleton.getInstance().getGson().toJson(graphProject);
		}

		return null;
	}

	public static synchronized GraphProjectVO open(String path) {
		path = path + (path.endsWith(".mbtsma") ? "" : ".mbtsma");

		String json = FileUtil.readFile(new File(path));

		if (json == null || "".equalsIgnoreCase(json)) {
			return null;
		} else {
			GraphProjectVO graphProject = GsonBuilderSingleton.getInstance().getGson().fromJson(json, GraphProjectVO.class);
			
			//if(!graphProject.getAndroidProjectPath().isEmpty()){
			//	graphProject.setItsAndroidProject(true);
			//}
			
			if(graphProject.getFramework() == null || graphProject.getFramework().isEmpty()){
				graphProject.setFramework("other");
			}
			
			graphProject.setFileSavingPath(path);

			return graphProject;
		}
	}

	public static synchronized boolean save(String path, GraphProjectVO graphProject) {
		path = path + (path.endsWith(".mbtsma") ? "" : ".mbtsma");

		graphProject.setFileSavingPath(path);

		String graphProjectJSON = GraphProjectBO.generateJSON(graphProject);

		FileUtil.writeFile(graphProjectJSON, new File(path));

		return true;
	}

	public static synchronized boolean save(String path, String content, String extension) {
		path = path + (path.endsWith("." + extension) ? "" : "." + extension);

		FileUtil.writeFile(content, new File(path));

		return true;
	}

	public static synchronized boolean exportToPNG(mxGraph graph, mxGraphComponent graphComponent, String path) throws Exception {
		path = path + (path.endsWith(".png") ? "" : ".png");

		String xml = URLEncoder.encode(mxXmlUtils.getXml(new mxCodec().encode(graph.getModel())), "UTF-8");

		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());

		mxPngEncodeParam param = mxPngEncodeParam.getDefaultEncodeParam(image);
		param.setCompressedText(new String[] { "graph", xml });

		FileOutputStream outputStream = new FileOutputStream(new File(path));

		try {
			mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, param);

			if (image != null) {
				encoder.encode(image);
			}
		} finally {
			outputStream.close();
		}

		return true;
	}

	public static synchronized boolean exportToXML(mxGraph graph, String path) throws Exception {
		path = path + (path.endsWith(".xml") ? "" : ".xml");

		String xml = mxXmlUtils.getXml(new mxCodec().encode(graph.getModel()));

		FileUtil.writeFile(xml, new File(path));

		return true;
	}

	public static synchronized boolean loadGraphFromXML(mxGraph graph, String xml) throws Exception {
		Document document = mxXmlUtils.parseXml(xml);

		new mxCodec().decode(document.getDocumentElement(), graph.getModel());

		return true;
	}

	public static synchronized boolean importFromXML(mxGraph graph, String path) throws Exception {
		path = path + (path.endsWith(".xml") ? "" : ".xml");

		Document document = mxXmlUtils.parseXml(mxUtils.readFile(path));

		new mxCodec().decode(document.getDocumentElement(), graph.getModel());

		return true;
	}

	public static synchronized void generateReusedESG(mxGraph graph, GraphProjectVO graphProject) {
		if (graph != null) {
			Object[] edges = graph.getChildEdges(graph.getDefaultParent());

			if (edges != null && edges.length > 0) {
				for (Object edge : edges) {
					mxCell customEdge = (mxCell) edge;

					if (MainView.MARKED_EDGE.equals(customEdge.getStyle())) {
						mxCell source = (mxCell) customEdge.getSource();
						mxCell target = (mxCell) customEdge.getTarget();

						double newX = (source.getGeometry().getCenterX() + target.getGeometry().getCenterX()) / 2;

						double newY = (source.getGeometry().getCenterY() + target.getGeometry().getCenterY()) / 2;

						graph.getModel().beginUpdate();

						// graph.removeCells(new Object[] { customEdge });

						mxCell newVertex = (mxCell) graph.insertVertex(graph.getDefaultParent(), UUID.randomUUID().toString(), customEdge.getValue(), newX, newY, 100, 50, MainView.GENERATED_EVENT_VERTEX);

						graph.insertEdge(graph.getDefaultParent(), UUID.randomUUID().toString(), "", source, newVertex, MainView.GENERATED_EDGE);

						graph.insertEdge(graph.getDefaultParent(), UUID.randomUUID().toString(), "", newVertex, target, MainView.GENERATED_EDGE);

						graph.getModel().endUpdate();

						graph.setSelectionCell(newVertex);

						graphProject.updateMethodTemplateByVertice(newVertex.getId(), (String) newVertex.getValue());

						String methodTemplateContent = FileUtil.readFile(new File("templates" + File.separator + "robotium-templates" + File.separator + "utility-methods" + File.separator + ((String) newVertex.getValue()).replace(" ", "") + ".java"));

						List<String> values = StringUtil.getValuesWithRegEx(methodTemplateContent, "\\{\\{([a-z]+)\\}\\}");

						if (values != null && !values.isEmpty()) {
							EventPropertiesDialog dialog = new EventPropertiesDialog(graphProject, MapUtil.fromList(values));

							dialog.setVisible(true);

							graphProject.updateMethodTemplatePropertiesByVertice(newVertex.getId(), dialog.getValues());
						}
					}
				}
			}
		}
	}

	public static synchronized List<String> getAllVertices(mxGraph graph) {
		List<String> vertices = null;

		if (graph != null) {
			vertices = new LinkedList<String>();

			Object[] cells = graph.getChildVertices(graph.getDefaultParent());

			if (cells != null && cells.length > 0) {
				for (Object cell : cells) {
					vertices.add((String) ((mxCell) cell).getValue());
				}
			}
		}
		return vertices;
	}
	
	public static synchronized Map<String, String> generateMethodNamesMapFromCESs(List<List<Vertex>> cess, String... excepts) {
		Map<String, String> methodNames = null;

		if (cess != null && !cess.isEmpty()) {
			methodNames = new LinkedHashMap<String, String>();

			for (List<Vertex> ces : cess) {
				for (Vertex vertice : ces) {
					boolean insert = true;

					if (excepts != null && excepts.length > 0) {
						for (String except : excepts) {
							if (except.equalsIgnoreCase(vertice.getId())) {
								insert = false;
								break;
							}
						}
					}

					if (insert) {
						methodNames.put(vertice.getId(), StringUtil.toCamelCase(vertice.getName(), false, "[", "]"));
					}
				}
			}
		}

		return methodNames;
	}

	public static synchronized List<String> generateMethodNamesListFromCES(List<Vertex> ces) {
		List<String> methodNames = null;

		if (ces != null && !ces.isEmpty()) {
			methodNames = new LinkedList<String>();

			for (Vertex vertice : ces) {
				methodNames.add(StringUtil.toCamelCase(vertice.getName(), false, "[", "]"));
			}
		}

		return methodNames;
	}

	public static synchronized List<String> generateMethodNamesFromVertices(List<String> vertices) {
		List<String> methodNames = null;

		if (vertices != null && !vertices.isEmpty()) {
			methodNames = new LinkedList<String>();

			for (String vertice : vertices) {
				methodNames.add(StringUtil.toCamelCase(vertice, false, "[", "]"));
			}
		}

		return methodNames;
	}
	
	public static synchronized boolean containsVerticeEventInstance (GraphProjectVO graphProject, List<Vertex> ces) {
		if (ces != null && !ces.isEmpty()) {
			for (Vertex vertice : ces) {
				if (graphProject.getEventInstanceByVertice(vertice.getId()) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static synchronized void generateMethodNamesEventInstanceListFromCES(GraphProjectVO graphProject, Map<String, String> parameters, List<Vertex> ces, StringBuilder testingMethodBodies) throws IOException {
		File parentPath = new File("..");
		String testingMethodTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "TestingMethodTemplate.java"));
		testingMethodTemplate = StringUtil.replace(testingMethodTemplate, parameters);
		
		if(graphProject.getFramework().equals("robotium")){
			testingMethodTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "TestingMethodTemplate.java"));
		}
		
		testingMethodTemplate = StringUtil.replace(testingMethodTemplate, parameters);
		
		List<String> originalCes = null;
		List<String> stringSequence = null;
		String testingMethodBody = "";
		Map<Integer, ArrayList<EventInstance>> map = null;
		int index = 0;

		if (ces != null && !ces.isEmpty()) {
			originalCes = new LinkedList<String>();
			map = new LinkedHashMap<Integer, ArrayList<EventInstance>>();
			
			for (Vertex vertice : ces) {
				originalCes.add(StringUtil.toCamelCase(vertice.getName(), false, "[", "]"));
				if (graphProject.getEventInstanceByVertice(vertice.getId()) != null) {
					if (!graphProject.getEventInstanceByVertice(vertice.getId()).isEmpty()) map.put(index, graphProject.getEventInstanceByVertice(vertice.getId()));
				}
				index++;
			}

			Iterator<Integer> iKey = map.keySet().iterator();
			
			while (iKey.hasNext()) {
				int key = iKey.next();
				ArrayList<EventInstance> values = map.get(key);
				for (EventInstance e : values) {
					stringSequence = new LinkedList<String>(originalCes);
					String nome = stringSequence.get(key);
					stringSequence.set(key, e.getId());
					
					StringBuilder sb = new StringBuilder();
					for (Parameter p : e.getParameters()) {
						if (e.getParameters().size() - 1 == e.getParameters().lastIndexOf(p)) {
							sb.append(p.getType() + " " + p.getName());
						} else {
							sb.append(p.getType() + " " + p.getName() + ", ");
						}		
					}
					
					// METHOD TEMPLATE
					testingMethodBody = testingMethodTemplate
							.replace("{{testingmethodname}}", "EVENT" + e.getId())
							.replace("{{ces}}", StringUtil.convertListToString(stringSequence, "[", "]").replaceAll(e.getId(), nome + "(" + sb.toString() + ")") );
					
					testingMethodBodies.append(testingMethodBody);
					
					if (testingMethodBodies.length() > 0  && testingMethodBodies.indexOf("\n\n") != testingMethodBodies.length() - 1) {
						testingMethodBodies.append("\n\n");
					}
				}	
			}
		}
	}

	//TODO SEQUENCIAS COM EVENTINSTANCE
	public static synchronized boolean generateTestingCodeSnippets(GraphProjectVO graphProject, Map<String, String> parameters, File testingCodeSnippetsDirectory, List<List<Vertex>> cess) throws Exception {
		File parentPath = new File("..");
		
		String testingClassTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "TestingClassTemplate.java"));
		String testingMethodTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "TestingMethodTemplate.java"));
		
		if(graphProject.getFramework().equals("robotium")){
			testingClassTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "TestingClassTemplate.java"));
			testingMethodTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "TestingMethodTemplate.java"));
		}
		
		testingClassTemplate = StringUtil.replace(testingClassTemplate, parameters);
		testingMethodTemplate = StringUtil.replace(testingMethodTemplate, parameters);

		if (testingCodeSnippetsDirectory.exists()) {
			FileUtils.deleteDirectory(testingCodeSnippetsDirectory);
		}

		testingCodeSnippetsDirectory.mkdir();

		if (cess != null && !cess.isEmpty()) {
			StringBuilder testingMethodBodies = new StringBuilder("");

			int count = 1;

			for (List<Vertex> ces : cess) {
				String testingMethodBody = testingMethodTemplate.replace("{{testingmethodname}}", "CES" + count++).replace("{{ces}}", StringUtil.convertListToString(generateMethodNamesListFromCES(ces), "[", "]").replace(" ", ""));

				if (testingMethodBodies.length() > 0) {
					testingMethodBodies.append("\n\n");
				}
				
				if (containsVerticeEventInstance(graphProject, ces)) generateMethodNamesEventInstanceListFromCES(graphProject, parameters, ces, testingMethodBodies);

				testingMethodBodies.append(testingMethodBody);
			}

			testingClassTemplate = testingClassTemplate.replace("{{testingmethodtemplate}}", testingMethodBodies.toString());

			FileUtil.writeFile(testingClassTemplate, new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + parameters.get("{{testingclassname}}") + "Test.java"));

			copyUtilityClasses(parameters, testingCodeSnippetsDirectory);

			generateTestingAdapters(graphProject, generateMethodNamesMapFromCESs(cess, MainView.ID_START_VERTEX, MainView.ID_END_VERTEX), parameters, testingCodeSnippetsDirectory);
		}

		return true;
	}

	private static synchronized void generateTestingAdapters(GraphProjectVO graphProject, Map<String, String> methodNames, Map<String, String> parameters, File testingCodeSnippetsDirectory) throws Exception {
		File parentPath = new File("..");
		
		String testingAdapterClassTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "adapter-templates" + File.separator + "TestingAdapterClassTemplate.java"));
		String testingAdapterMethodTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "adapter-templates" + File.separator + "TestingAdapterMethodTemplate.java"));

		if(graphProject.getFramework().equals("robotium")){
			testingAdapterClassTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "adapter-templates" + File.separator + "TestingAdapterClassTemplate.java"));
			testingAdapterMethodTemplate = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "adapter-templates" + File.separator + "TestingAdapterMethodTemplate.java"));
		}
		
		testingAdapterClassTemplate = StringUtil.replace(testingAdapterClassTemplate, parameters);

		File testingCodeSnippetsAdaptersDirectory = new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + "adapters");

		if (testingCodeSnippetsAdaptersDirectory.exists()) {
			FileUtils.deleteDirectory(testingCodeSnippetsAdaptersDirectory);
		}

		testingCodeSnippetsAdaptersDirectory.mkdir();

		StringBuilder testingMethodBodies = new StringBuilder("");

		if (methodNames != null && !methodNames.isEmpty()) {
			Iterator<String> iMethodNames = methodNames.keySet().iterator();

			while (iMethodNames.hasNext()) {
				String key = iMethodNames.next();
				String value = methodNames.get(key);
												
				if(testingMethodBodies.indexOf(value) == -1){
					
					String testingMethodBody = testingAdapterMethodTemplate.replace("{{testingmethodname}}", value);


				if (graphProject.getMethodTemplatesByVertices().containsKey(key)) {
					String methodTemplateContent = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-methods" + File.separator + graphProject.getMethodTemplatesByVertices().get(key).replace(" ", "") + ".java"));

					if (methodTemplateContent == null || "".equalsIgnoreCase(methodTemplateContent)) {
						methodTemplateContent = FileUtil.readFile(new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "utility-methods" + File.separator + graphProject.getMethodTemplatesByVertices().get(key).replace(" ", "") + ".java"));
					}
					methodTemplateContent = validateEventProperties(methodTemplateContent, graphProject.getMethodTemplatesPropertiesByVertices().get(key));
					testingMethodBody = testingMethodBody.replace("{{testingmethodtemplate}}", methodTemplateContent);
					
				} else {
					testingMethodBody = testingMethodBody.replace("{{testingmethodtemplate}}", "");
				}

				if (testingMethodBodies.length() > 0) {
					testingMethodBodies.append("\n\n");
				}
					testingMethodBodies.append(testingMethodBody);
				}							
			}
		}

		testingAdapterClassTemplate = testingAdapterClassTemplate.replace("{{testingmethodtemplate}}", testingMethodBodies.toString());

		FileUtil.writeFile(testingAdapterClassTemplate, new File(testingCodeSnippetsAdaptersDirectory.getAbsolutePath() + File.separator + parameters.get("{{testingclassname}}") + "Adapter.java"));
	}

	private static synchronized String validateEventProperties(String methodTemplateContent, Map<String, String> properties) {
		if (properties != null && !properties.isEmpty()) {
			Iterator<String> iProperties = properties.keySet().iterator();

			while (iProperties.hasNext()) {
				String key = iProperties.next();
				String value = properties.get(key);

				if (value != null && !"".equalsIgnoreCase(value)) {
					methodTemplateContent = methodTemplateContent.replace("{{" + key + "}}", value);
				}
			}
		}

		return methodTemplateContent;
	}

	private static synchronized void copyUtilityClasses(Map<String, String> parameters, File testingCodeSnippetsDirectory) throws Exception {
		File parentPath = new File("..");
		
		File[] utilityClasses = new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "utility-classes").listFiles();

		if (utilityClasses != null && utilityClasses.length > 0) {
			File testingCodeSnippetsUtilityClassesDirectory = new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + "util");

			if (testingCodeSnippetsUtilityClassesDirectory.exists()) {
				FileUtils.deleteDirectory(testingCodeSnippetsUtilityClassesDirectory);
			}

			testingCodeSnippetsUtilityClassesDirectory.mkdir();

			for (File utilityClass : utilityClasses) {
				String utilityClassTemplate = FileUtil.readFile(utilityClass);

				utilityClassTemplate = StringUtil.replace(utilityClassTemplate, parameters);

				FileUtil.writeFile(utilityClassTemplate, new File(testingCodeSnippetsUtilityClassesDirectory.getAbsolutePath() + File.separator + utilityClass.getName()));
			}
		}
	}

	public static synchronized Map<String, String> getMethodTemplates(String framework) {
		File parentPath = new File("..");
		
		Map<String, String> methodTemplates = new LinkedHashMap<String, String>();

		File[] robotiumMethods = null;
		try {
			if(framework == "robotium"){
				robotiumMethods = new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "robotium-methods").listFiles();
			} else {
				robotiumMethods = new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "generic-methods").listFiles();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (robotiumMethods != null && robotiumMethods.length > 0) {
			for (File robotiumMethod : robotiumMethods) {
				methodTemplates.put(StringUtil.splitCamelCase(robotiumMethod.getName().substring(0, robotiumMethod.getName().lastIndexOf("."))), "");
			}
		}

		return methodTemplates;
	}

	public static synchronized Map<String, String> getEdgeTemplates(String framework) {
		File parentPath = new File("..");
		Map<String, String> edgeTemplates = new LinkedHashMap<String, String>();

		File[] utilityMethods = null;
		try {
			if(framework == "robotium"){
				utilityMethods = new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "robotium-templates" + File.separator + "utility-methods").listFiles();
			} else {
				utilityMethods = new File(parentPath.getCanonicalPath() + File.separator + "templates" + File.separator + "generic-templates" + File.separator + "generic-utility-methods").listFiles();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (utilityMethods != null && utilityMethods.length > 0) {
			for (File utilityMethod : utilityMethods) {
				edgeTemplates.put(StringUtil.splitCamelCase(utilityMethod.getName().substring(0, utilityMethod.getName().lastIndexOf("."))), "");
			}
		}

		return edgeTemplates;
	}
	
	/**
	 * Check if all EventInstances of a Complete Event Sequence contains the same parameters quantity
	 * @param GraphProjectVO graphProject
	 * @param List<Vertex> ces
	 * @return boolean
	 * */
	private static synchronized boolean checkWebParametersConsistency(GraphProjectVO graphProject, List<Vertex> ces) {
		
		int maximumParamQuantity = 0;
		
		for (Vertex vertex : ces) {
		
			ArrayList<EventInstance> eis = graphProject.getEventInstanceByVertice(vertex.getId());
			
			if (eis != null && eis.size() > 0) {
				
				if (maximumParamQuantity > 0) {
					if (eis.size() != maximumParamQuantity) {
						JOptionPane.showMessageDialog(null, "Check the parameters consistency!", "Attention", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				} else {
					maximumParamQuantity = eis.size();
				}
				
			}
		
		}
		
		return true;
	}

	private static synchronized boolean checkIfVertexWasCreatedByUser(GraphProjectVO graphProject, Vertex vertex) {
		
		if (graphProject.getVerticesCreatedByUser().contains(vertex.getId()))
			return true;
		
		return false;
	}
	
	private static synchronized boolean checkIfEventInstanceWasCreatedByUser(GraphProjectVO graphProject, EventInstance ei) {
		
		if (ei.getCreatedAutomatically())
			return false;
		
		return true;
	}
	
	private static synchronized Integer getVertexVisits(Map<String, Integer> visitArr, String id) {
		
		if (visitArr.get(id) == null) {
			visitArr.put(id, 0);
		}
		
		return visitArr.get(id);
		
	}
	
	private static synchronized void incrementVertexVisits(Map<String, Integer> visitArr, String id) {
		
		if (visitArr.get(id) == null) {
			visitArr.put(id, 1);
		} else {
			visitArr.put(id, visitArr.get(id) + 1);
		}
	}
	
	private static synchronized boolean checkBFS(ArrayList<String> seq, ArrayList<String> mustPassVerticesId, mxCell edge) {
		
		for (String mustPassVertex : mustPassVerticesId) {
			if (!seq.contains(mustPassVertex))
				return false;
		}
		
		if (edge != null) {
			
			boolean checkEdge = false;
			
			mxCell vertexA = (mxCell) edge.getSource();
			mxCell vertexB = (mxCell) edge.getTerminal(false);
			
			for (int i = 0; i < seq.size() - 1; i++) {
				if (seq.get(i).equals(vertexA.getId()) && seq.get(i+1).equals(vertexB.getId()))
					checkEdge = true;
			}
			
			return checkEdge;
			
		}
		
		return true;
		
	}
	
	private static synchronized ArrayList<String> getSequenceBFS(GraphProjectVO graphProject, ArrayList<String> mustPassVerticesId, mxGraph graph, mxCell edge) {

		class Vertice {
			public String id;
			public ArrayList<String> seq;			
			
			Vertice(String id, ArrayList<String> seq) {
				this.id = id;
				
				this.seq = new ArrayList<String>();
				for (String v : seq) {
					this.seq.add(v);
				}
				this.seq.add(this.id);
			}
			
			String getId() {
				return this.id;
			}
			
			ArrayList<String> getSeq() {
				return this.seq;
			}
			
			Object[] getOutgoingEdges(mxGraph graph) {
				mxCell cell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(this.id);
				Object[] outgoingEdges = graph.getOutgoingEdges(cell);
				return outgoingEdges;				
			}
			
		}
		
		int visitsLimit = 100;
		
		int searchLimit = 100000;
		
		int searchAux = 1;
		
		Map<String, Integer> visitas = new LinkedHashMap<String, Integer>();
		ArrayList<Vertice> fila = new ArrayList<Vertice>();
		
		fila.add(new Vertice(MainView.ID_START_VERTEX, new ArrayList<String>()));
		
		while (fila.size() > 0 && searchAux < searchLimit) {
			
			Vertice v = fila.remove(0);
			Object[] edges = v.getOutgoingEdges(graph);
			
			for (int i = 0; i < edges.length; i++) {
				
				mxCell terminalVertex = (mxCell)((mxCell)edges[i]).getTerminal(false);
				
				if (getVertexVisits(visitas, terminalVertex.getId()) < visitsLimit) {
					
					incrementVertexVisits(visitas, terminalVertex.getId());
					
					Vertice newV = new Vertice(terminalVertex.getId(), v.getSeq());
					
					fila.add(newV);
					
					if (checkBFS(newV.getSeq(), mustPassVerticesId, edge)) {
						
						newV.getSeq().add(MainView.ID_END_VERTEX);
						
						return newV.getSeq();
					}
										
				}
				
			}
			
			searchAux++;
			
		}
		
		return null;
		
	}
	
	private static synchronized ArrayList<Object> getPathFromVerticesOrder(ArrayList<String> verticesOrder, GraphProjectVO graphProject, mxGraph graph) {
		
		mxICostFunction mcf = new mxConstCostFunction(1.00d);
		mxGraphAnalysis mga = mxGraphAnalysis.getInstance();
		
		ArrayList<Object> path = new ArrayList<Object>();
		
		for (int i = 0; i < verticesOrder.size()-1; i++) {
		
			mxCell vertexA = (mxCell) ((mxGraphModel)graph.getModel()).getCell(verticesOrder.get(i));
			
			mxCell vertexB = (mxCell) ((mxGraphModel)graph.getModel()).getCell(verticesOrder.get(i+1));
			
			Object[] elements = mga.getShortestPath(graph, vertexA, vertexB, mcf, 1000, true);
			
			for (int j = 0; j < elements.length - 1; j++) {
				path.add(elements[j]);
			}
			
		}
		
		path.add((mxCell) ((mxGraphModel)graph.getModel()).getCell(MainView.ID_END_VERTEX));		
		
		return path;
		
	}
	
	private static synchronized List<CtMethod<?>> getMethodsToCreateFromCES(GraphProjectVO graphProject, List<Vertex> ces, mxGraph graph) {
		
		List<CtMethod<?>> methodsToCreate = new ArrayList<CtMethod<?>>();
		
		mxICostFunction mcf = new mxConstCostFunction(1.00d);
		mxGraphAnalysis mga = mxGraphAnalysis.getInstance();
		
		double timeToGeneratePath = 0;
		double timeToGenerateTestCasesFromPath = 0;
		double initial = 0;
		int eventCount = 0;
		
		//Find paths covering all event instances
		ArrayList<String> eventInstanceGroups = graphProject.getDistinctGroupNameOfEventInstances();
		
		//Find paths covering all new edges
		Map<String, ArrayList<String>> newEdgesCreatedByUser = new LinkedHashMap<String, ArrayList<String>>();
		
		for (Map.Entry<String, ArrayList<String>> entry : graphProject.getEdgesCreatedByUser().entrySet()) {
		    String key = entry.getKey();
		    ArrayList<String> value = entry.getValue();
		    newEdgesCreatedByUser.put(key, (ArrayList<String>) value.clone());
		}
		
		for (String eventInstanceGroup : eventInstanceGroups) {
			
			ArrayList<String> verticesId = new ArrayList<String>();
			
			for (Map.Entry<String, ArrayList<EventInstance>> entry : graphProject.getEventInstanceByVertices().entrySet()) {
				for (EventInstance ei : entry.getValue()) {					
					if (ei.getTestCaseMethodName().equals(eventInstanceGroup)) {
						verticesId.add(entry.getKey());
					}
				}
			}
			
			ArrayList<String> restrictions = graphProject.getEventInstanceToVertexRestrictions().get(eventInstanceGroup);
			
			if (restrictions != null) {
				for (String restriction : restrictions) {
					verticesId.add(restriction);
				}
			}
			
			initial = System.currentTimeMillis();
			ArrayList<String> verticesOrder = getSequenceBFS(graphProject, verticesId, graph, null);
			if (verticesOrder == null || verticesOrder.size() == 0) {
				System.out.println(eventInstanceGroup);
			}
			ArrayList<Object> path = getPathFromVerticesOrder(verticesOrder, graphProject, graph);
			timeToGeneratePath += (System.currentTimeMillis() - initial);
			eventCount += (path.size() - 3)/2; //Length of path - less initial and final vertices and edges - considering only vertices

			ArrayList<String> eventInstanceToUse = new ArrayList<String>();
			eventInstanceToUse.add(eventInstanceGroup);
			
			initial = System.currentTimeMillis();
			methodsToCreate.add(createMethodFromPath(graphProject, path, eventInstanceToUse));
			timeToGenerateTestCasesFromPath += (System.currentTimeMillis() - initial);
			
			//Update remaining edges to be covered
			ArrayList<String> removeFromEdgesRemaining = new ArrayList<String>();
			for (Map.Entry<String, ArrayList<String>> entry : newEdgesCreatedByUser.entrySet()) {
			    String key = entry.getKey();			    
				for (Object element : path) {
					mxCell e = (mxCell) element;
					if (e.getId().equals(key))
						removeFromEdgesRemaining.add(key);
				}
			}
			for (String edge : removeFromEdgesRemaining) {
				newEdgesCreatedByUser.remove(edge);
			}
						
		}
		
		// ---------- Paths for new edges with restrictions
		
		while (!newEdgesCreatedByUser.isEmpty()) {
		
			Map.Entry<String,ArrayList<String>> entry = newEdgesCreatedByUser.entrySet().iterator().next();
		 	String newEdgeId = entry.getKey();
			
			mxCell newEdge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(newEdgeId);
			
			mxCell sourceVertex = (mxCell)newEdge.getSource();
			mxCell terminalVertex = (mxCell)newEdge.getTerminal(false);
			
			ArrayList<String> verticesId = new ArrayList<String>();
			
			verticesId.add(sourceVertex.getId());
			verticesId.add(terminalVertex.getId());
			
			ArrayList<String> restrictions = graphProject.getEdgesToVertexRestrictions().get(newEdgeId);
			
			if (restrictions != null) {
				for (String restriction : restrictions) {
					verticesId.add(restriction);
				}
			}
			
			initial = System.currentTimeMillis();
			ArrayList<String> verticesOrder = getSequenceBFS(graphProject, verticesId, graph, newEdge);
			
			ArrayList<Object> path = getPathFromVerticesOrder(verticesOrder, graphProject, graph);
			timeToGeneratePath += (System.currentTimeMillis() - initial);
			eventCount += (path.size() - 3)/2; //Length of path - less initial and final vertices and edges - considering only vertices
			
			initial = System.currentTimeMillis();
			methodsToCreate.add(createMethodFromPath(graphProject, path, graphProject.getEdgesCreatedByUser().get(newEdgeId)));
			timeToGenerateTestCasesFromPath += (System.currentTimeMillis() - initial);
			
			//Update remaining edges to be covered
			ArrayList<String> removeFromEdgesRemaining = new ArrayList<String>();
			for (Map.Entry<String, ArrayList<String>> entry2 : newEdgesCreatedByUser.entrySet()) {
			    String key = entry2.getKey();
				for (Object element : path) {
					mxCell e = (mxCell) element;
					if (e.getId().equals(key)) {
						ArrayList<String> verifyRestrictions = graphProject.getEdgesToVertexRestrictions().get(key);
						if (verifyRestrictions != null && verifyRestrictions.size() > 0) {
							boolean delete = true;
							for (String rest : verifyRestrictions) {
								Object vertex = ((mxGraphModel)graph.getModel()).getCell(rest);
								if (!path.contains(vertex))
									delete = false;
							}
							if (delete)
								removeFromEdgesRemaining.add(key);
						} else {
							removeFromEdgesRemaining.add(key);
						}
						
					}
				}
			}
			for (String edge : removeFromEdgesRemaining) {
				newEdgesCreatedByUser.remove(edge);
			}
				
		}
		// ----------
				
		System.out.println("Time to generate paths: " + timeToGeneratePath);
		System.out.println("Time to generate test cases from path: " + timeToGenerateTestCasesFromPath);
		System.out.println("Event count (vertices): " + eventCount);
		System.out.println(methodsToCreate.toString());
		
		return methodsToCreate;
	}
	
	public static synchronized CtMethod<?> createMethodFromPath(GraphProjectVO graphProject, ArrayList<Object> path, ArrayList<String> eventInstanceToUse) {
		//Cria o método e inicia o bloco
		CtMethod<?> newMethod = graphProject.getLauncher().getFactory().createMethod();
		newMethod.addModifier(ModifierKind.PUBLIC);
		newMethod.setSimpleName("CT" + UUID.randomUUID().toString().replace("-", ""));
		CtBlock<?> ctBlock = graphProject.getLauncher().getFactory().createBlock();
		newMethod.setBody(ctBlock);
		
		//Generate testscript from path
		for (int i = 0; i < path.size(); i++) {
			
			if (i % 2 == 0) {
				//A variável PARAM que define qual será o eventInstance usado para gerar o statement, por padrão é o primeiro EventInstance
				mxCell vertex = (mxCell) path.get(i);
				ArrayList<EventInstance> eventInstances = graphProject.getEventInstanceByVertice(vertex.getId());
				EventInstance param = null;
				if (eventInstances != null && !eventInstances.isEmpty()) {
					param = eventInstances.get(0);
					if (!eventInstanceToUse.isEmpty()) {
						for (EventInstance ei : eventInstances) {
							for (String eitouse : eventInstanceToUse) {
								if (ei.getTestCaseMethodName().equals(eitouse)) {
									param = ei;
								}
							}
							
						}
					}
				}
				String statementStr = getStatementFromVertex(graphProject, vertex.getId(), param, ctBlock.toString());
				if (statementStr != null) {
					CtStatement statement = graphProject.getLauncher().getFactory().createCodeSnippetStatement(statementStr);
					ctBlock.addStatement(statement);
				}
			}
			
		}
		
		return newMethod;
	}
	
	private static synchronized List<CtMethod<?>> getMethodsToCreateFromCESNAOMAISUSADO(GraphProjectVO graphProject, List<Vertex> ces) {
		
		List<CtMethod<?>> methodsToCreate = new ArrayList<CtMethod<?>>();				
		
		boolean thereIsVerticesCreatedByUser = false;
		boolean thereIsEventInstanceCreatedByUser = false;
		
		//Has the index of EventInstance that were created by the user
		ArrayList<String> eventInstanceIndexCreatedByUser = new ArrayList<String>();
		
		for (Vertex vertex : ces) {
			
			if (checkIfVertexWasCreatedByUser(graphProject, vertex)) {
				thereIsVerticesCreatedByUser = true;
			}
			
			ArrayList<EventInstance> eis = graphProject.getEventInstanceByVertice(vertex.getId());
			
			if (eis != null) {
			
				if (eis.size() > 0 && eventInstanceIndexCreatedByUser == null)
					eventInstanceIndexCreatedByUser = new ArrayList<String>();
				
				for (EventInstance ei : eis) {
					if (checkIfEventInstanceWasCreatedByUser(graphProject, ei)) {
						thereIsEventInstanceCreatedByUser = true;
						if (!eventInstanceIndexCreatedByUser.contains(ei.getTestCaseMethodName()))
							eventInstanceIndexCreatedByUser.add(ei.getTestCaseMethodName());
					}
				}
				
			}
			
		}
		
		if (thereIsEventInstanceCreatedByUser || thereIsVerticesCreatedByUser) {
			
			if (thereIsEventInstanceCreatedByUser) {
				
				for (String testCaseMethodName: eventInstanceIndexCreatedByUser) {
					
					//Cria o método e inicia o bloco
					CtMethod<?> newMethod = graphProject.getLauncher().getFactory().createMethod();
					newMethod.addModifier(ModifierKind.PUBLIC);					
					newMethod.setSimpleName("CT" + UUID.randomUUID().toString().replace("-", "") + testCaseMethodName);
					CtBlock<?> ctBlock = graphProject.getLauncher().getFactory().createBlock();				
					newMethod.setBody(ctBlock);
					
					boolean existConsistentEventInstanceForThisCES = true;
					
					for (Vertex vertex : ces) {
											
						if (existConsistentEventInstanceForThisCES) {
						
							ArrayList<EventInstance> eis = graphProject.getEventInstanceByVertice(vertex.getId());
							
							EventInstance ei = null;
							
							if (eis != null && eis.size() > 0) {
								
								for (EventInstance e : eis) {
									if (e.getTestCaseMethodName().equals(testCaseMethodName))
										ei = e;
								}
							
								if (ei == null) {
									existConsistentEventInstanceForThisCES = false;
									newMethod = null;
								}
								
							}
													
							//Cria o statement para incluir no método
							String statementStr = getStatementFromVertex(graphProject, vertex.getId(), ei, ctBlock.toString());
							if (statementStr != null) {
								CtStatement statement = graphProject.getLauncher().getFactory().createCodeSnippetStatement(statementStr);
								ctBlock.addStatement(statement);
							}

						}
					}
					
					if (newMethod != null) {
						methodsToCreate.add(newMethod);
					}
				}
				
			} else if (thereIsVerticesCreatedByUser) {
				//Cria o método e inicia o bloco
				CtMethod<?> newMethod = graphProject.getLauncher().getFactory().createMethod();
				newMethod.addModifier(ModifierKind.PUBLIC);
				newMethod.setSimpleName("CT" + UUID.randomUUID().toString().replace("-", ""));
				CtBlock<?> ctBlock = graphProject.getLauncher().getFactory().createBlock();
				newMethod.setBody(ctBlock);
				
				//Apenas transforma a CES em um método e adiciona ao MethodsToCreate
				for (Vertex vertex : ces) {
					String statementStr = getStatementFromVertex(graphProject, vertex.getId(), null, ctBlock.toString());
					if (statementStr != null) {
						CtStatement statement = graphProject.getLauncher().getFactory().createCodeSnippetStatement(statementStr);
						ctBlock.addStatement(statement);
					}
				}
				methodsToCreate.add(newMethod);
			}
			
		}
		
		return methodsToCreate;
	}
	
	private static synchronized String getStatementFromVertex(GraphProjectVO graphProject, String vertexId, EventInstance ei, String blockUntilNow) {
		
		String vertexMethod = graphProject.getMethodTemplatesByVertices().get(vertexId);
		
		if (vertexMethod == null)
			return null;
		
		vertexMethod = vertexMethod.replace("\n", "").replace(System.getProperty("line.separator"), "");
		
		String[] split = vertexMethod.split("::");
		
		String methodSignature = split[1];
		String className = split[0];
		
		CtMethod<?> ctMethod = SpoonUtil.getCtMethodFromMethodSignatureAndClassName(methodSignature, className, graphProject.getLauncher(), graphProject.getPageObjects());
		
		CtConstructor<?> constructor = null;
		if (ctMethod == null)
			constructor = SpoonUtil.getCtConstructorFromMethodSignatureAndClassName(methodSignature, className.toLowerCase(), graphProject.getLauncher(), graphProject.getPageObjects());
		
		String statement = "";
		List<CtParameter<?>> params = null;
		
		graphProject.countConcreteMethods++;
		
		if (methodSignature.contains("assertEquals") || methodSignature.contains("assertTrue") || methodSignature.equals("assertFalse")) {			
			if (methodSignature.contains("assertEquals") && ei != null && ei.getParameters().size() == 2) {
				statement = "assertEquals(" + ei.getParameters().get(0).getValue() + ", " + ei.getParameters().get(1).getValue();
			} else if (methodSignature.contains("assertTrue") && ei != null && ei.getParameters().size() == 1){
				statement = "assertTrue(" + ei.getParameters().get(0).getValue();
			} else if (methodSignature.contains("assertFalse") && ei != null && ei.getParameters().size() == 1){
				statement = "assertFalse(" + ei.getParameters().get(0).getValue();
			}
			
		} else if (ctMethod != null) {		
			if (ctMethod.toString().contains("This method need to be implemented!")) {
				graphProject.countAbstractMethods++;
				graphProject.countConcreteMethods--;
			}
			
			String simpleMethodReturnClassName = ctMethod.getType().toString().lastIndexOf(".") != -1 ? ctMethod.getType().toString().substring(ctMethod.getType().toString().lastIndexOf(".") + 1).trim() : ctMethod.getType().toString();  
			String simpleMethodTargetClassName = className.lastIndexOf(".") != -1 ? className.substring(className.lastIndexOf(".") + 1).trim() : className;
			
			statement = (!blockUntilNow.toLowerCase().contains(simpleMethodReturnClassName.toLowerCase() + " " + simpleMethodReturnClassName.toLowerCase()) ? simpleMethodReturnClassName + " " : "") + simpleMethodReturnClassName.toLowerCase() + " = " + simpleMethodTargetClassName.toLowerCase() + "."; 
			statement += ctMethod.getSimpleName() + "(";
			params = ctMethod.getParameters();
		} else if (constructor != null) {			
			String getParamsStr = null;	
			Pattern pattern = Pattern.compile("\\((.*?)\\)");
	        Matcher match = pattern.matcher(vertexMethod);
	        while (match.find()) {        
	        	String found = match.group().trim();
	        	
	        	if (!found.equals(""))
	        		getParamsStr = found;
	        }	        
	        String constructorClassName = methodSignature.replace(getParamsStr, "");
	        if (constructorClassName.indexOf(".") != -1) {
	        	String[] aux = constructorClassName.split("\\.");
	        	constructorClassName = aux[aux.length-1];
	        }
			statement = constructorClassName + " " + constructorClassName.toLowerCase() + " = " + "new " + constructorClassName + "(";
			params = constructor.getParameters();
		} else {
			return  null;
		}
		
		String setParamsStr = "";
		
		if (ei != null && params != null) {				
			
			for (CtParameter<?> ctParameter : params) {
				
				for (Parameter p : ei.getParameters()) {
				
					if (ctParameter.getSimpleName().equals(p.getName()) || ctParameter.getSimpleName().contains(p.getName()) || p.getName().contains(ctParameter.getSimpleName()) || (ctParameter.getType().getSimpleName() + " " + ctParameter.getSimpleName()).equals(p.getName())) {
						if (!setParamsStr.equals(""))
							setParamsStr += ", ";
						setParamsStr += p.getValue();
					}
				}
			}
		}
		
		statement += setParamsStr + ")";
		
		return statement;
	}
	
	public static synchronized boolean generateTestingWebCodeSnippetsFiles(GraphProjectVO graphProject, List<List<Vertex>> cess, mxGraph graph) throws Exception {
	
		if (cess != null && !cess.isEmpty()) {

			//for (List<Vertex> ces : cess) {
				
				long initialTime = System.currentTimeMillis();
				graphProject.countAbstractMethods = 0;
				graphProject.countConcreteMethods = 0;
			
				List<CtMethod<?>> methods = getMethodsToCreateFromCES(graphProject, null, graph);
				
				if (methods != null) {				
					NewTestCasesDialog dialog = new NewTestCasesDialog(methods);
					dialog.setVisible(true);
				}
				
				System.out.println("Time to generate new TCs: " + (System.currentTimeMillis() - initialTime) + " milliseconds");
				System.out.println("Concrete events: " + graphProject.countConcreteMethods);
				System.out.println("Abstract events: " + graphProject.countAbstractMethods);				
				
				//System.out.println(methods);
				
			//}

		}
		
		//graphProject.getLauncher().prettyprint();
		
		return true;
	}
	
	public static synchronized boolean generateTestingWebCodeSnippets(GraphProjectVO graphProject, Map<String, String> parameters, File testingCodeSnippetsDirectory, List<List<Vertex>> cess) throws Exception {
		String testingClassTemplate = FileUtil.readFile(new File("templatesweb" + File.separator + "TestingClassTemplate.java"));
		String testingMethodTemplate = FileUtil.readFile(new File("templatesweb" + File.separator + "TestingMethodTemplate.java"));
		String databaseClassTemplate = FileUtil.readFile(new File("templatesweb" + File.separator + "DatabaseClassTemplate.java"));
		
		testingClassTemplate = StringUtil.replace(testingClassTemplate, parameters);
		testingMethodTemplate = StringUtil.replace(testingMethodTemplate, parameters);
		databaseClassTemplate = StringUtil.replace(databaseClassTemplate, parameters);

		if (testingCodeSnippetsDirectory.exists()) {
			//FileUtils.deleteDirectory(testingCodeSnippetsDirectory);
		}

		testingCodeSnippetsDirectory.mkdir();

		if (cess != null && !cess.isEmpty()) {
			StringBuilder testingMethodBodies = new StringBuilder("");

			int count = 1;

			for (List<Vertex> ces : cess) {
				
				List<String> cesList = generateMethodNamesListFromCES(ces);
				
				String testMethodsCall = "";
				
				for (Iterator iterator = cesList.iterator(); iterator.hasNext();) {
					String methodName = (String) iterator.next();
					
					if (!methodName.equals("]") && !methodName.equals("["))
						testMethodsCall += "assertTrue(adapter."+methodName+"());\n\t";
					
				}
				
				String testingMethodBody = testingMethodTemplate.replace("{{testingmethodname}}", "CES" + count++).replace("{{ces}}", StringUtil.convertListToString(generateMethodNamesListFromCES(ces), "[", "]")).replace("{{methodsinvocation}}", testMethodsCall);

				if (testingMethodBodies.length() > 0) {
					testingMethodBodies.append("\n\n");
				}
				
				testingMethodBodies.append(testingMethodBody);
			}

			testingClassTemplate = testingClassTemplate.replace("{{testingmethodtemplate}}", testingMethodBodies.toString());


			//Creating the Database file into the project directory if setup
			if (graphProject.getDatabaseRegression().getRegressionScriptContent() != null && !graphProject.getDatabaseRegression().getRegressionScriptContent().equals("")) {				
				File testingDatabaseRegressionScriptDirectory = new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + "database");
				if (!testingDatabaseRegressionScriptDirectory.exists()) {
					testingDatabaseRegressionScriptDirectory.mkdir();
				}
				FileUtil.writeFile(databaseClassTemplate, new File(testingDatabaseRegressionScriptDirectory.getAbsolutePath() + File.separator + "Database.java"));
				FileUtil.writeFile(graphProject.getDatabaseRegression().getRegressionScriptContent(), new File(testingDatabaseRegressionScriptDirectory.getAbsolutePath() + File.separator + "script.sql"));
			}
			
			//Creating the Test File into the project directory
			FileUtil.writeFile(testingClassTemplate, new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + parameters.get("{{testingclassname}}") + "Test.java"));			
			
			//copyUtilityClasses(parameters, testingCodeSnippetsDirectory);

			generateTestingWebAdapters(graphProject, generateMethodNamesMapFromCESs(cess, MainView.ID_START_VERTEX, MainView.ID_END_VERTEX), parameters, testingCodeSnippetsDirectory);
		}

		return true;
	}
	
	private static synchronized void generateTestingWebAdapters(GraphProjectVO graphProject, Map<String, String> methodNames, Map<String, String> parameters, File testingCodeSnippetsDirectory) throws Exception {
		String testingAdapterClassTemplate = FileUtil.readFile(new File("templatesweb" + File.separator + "adapter-templates" + File.separator + "TestingAdapterClassTemplate.java"));
		String testingAdapterMethodTemplate = FileUtil.readFile(new File("templatesweb" + File.separator + "adapter-templates" + File.separator + "TestingAdapterMethodTemplate.java"));		
		
		testingAdapterClassTemplate = StringUtil.replace(testingAdapterClassTemplate, parameters);

		File testingCodeSnippetsAdaptersDirectory = new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + "adapter");

		File testingPageObjectsDirectory = new File(testingCodeSnippetsDirectory.getAbsolutePath() + File.separator + "pageobjects");
		
		if (testingCodeSnippetsAdaptersDirectory.exists()) {
			FileUtils.deleteDirectory(testingCodeSnippetsAdaptersDirectory);
		}

		testingCodeSnippetsAdaptersDirectory.mkdir();
		
		if (!testingPageObjectsDirectory.exists()) {
			testingPageObjectsDirectory.mkdir();
		}		

		StringBuilder testingMethodBodies = new StringBuilder("");

		if (methodNames != null && !methodNames.isEmpty()) {
			Iterator<String> iMethodNames = methodNames.keySet().iterator();

			while (iMethodNames.hasNext()) {
				String key = iMethodNames.next();
				String value = methodNames.get(key);

				String testingMethodBody = testingAdapterMethodTemplate.replace("{{testingmethodname}}", value);

				if (graphProject.getMethodTemplatesByVertices().containsKey(key)) {
					//String methodTemplateContent = FileUtil.readFile(new File("templatesweb" + File.separator + "robotium-methods" + File.separator + graphProject.getMethodTemplatesByVertices().get(key).replace(" ", "") + ".java"));
					
					//String methodTemplateContent = graphProject.getMethodTemplatesByVertices().get(key).replace(" ", ".");
					String methodTemplateContent = graphProject.getMethodTemplatesByVertices().get(key);
					
					int indexFirstSpaceBar = methodTemplateContent.indexOf(' ');
					
					String pageObjectNameClass = methodTemplateContent.substring(0, indexFirstSpaceBar);
					
					//Reinstantiate the page object
					String reinstantiatePageObject = StringUtil.toCamelCase(pageObjectNameClass, false) + " = PageFactory.initElements(driver, " + pageObjectNameClass + ".class);" ;
					
					String pageObjectNameClassCamelCase = StringUtil.toCamelCase(pageObjectNameClass, false);
					
					methodTemplateContent = methodTemplateContent.substring(indexFirstSpaceBar+1, methodTemplateContent.length());
					
					methodTemplateContent = methodTemplateContent.substring(0,1).toLowerCase().concat(methodTemplateContent.substring(1))+";";
					
					/*if (methodTemplateContent == null || "".equalsIgnoreCase(methodTemplateContent)) {
						methodTemplateContent = FileUtil.readFile(new File("templatesweb" + File.separator + "utility-methods" + File.separator + graphProject.getMethodTemplatesByVertices().get(key).replace(" ", "") + ".java"));
					}*/

					methodTemplateContent = validateEventWebProperties(methodTemplateContent, graphProject.getMethodTemplatesPropertiesByVertices().get(key));
					
					methodTemplateContent = pageObjectNameClassCamelCase + "." + methodTemplateContent;
					
					testingMethodBody = testingMethodBody.replace("{{reinstantiatepageobject}}", reinstantiatePageObject);
					testingMethodBody = testingMethodBody.replace("{{testingmethodtemplate}}", methodTemplateContent);
				} else {
					testingMethodBody = testingMethodBody.replace("{{testingmethodtemplate}}", "");
				}

				if (testingMethodBodies.length() > 0) {
					testingMethodBodies.append("\n\n");
				}
				
				if (testingMethodBodies.indexOf(testingMethodBody) == -1) {
					testingMethodBodies.append(testingMethodBody);
				}
			}
		}

		testingAdapterClassTemplate = testingAdapterClassTemplate.replace("{{testingmethodtemplate}}", testingMethodBodies.toString());

		FileUtil.writeFile(testingAdapterClassTemplate, new File(testingCodeSnippetsAdaptersDirectory.getAbsolutePath() + File.separator + parameters.get("{{testingclassname}}") + "Adapter.java"));
		
		//Creating the Page Objects file into the project directory		
		ArrayList<PageObject> pobjects = (ArrayList<PageObject>) graphProject.getPageObjects();
		
		for (Iterator iterator = pobjects.iterator(); iterator.hasNext();) {
			PageObject pageObject = (PageObject) iterator.next();
			
			FileUtil.writeFile(pageObject.getContent(), new File(testingPageObjectsDirectory.getAbsolutePath() + File.separator + pageObject.getClassName()));
			
		}

	}
	
	private static synchronized String validateEventWebProperties(String methodTemplateContent, Map<String, String> properties) {
		if (properties != null && !properties.isEmpty()) {
			Iterator<String> iProperties = properties.keySet().iterator();

			while (iProperties.hasNext()) {
				String key = iProperties.next();
				String value = properties.get(key);
				while (key.charAt(0) == ' ') {
					key = key.replaceFirst(" ", "");
				}
				
				String[] tipoParametro = key.split(" ");
				
				if (tipoParametro[0].equals("String")) {
					value = "\""+value+"\"";		
				} else if (tipoParametro[0].equals("char")){
					value = "\'"+value+"\'";
				} else if (tipoParametro[0].equals("float")){
					if (key.indexOf('f') != -1) {
						value = value+"f";
					}
				}
				
				if (value != null && !"".equalsIgnoreCase(value)) {
					methodTemplateContent = methodTemplateContent.replace(key, value);
				}
			}
		}

		return methodTemplateContent;
	}
}

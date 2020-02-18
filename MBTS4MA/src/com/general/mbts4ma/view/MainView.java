package com.general.mbts4ma.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.compress.utils.IOUtils;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;

import com.general.mbts4ma.EventInstance;
import com.general.mbts4ma.Parameter;
import com.general.mbts4ma.view.dialog.ExtractCESsDialog;
import com.general.mbts4ma.view.dialog.ExtractEventFlowDialog;
import com.general.mbts4ma.view.dialog.NewEdgeEventInstancePriorityDialog;
import com.general.mbts4ma.view.dialog.ProjectPropertiesDialog;
import com.general.mbts4ma.view.dialog.WebProjectPropertiesDialog;
import com.general.mbts4ma.view.framework.bo.GraphConverter;
import com.general.mbts4ma.view.framework.bo.GraphProjectBO;
import com.general.mbts4ma.view.framework.bo.GraphSolver;
import com.general.mbts4ma.view.framework.graph.CustomGraphActions;
import com.general.mbts4ma.view.framework.util.ASTSpoonScanner;
import com.general.mbts4ma.view.framework.util.HardwareUtil;
import com.general.mbts4ma.view.framework.util.JScrollPopupMenu;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.util.SpoonModelCompiler;
import com.general.mbts4ma.view.framework.util.SpoonUtil;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.github.eta.esg.Vertex;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.CtModelImpl;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtScanner;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.sniper.SniperJavaPrettyPrinter;

public class MainView extends JFrame {

	public static final String MARKED_EDGE = "MARKED_EDGE";
	public static final String GENERATED_EDGE = "GENERATED_EDGE";
	public static final String NORMAL_VERTEX = "NORMAL_VERTEX";
	public static final String EVENT_VERTEX = "EVENT_VERTEX";
	public static final String PARAMETER_VERTEX = "PARAMETER_VERTEX";
	public static final String GENERATED_EVENT_VERTEX = "GENERATED_EVENT_VERTEX";
	public static final String WEB_GENERATED_NORMAL_EVENT_VERTEX = "WEB_GENERATED_NORMAL_EVENT_VERTEX";
	public static final String WEB_GENERATED_ASSERT_EVENT_VERTEX = "WEB_GENERATED_ASSERT_EVENT_VERTEX";
	public static final String START_VERTEX = "START_VERTEX";
	public static final String END_VERTEX = "END_VERTEX";

	public static final String ID_START_VERTEX = "1000";
	public static final String ID_END_VERTEX = "2000";

	private static final long serialVersionUID = 8273385277816531639L;

	/* BEGIN WEB VARIABLES */
	//public static ArrayList<String> metodos;
	//public static ArrayList<ArrayList<String>> metodosWeb;
	public static ArrayList<String> pageObjectsPath;
	public static ArrayList<PageObject> pageObjects;
	/* END WEB VARIABLES */
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	private mxGraph graph = null;
	private mxGraphComponent graphComponent = null;

	public static final String MY_CUSTOM_VERTEX_STYLE = "MY_CUSTOM_VERTEX_STYLE";
	public static final String MY_CUSTOM_EDGE_STYLE = "MY_CUSTOM_EDGE_STYLE";

	private GraphProjectVO graphProject = null;

	private JButton btnNew;
	private JButton btnOpen;
	private JButton btnSave;
	private JButton btnPreferences;
	private JButton btnClose;
	private JButton btnExecuteGraph;
	private JButton btnExit;

	private JMenu mnFile;
	private JMenu mnProject;
	private JMenu mnSettings;

	private JMenuItem mnItemNew;
	private JMenuItem mnItemNewWebApplicationProject;
	private JMenuItem mnItemOpen;
	private JMenuItem mnItemSave;
	private JMenuItem mnItemClose;
	private JMenuItem mnItemExit;

	private JMenuItem mnItemProperties;
	private JMenuItem mnItemExtractEventFlow;
	private JMenu mnItemExportGraph;
	private JMenu mnItemImportGraph;

	private JMenuItem mnItemToPngImage;
	private JMenuItem mnItemToXml;
	private JMenuItem mnItemFromXml;

	private JMenuItem mnItemExtractCESs;
	private JMenuItem mnItemPreferences;
	private JButton btnProperties;
	private JButton btnExtractEventFlow;
	private JButton btnGenerateReusedEsg;
	private JMenuItem mnItemGenerateReusedESG;

	public MainView() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 600, 450);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.setJMenuBar(menuBar);

		this.mnFile = new JMenu("File");
		this.mnFile.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnFile);

		this.mnItemNew = new JMenuItem("New");
		this.mnItemNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newProject();
			}
		});
		this.mnItemNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemNew);
		
		this.mnItemNewWebApplicationProject = new JMenuItem("New Web App Project");
		this.mnItemNewWebApplicationProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newWebAppProject();
			}
		});
		this.mnItemNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemNewWebApplicationProject);

		this.mnItemOpen = new JMenuItem("Open");
		this.mnItemOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.openProject();
			}
		});
		this.mnItemOpen.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemOpen);

		this.mnItemSave = new JMenuItem("Save");
		this.mnItemSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.saveProject();
			}
		});
		this.mnItemSave.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemSave);

		this.mnItemClose = new JMenuItem("Close");
		this.mnItemClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.closeProject();
			}
		});
		this.mnItemClose.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemClose);

		JSeparator separator = new JSeparator();
		this.mnFile.add(separator);

		this.mnItemExit = new JMenuItem("Exit");
		this.mnItemExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exitApplication();
			}
		});
		this.mnItemExit.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnFile.add(this.mnItemExit);

		this.mnProject = new JMenu("Project");
		this.mnProject.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnProject);

		this.mnItemProperties = new JMenuItem("Properties");
		this.mnItemProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayProjectProperties();
			}
		});
		this.mnItemProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemProperties);

		this.mnItemExtractEventFlow = new JMenuItem("Show edges (event pairs)");
		this.mnItemExtractEventFlow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.showEventPairs();
			}
		});
		this.mnItemExtractEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemExtractEventFlow);

		this.mnItemExtractCESs = new JMenuItem("Extract CESs");
		this.mnItemExtractCESs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.extractCESs();
			}
		});
		this.mnProject.add(this.mnItemExtractCESs);
		this.mnItemExtractCESs.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.mnItemGenerateReusedESG = new JMenuItem("Generate reused ESG");
		this.mnItemGenerateReusedESG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.generateReusedESG();
			}
		});
		this.mnItemGenerateReusedESG.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemGenerateReusedESG);

		JSeparator separator_1 = new JSeparator();
		this.mnProject.add(separator_1);

		this.mnItemExportGraph = new JMenu("Export graph");
		this.mnItemExportGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemExportGraph);

		this.mnItemToPngImage = new JMenuItem("to PNG (Image)");
		this.mnItemToPngImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exportToPng();
			}
		});
		this.mnItemToPngImage.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemExportGraph.add(this.mnItemToPngImage);

		JSeparator separator_3 = new JSeparator();
		this.mnItemExportGraph.add(separator_3);

		this.mnItemToXml = new JMenuItem("to XML");
		this.mnItemToXml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exportToXml();
			}
		});
		this.mnItemToXml.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemExportGraph.add(this.mnItemToXml);

		this.mnItemImportGraph = new JMenu("Import graph");
		this.mnItemImportGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnProject.add(this.mnItemImportGraph);

		this.mnItemFromXml = new JMenuItem("from XML");
		this.mnItemFromXml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.importFromXml();
			}
		});
		this.mnItemFromXml.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnItemImportGraph.add(this.mnItemFromXml);

		this.mnSettings = new JMenu("Settings");
		this.mnSettings.setEnabled(false);
		this.mnSettings.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.add(this.mnSettings);

		this.mnItemPreferences = new JMenuItem("Preferences");
		this.mnItemPreferences.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.mnSettings.add(this.mnItemPreferences);

		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		
		/*this.tabbedPane = new JTabbedPane();
		this.tabbedPane.addTab("Model", this.contentPane);*/		

		this.setContentPane(this.contentPane);

		this.init();

		this.updateControllers();
	}

	private void updateControllers() {
		boolean isProjectOpened = this.graphProject != null;

		this.btnNew.setEnabled(!isProjectOpened);
		this.btnOpen.setEnabled(!isProjectOpened);
		this.btnSave.setEnabled(isProjectOpened);
		this.btnClose.setEnabled(isProjectOpened);

		this.btnProperties.setEnabled(isProjectOpened);
		this.btnExtractEventFlow.setEnabled(isProjectOpened);
		this.btnExecuteGraph.setEnabled(isProjectOpened);
		this.btnGenerateReusedEsg.setEnabled(isProjectOpened);

		this.btnPreferences.setEnabled(false);
		this.btnExit.setEnabled(true);

		this.mnItemNew.setEnabled(!isProjectOpened);
		this.mnItemNewWebApplicationProject.setEnabled(!isProjectOpened);
		this.mnItemOpen.setEnabled(!isProjectOpened);
		this.mnItemSave.setEnabled(isProjectOpened);
		this.mnItemClose.setEnabled(isProjectOpened);

		this.mnProject.setEnabled(isProjectOpened);

		this.mnItemPreferences.setEnabled(true);
		this.mnItemExit.setEnabled(true);
	}

	private void initGraph() {
		if (this.graphComponent != null) {
			this.contentPane.remove(this.graphComponent);
		}

		this.graph = new mxGraph();

		this.graph.setCellsEditable(true);

		this.graphComponent = new mxGraphComponent(this.graph);

		this.graphComponent.getViewport().setOpaque(true);
		this.graphComponent.getViewport().setBackground(Color.WHITE);
		// this.graphComponent.setGridVisible(true);
		// this.graphComponent.setGridColor(Color.GRAY);

		this.graphComponent.setToolTips(true);

		this.setCustomStylesheet();

		this.configureKeyboardEvents();

		/* this.graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
		
			@Override
			public void invoke(Object sender, mxEventObject event) {
				try {
					System.out.println(((mxCell) ((mxGraphSelectionModel) sender).getCell()).getValue());
				} catch (Exception e) {
				}
			}
		}); */

		
		this.graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener(){
			public void invoke(Object sender, mxEventObject evt) {
				mxCell newEdge = (mxCell) evt.getProperty("cell");
				
				NewEdgeEventInstancePriorityDialog dialog = new NewEdgeEventInstancePriorityDialog(MainView.this.graphProject);

				dialog.setVisible(true);
				
				ArrayList<String> eventInstancesToUse = dialog.getEventInstancesToUse();
								
				MainView.this.graphProject.getEdgesCreatedByUser().put(newEdge.getId(), eventInstancesToUse);
			}
		});
		
		this.graph.addListener(mxEvent.CELLS_REMOVED, new mxIEventListener() {

			@Override
			public void invoke(Object o, mxEventObject eo) {
				Object[] cells = (Object[]) eo.getProperty("cells");

				for (Object oCell : cells) {
					MainView.this.graphProject.removeEventInstanceByVertices(((mxCell) oCell).getId());
					MainView.this.graphProject.removeMethodTemplatePropertiesByVertice(((mxCell) oCell).getId());
					MainView.this.graphProject.removeEdgeTemplateByVertice(((mxCell) oCell).getId());
					MainView.this.graphProject.removeEdgesCreatedByUser(((mxCell) oCell).getId());
				}
			}
		});
		
		this.graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);

				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
					String nodeValue = JOptionPane.showInputDialog(null, "Enter the value of the node", "Attention", JOptionPane.INFORMATION_MESSAGE);
										
					if (nodeValue != null && !"".equalsIgnoreCase(nodeValue)) {
						MainView.this.graph.getModel().beginUpdate();

						String uuid = UUID.randomUUID().toString();
						
						MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), uuid, nodeValue, e.getX() - 50, e.getY() - 25, 100, 50, NORMAL_VERTEX);

						graphProject.getVerticesCreatedByUser().add(uuid);
						
						MainView.this.graph.getModel().endUpdate();
					}
										
				} else {
					if (SwingUtilities.isMiddleMouseButton(e)) {
						MainView.this.graphComponent.zoomActual();
					} else if (SwingUtilities.isRightMouseButton(e)) {
						//final JPopupMenu popup = new JPopupMenu();
						final JScrollPopupMenu popup = new JScrollPopupMenu();
						popup.add(MainView.this.bind("Delete", CustomGraphActions.getDeleteAction()));
						popup.add(MainView.this.bind("Rename", CustomGraphActions.getEditAction()));
						popup.add(MainView.this.bind("Display ID", CustomGraphActions.getDisplayIdAction()));
						popup.add(MainView.this.bind("Parameters", CustomGraphActions.getParametersAction(MainView.this.graphProject)));

						popup.addSeparator();

						popup.add(MainView.this.bind("Select All Edges", CustomGraphActions.getSelectAllEdgesAction()));
						popup.add(MainView.this.bind("Select All Vertices", CustomGraphActions.getSelectAllVerticesAction()));

						popup.addSeparator();
						
						if (graphProject.getItsAndroidProject()) {

							final JMenu methodTemplatesMenu = new JMenu("Method Templates");
	
							methodTemplatesMenu.add(MainView.this.bind("Clear Method Template", CustomGraphActions.getClearMethodTemplateAction(MainView.this.graphProject)));
	
							methodTemplatesMenu.addSeparator();
	
							Map<String, String> methodTemplates = GraphProjectBO.getMethodTemplates(graphProject.getFramework());
	
							Iterator<String> iMethodTemplates = methodTemplates.keySet().iterator();
	
							while (iMethodTemplates.hasNext()) {
								String key = iMethodTemplates.next();
	
								methodTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, key)));
							}
	
							popup.add(methodTemplatesMenu);
	
							final JMenu edgeTemplatesMenu = new JMenu("Edge Templates");
	
							edgeTemplatesMenu.add(MainView.this.bind("Clear Edge Template", CustomGraphActions.getClearEdgeTemplateAction(MainView.this.graphProject)));
	
							edgeTemplatesMenu.addSeparator();
	
							Map<String, String> edgeTemplates = GraphProjectBO.getEdgeTemplates(graphProject.getFramework());
	
							Iterator<String> iEdgeTemplates = edgeTemplates.keySet().iterator();
	
							while (iEdgeTemplates.hasNext()) {
								String key = iEdgeTemplates.next();
	
								edgeTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineEdgeTemplateAction(MainView.this.graphProject, key)));
							}
	
							popup.add(edgeTemplatesMenu);
							
						} else { //Senão é web project
							//ArrayList<String> pageObjectsPath = new ArrayList(Arrays.asList(graphProject.getWebProjectPageObject().split(",")));
							
							popup.add(MainView.this.bind("New Event Instance Restriction Here", CustomGraphActions.getNewEventInstanceRestriction(MainView.this.graphProject)));
							popup.add(MainView.this.bind("New Edge Restriction", CustomGraphActions.getNewEdgeRestriction(MainView.this.graphProject)));

							popup.addSeparator();
							
							JMenu assertsTemplatesMenu = new JMenu("ASSERTS");
							assertsTemplatesMenu.add(MainView.this.bind("assertEquals(Object expected, Object actual)", CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, "java.org.junit.Assert::assertEquals(java.lang.Object,java.lang.Object)")));
							assertsTemplatesMenu.add(MainView.this.bind("assertTrue(Object condition)", CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, "java.org.junit.Assert::assertTrue(java.lang.Object)")));
							popup.add(assertsTemplatesMenu);
							
							JMenu pageObjectsTemplatesMenu;
							
							Map<String, String> metodosWeb;
							
							//for (Iterator<String> i = pageObjectsPath.iterator(); i.hasNext(); ){
							for (Iterator<PageObject> i = graphProject.getPageObjects().iterator(); i.hasNext(); ) {
								
								PageObject pageObjectNext = i.next();
								
								String fileContentPageObject = pageObjectNext.getContent();
								String fileNamePageObject = pageObjectNext.getClassName();
								
								pageObjectsTemplatesMenu = new JMenu(fileNamePageObject);
								
								metodosWeb = new LinkedHashMap<String, String>();
								
								Set<CtMethod> ctMethods = pageObjectNext.getParsedClass().getMethods();
								
						        //Run through all methods
						        for (CtMethod method : ctMethods) {
			
									String key = method.getSimpleName();
									
									String signature = pageObjectNext.getClassName() + "::\n" + method.getSignature();
									
									pageObjectsTemplatesMenu.add(MainView.this.bind(key, CustomGraphActions.getDefineMethodTemplateAction(MainView.this.graphProject, signature)));
			
								}
						        
						        pageObjectsTemplatesMenu.add(MainView.this.bind("-- Create new abstract method --", CustomGraphActions.getCreateNewAbstractMethod(MainView.this.graphProject, pageObjectNext)));
						        
								popup.add(pageObjectsTemplatesMenu);
								
							}
							
							popup.add(MainView.this.bind("-- Create new PageObject class --", CustomGraphActions.getCreateNewPageObjectClass(MainView.this.graphProject)));
						
						}

						popup.show(MainView.this.graphComponent, e.getX(), e.getY());
					}
				}
			}

		});

		this.graphComponent.getGraphControl().addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					MainView.this.graphComponent.zoomIn();
				} else {
					MainView.this.graphComponent.zoomOut();
				}
			}
		});

		this.getContentPane().add(this.graphComponent);
		this.refreshContentPane();
	}

	@SuppressWarnings("serial")
	private Action bind(String name, final Action action) {

		return new AbstractAction(name, null) {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.actionPerformed(new ActionEvent(MainView.this.graphComponent, e.getID(), e.getActionCommand()));
			}
		};

	}

	private void refreshContentPane() {
		this.contentPane.revalidate();
		this.contentPane.repaint();
	}

	private void createBasicGraph() {
		MainView.this.graph.getModel().beginUpdate();

		MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), ID_START_VERTEX, "[", 50, 400, 50, 50, START_VERTEX);
		MainView.this.graph.insertVertex(MainView.this.graph.getDefaultParent(), ID_END_VERTEX, "]", 1400, 500, 50, 50, END_VERTEX);

		MainView.this.graph.getModel().endUpdate();
	}

	private void newProject() {
		this.graphProject = null;

		ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(this.graphProject);

		dialog.setVisible(true);

		this.graphProject = dialog.getGraphProject();

		if (this.graphProject != null) {
			this.initGraph();

			this.createBasicGraph();
		}

		this.updateControllers();
	}
	
	//Funcao retorna uma das listas, dentro do parametro searchingIntoSequences, que possui a lista enviada por parametro (searchingBySequence) como sublist.
	private LinkedHashMap<String, String> getPreviousVerticesList(ArrayList<LinkedHashMap<String, String>> searchingIntoSequences, ArrayList<String> searchingBySequence) {
		
		if (searchingBySequence.isEmpty()) 
			return null;
		
		for (LinkedHashMap<String, String> linkedHashMap : searchingIntoSequences) {
		
			int count = searchingBySequence.size()-1;
			
			boolean control = true;
			
			if (linkedHashMap.size() > count) {
				
				while (control == true && count >= 0) {
					
					Set<Map.Entry<String, String>> mapSetSearchingIntoList = linkedHashMap.entrySet();
			        Map.Entry<String, String> elementAtSearchingIntoList = (Map.Entry<String, String>) mapSetSearchingIntoList.toArray()[count];
			        
			        String elementAtSearchingForList = searchingBySequence.get(count);
			        
			        if (!elementAtSearchingForList.equals(elementAtSearchingIntoList.getValue())) {
			        	control = false;
			        }
	
					count--;
					
				}
				
				//Nesse caso, retorna a lista
				if (control == true) 
					return linkedHashMap;
				
			}
			
		}
				
		return null;
	}
	
	private boolean isAssert(CtElement ctElement) {
		
		if (ctElement instanceof CtInvocation) {
			CtInvocation ctInvocation = (CtInvocation) ctElement;
			if (ctInvocation.toString().contains("org.junit.Assert") || (ctInvocation.toString().contains("junit.framework.Assert")) || ctInvocation.toString().contains("assertEquals") || ctInvocation.toString().contains("assertFalse") || ctInvocation.toString().contains("assertTrue")) {
				return true;
			}
		} else if (ctElement instanceof CtConstructorCall) {
			CtConstructorCall ctConstructorCall = (CtConstructorCall) ctElement;
			if (ctConstructorCall.toString().contains("org.junit.Assert") || (ctConstructorCall.toString().contains("junit.framework.Assert")) || ctConstructorCall.toString().contains("assertEquals") || ctConstructorCall.toString().contains("assertFalse") || ctConstructorCall.toString().contains("assertTrue")) {
				return true;
			}
		}
		
		
		
		return false;
	}
	
	private boolean relatedToAProjectClass(CtElement ctElement) {
		
		ArrayList<String> pageObjectsName = new ArrayList<String>();
		
		for (PageObject pageObject : this.graphProject.getPageObjects()) {
			pageObjectsName.add(pageObject.getClassName());
		}
		
		if (ctElement instanceof CtInvocation) {
			CtInvocation ctInvocation = (CtInvocation) ctElement;
			
			if (ctInvocation.getTarget().getType() != null && pageObjectsName.contains(ctInvocation.getTarget().getType().getSimpleName())) {
				//if ((ctInvocation.getExecutable().getType() != null) && (classesName.contains(ctInvocation.getExecutable().getType().getSimpleName()) || (ctInvocation.getExecutable().getType().getSimpleName().contains("void")))) {
					return true;
				//}
			}
				
		} else if (ctElement instanceof CtConstructorCall) {
			CtConstructorCall ctConstructorCall = (CtConstructorCall) ctElement;
			if (ctConstructorCall.getExecutable().getType() != null && pageObjectsName.contains(ctConstructorCall.getExecutable().getType().getSimpleName()))
				return true;
		}
		
		
		
		return false;
	}
	
	/*private CtMethod getMethodBySignature2(String signature, List<CtType<?>> classesList, String className) {
		
		for (CtType<?> type : classesList) {
			Set<CtMethod<?>> ctMethods = type.getMethods();
			for (CtMethod<?> method : ctMethods) { 
				if (method.getSignature().equals(signature) && (className.contains(type.getSimpleName()) || (type.getSimpleName().contains(className)) || (className == null))) {
					return method;
				}
			}
		}
		
		return null;
		
	}*/
	
	
	private void newWebAppProject() {
		this.graphProject = null;

		WebProjectPropertiesDialog dialog = new WebProjectPropertiesDialog(this.graphProject);

		dialog.setVisible(true);

		this.graphProject = dialog.getGraphProject();

		if (this.graphProject != null) {
			graphProject.setIsWebProject(true);
			
			this.initGraph();
			
			this.createBasicGraph();
		} else {
			return;
		}
		
		long initialTime = System.currentTimeMillis();

		//final CtModelImpl model = (CtModelImpl) launcher.getModel();
		List<CtType<?>> classesList = this.graphProject.getLauncher().getFactory().Class().getAll();
		
		//this.graphProject.setLauncher(launcher);
		
		double verticalDistance = 50;
		
		//Essa lista serve para identificar caminhos pré-existentes
		ArrayList<LinkedHashMap<String, String>> listaStatements = new ArrayList<LinkedHashMap<String, String>>();
		
		ArrayList<Double> countDistanceX = new ArrayList<Double>();
		
		for (CtType<?> type : classesList) {
			
			Set<CtMethod<?>> ctMethods = type.getMethods();
			
			for (CtMethod<?> method : ctMethods) {
									        	
	            //Get the annotations to look for test methods        	
	        	for (CtAnnotation<? extends Annotation> ann : method.getAnnotations()) {
	        		
	                if (ann.toString().contains("org.junit.Test") || ann.toString().equals("@Test")) {
	                	CtBlock block = method.getBody();
	                	
	                	//Grava a sequência de statements desse método com o respectivo id do vértice
	                	LinkedHashMap<String, String> methodSequence = new LinkedHashMap<String, String>();
	                		              	                	
	                    List<CtStatement> statements = block.getStatements();
	                    
	                    ArrayList<String> statementsSequence = new ArrayList<String>();	                   	                    
	                    
	                    double horizontalDistance = 200;	                   
	                    
	                    mxCell lastVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_START_VERTEX); 	                    	                    
	                    	                    
	                    for (CtStatement statement : statements) {
	                    	
	                    	//ASTSpoonScanner Class cleans invocations arguments list
	                    	//ArrayList<CtElement> elementsFromStatementWithArguments = (new ASTSpoonScanner()).visitStatementAST(statement);	     	                    	
	                    	
	                    	//Statements without arguments
	                    	ArrayList<CtElement> elementsFromStatement = (new ASTSpoonScanner()).visitStatementAST(statement);		                    
	                    	
	                    	//System.out.println(elementsFromStatement);
	                    	
	                    	//Check if the statement is an Assert
	                    	if (elementsFromStatement.size() > 0 && elementsFromStatement.get(elementsFromStatement.size()-1) instanceof CtInvocation) {	                    		
	                    		CtInvocation checkIfIsAssert = (CtInvocation) elementsFromStatement.get(elementsFromStatement.size()-1);
	                    	
	                    		//If an Assert, remove all related invocations
	                    		if (isAssert(checkIfIsAssert)) {
		                    		elementsFromStatement.clear();
		                    		elementsFromStatement.add(checkIfIsAssert);
		                    	}
	                    	}
	                    	
	                    	for (CtElement ctElement : elementsFromStatement) {
	                    		
	                    		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	                    		
	                    		if ((ctElement instanceof CtInvocation || ctElement instanceof CtConstructorCall) && (isAssert(ctElement) || relatedToAProjectClass(ctElement))) {
	                    			
	                    			CtInvocation ctInvocation = null;
	                    			CtConstructorCall ctConstructorCall = null;
	                    			
	                    			String methodUniqueName = "";
	                    			String vertexLabel = "";
	                    			String vertexType = MainView.WEB_GENERATED_NORMAL_EVENT_VERTEX;
	                    			double vertexSize = 0;
	                    			
	                    			if (ctElement instanceof CtInvocation) {
		                    			ctInvocation = (CtInvocation) ctElement;
		                    			//System.out.println(ctInvocation.getExecutable().getSignature().toString() + " - " + ctInvocation.getArguments());
		                    			
		                    			//Create the parameters
		                    			List<CtExpression<?>> args = ctInvocation.getArguments();
		                    			
		                    			int countNameParam = 0;
		                    			
		                    			for (CtExpression<?> param : args) {
		                    				CtMethod<?> methodParam = SpoonUtil.getMethodBySignature(ctInvocation.getExecutable().getSignature(), classesList, ctInvocation.getTarget().getType().toString(), graphProject.getPageObjects());		                    				
		                    				Parameter p = new Parameter(param.getType().getSimpleName(), param.toString(), (methodParam != null ? methodParam.getParameters().get(countNameParam).toString() : "nome do metodo"));
											parameters.add(p);
											countNameParam++;
										}		                    			
		                    			
		                    			methodUniqueName = ctInvocation.getTarget().getType() + "::" + ctInvocation.getExecutable();
		                    			vertexSize = ctInvocation.getExecutable().getSimpleName().toString().length() * 3 + 120;
		                    			if (isAssert(ctInvocation)) {
		                    				vertexLabel = "ASSERT\n" + ctInvocation.getExecutable().getSimpleName();
		                    				vertexType = MainView.WEB_GENERATED_ASSERT_EVENT_VERTEX;
		                    			} else {		              
		                    				if (ctInvocation.getTarget().getType() != null)
		                    					vertexLabel = ctInvocation.getTarget().getType().getSimpleName() + "::\n" + ctInvocation.getExecutable().getSimpleName();
		                    				else
		                    					vertexLabel = ctInvocation.getTarget() + "::\n" + ctInvocation.getExecutable().getSimpleName();
		                    					
		                    			}
		                    			
	                    			} else {
	                    				ctConstructorCall = (CtConstructorCall) ctElement;
	                    				
	                    				//Create the parameters
		                    			List<CtExpression<?>> args = ctConstructorCall.getArguments();
		                    			
		                    			int countNameParam = 0;
		                    			
		                    			for (CtExpression<?> param : args) {
		                    				countNameParam++;
		                    				CtConstructor<?> constructor = SpoonUtil.getCtConstructorFromMethodSignatureAndClassName(ctConstructorCall.getExecutable().getSignature(), ctConstructorCall.getExecutable().getDeclaringType().getSimpleName(), this.graphProject.getLauncher(), this.graphProject.getPageObjects());
		                    				//CtMethod methodParam = getMethodBySignature(ctConstructorCall.getExecutable().getSignature(), classesList);
		                    				Parameter p = new Parameter(param.getType().getSimpleName(), param.toString(), (constructor != null ? constructor.getParameters().get(0).toString() : "nome do metodo"));
											parameters.add(p);
										}	
	                    				
	                    				methodUniqueName = ctConstructorCall.getExecutable().getType() + "::" + ctConstructorCall.getExecutable();
	                    				vertexSize = ctConstructorCall.getExecutable().getSimpleName().toString().length() * 3 + 120;
	                    				if (isAssert(ctConstructorCall)) {
		                    				vertexLabel = "ASSERT\n" + ctConstructorCall.getExecutable().getSimpleName();
		                    				vertexType = MainView.WEB_GENERATED_ASSERT_EVENT_VERTEX;
		                    			} else {		                    			
		                    				vertexLabel = ctConstructorCall.getExecutable().getType().getSimpleName() + "::\n new " + ctConstructorCall.getExecutable().getType().getSimpleName();
		                    			}
	                    			}
		                    		
		                    		statementsSequence.add(methodUniqueName);
		                    		
		                    		LinkedHashMap<String, String> listLastCommonVertex = getPreviousVerticesList(listaStatements, statementsSequence);

		                    		mxCell newVertex = null;
		                    		
		                    		String newVertexId = UUID.randomUUID().toString();
		                    		
		                    		boolean createNewVertex = true;
		                    		
		                    		if (listLastCommonVertex != null && listLastCommonVertex.size() > 0) {
		                    			Set<Map.Entry<String, String>> mapSetSearchingReturnedList = listLastCommonVertex.entrySet();
		                    			
		                    			Map.Entry<String, String> lastElementAtSearchingIntoList = null;
		                    			
		                    			if (mapSetSearchingReturnedList.size() >= statementsSequence.size()) {
		                    				
		                    				lastElementAtSearchingIntoList = (Map.Entry<String, String>) mapSetSearchingReturnedList.toArray()[statementsSequence.size()-1];
		                    			
		                    			}
		                    			
		                    			String lastElementAtSearchingForList = statementsSequence.get(statementsSequence.size()-1);
		                    			
		                		        if (lastElementAtSearchingIntoList != null && lastElementAtSearchingForList.equals(lastElementAtSearchingIntoList.getValue())) {
		                		        	
		                		        	newVertexId = lastElementAtSearchingIntoList.getKey();
		                		        	createNewVertex = false;
		                		        	
		                		        } else if (mapSetSearchingReturnedList.size()+1 >= statementsSequence.size() && listLastCommonVertex.size() > 1 && statementsSequence.size() > 1) {
		                		        	
		                		        	Map.Entry<String, String> beforeLastElementAtSearchingIntoList = (Map.Entry<String, String>) mapSetSearchingReturnedList.toArray()[statementsSequence.size()-2];
		                		        	String beforeLastElementAtSearchingForList = statementsSequence.get(statementsSequence.size()-2);
		                		        	
		                		        	if (beforeLastElementAtSearchingForList.equals(beforeLastElementAtSearchingIntoList.getValue())) {
		                		        		lastVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(beforeLastElementAtSearchingIntoList.getKey());
		                		        	}
		                		        }
		                    		}
		                    		
		                    		if (createNewVertex) {		                    		
			                    		
		                    			newVertex = (mxCell) graph.insertVertex(MainView.this.graph.getDefaultParent(), newVertexId, vertexLabel, horizontalDistance, verticalDistance, vertexSize, 50, vertexType);
			                    		
			                    		graph.insertEdge(graph.getDefaultParent(), UUID.randomUUID().toString(), "", lastVertex, newVertex, MainView.GENERATED_EDGE);																				
			                    		
			                    		//Update methodTemplateByVertice
			                    		this.graphProject.updateMethodTemplateByVertice(newVertexId, methodUniqueName);
		                    		} else {
		                    			newVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(newVertexId);
		                    		}
		                    				                    		
		                    		if (parameters.size() > 0) {
			                    		//Pega um possível event instance já declarado
			                    		ArrayList<EventInstance> arrEI = graphProject.getEventInstanceByVertice(newVertexId);
			                    		
			                    		if (arrEI == null) {
			                    			arrEI = new ArrayList<EventInstance>();
			                    		}
			                    		
			                    		EventInstance ei = new EventInstance();
			                    		ei.setId(UUID.randomUUID().toString());
		                    			ei.setParameters(parameters);
		                    			ei.setTestCaseMethodName(method.getSignature());
		                    			ei.setCreatedAutomatically(true);
			                    	
		                    			if (!this.graphProject.getImportedTestCaseNames().contains(method.getSignature()))
		                    				this.graphProject.getImportedTestCaseNames().add(method.getSignature());
		                    			
		                    			arrEI.add(ei);
			                    		graphProject.updateEventInstanceByVertices(newVertexId, arrEI);
		                    		}

		                    		methodSequence.put(newVertexId, methodUniqueName);
									
									lastVertex = newVertex;
	                    		
			                    	horizontalDistance += vertexSize + 50;
			                    	
	                    		}
		                    	
							}
	                    		                    	
		    				//System.out.println("---------\n---------\n---------");
	                    	
	                    }
	                    
	                    countDistanceX.add(horizontalDistance);
	                    
	                    verticalDistance += 100;
	                    
	                    //link the last vertex created to the last vertex of the graph
	                    mxCell lastGraphVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_END_VERTEX);
	                    graph.insertEdge(graph.getDefaultParent(), UUID.randomUUID().toString(), "", lastVertex, lastGraphVertex, MainView.GENERATED_EDGE);	                    
	                   
	                    listaStatements.add(methodSequence);
	                    
	                    Double longerArrayStatement = countDistanceX.get(0);
	                    
	                    for (Double distanceX : countDistanceX) {
							if (distanceX > longerArrayStatement)
								longerArrayStatement = distanceX;
						}
	                    
	                    mxGeometry geo = (mxGeometry) graph.getCellGeometry(lastGraphVertex).clone();
	                    geo.setY(50 * listaStatements.size());
	                    geo.setX(longerArrayStatement);
	                    MainView.this.graph.getModel().setGeometry(lastGraphVertex, geo);
	                    
	                    mxCell firstVertex = (mxCell) ((mxGraphModel)graph.getModel()).getCell(ID_START_VERTEX);
	                    
	                    mxGeometry geo2 = (mxGeometry) graph.getCellGeometry(firstVertex).clone();
	                    geo2.setY(50 * listaStatements.size());	                    
	                    graph.getModel().setGeometry(firstVertex, geo2);
	                    
	                }
	        	}
			}
			
	    }

		MainView.this.graph.getModel().endUpdate();
		
		//---- METRICS ---- //
		System.out.println("Time to create graph: " + (System.currentTimeMillis() - initialTime) + " miliseconds");
		Object[] edges = graph.getChildEdges(graph.getDefaultParent());
		Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
		System.out.println("#Vertices WITHOUT initial/final: " + this.graphProject.getMethodTemplatesByVertices().size());
		System.out.println("#Vertices with initial/final: " + vertices.length);
		System.out.println("#Edges: " + edges.length);
		
		int eiCount = 0;
		Map<String, ArrayList<EventInstance>> ei = this.graphProject.getEventInstanceByVertices();
		for (ArrayList<EventInstance> value : ei.values()) {
		    for (EventInstance e : value) {
		    	if (e.getCreatedAutomatically())
		    		eiCount++;
		    }
		}		
		System.out.println("#EventInstance automatically created: " + eiCount);
		//---- END METRICS ---- //
		
		this.updateControllers();
	}

	private void openProject() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("Model-Based Test Suite For Mobile Applications (*.mbtsma, *.graph, *.esg)", "mbtsma", "graph", "esg"));
		fileChooser.setDialogTitle("Specify a file to open");

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			this.load(fileChooser.getSelectedFile().getAbsolutePath());
		}

		this.updateControllers();
	}

	private void load(String path) {
		this.graphProject = GraphProjectBO.open(path);

		if (this.graphProject != null) {
			this.initGraph();

			try {
				if (this.graphProject.getIsWebProject()) {
					Launcher launcher = new Launcher();
					launcher.getEnvironment().setNoClasspath(true);
					launcher.getEnvironment().setAutoImports(true);
					launcher.addInputResource(this.graphProject.getWebProjectDirTestPath());			
					launcher.buildModel();
					
					this.graphProject.setLauncher(launcher);
					
					System.out.println("#Vertices WITHOUT initial/final: " + this.graphProject.getMethodTemplatesByVertices().size());
					int eiCount = 0;
					Map<String, ArrayList<EventInstance>> ei = this.graphProject.getEventInstanceByVertices();
					for (ArrayList<EventInstance> value : ei.values()) {
					    for (EventInstance e : value) {
					    	if (!e.getCreatedAutomatically())
					    		eiCount++;
					    }
					}
					System.out.println("#EventInstance created by the user: " + eiCount);					
					
				}
				
				GraphProjectBO.loadGraphFromXML(this.graph, this.graphProject.getGraphXML());
								
				Object[] edges = graph.getChildEdges(graph.getDefaultParent());
				Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
				System.out.println("#Edges: " + edges.length);
				System.out.println("#Vertices with initial/final: " + vertices.length);				

				JOptionPane.showMessageDialog(null, "Project successfully opened.", "Attention", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		this.updateControllers();
	}

	private void saveProject() {
		String fileSavingPath = null;
		
		Launcher l = this.graphProject.getLauncher();
		
		this.graphProject.setLauncher(null);
		
		for (PageObject pageObject : this.graphProject.getPageObjects()) {
			pageObject.setParsedClass(null);
		}
		/*this.graphProject.setTestClasses(null);
		this.graphProject.setPageObjects(null);*/

		if (this.graphProject.hasFileSavingPath()) {
			fileSavingPath = this.graphProject.getFileSavingPath();
		} else {
			JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

			fileChooser.setSelectedFile(new File(this.graphProject.getName()));
			fileChooser.setFileFilter(new FileNameExtensionFilter("Model-Based Test Suite For Mobile Applications (*.mbtsma, *.graph, *.esg)", "mbtsma", "graph", "esg"));
			fileChooser.setDialogTitle("Specify a file to save");

			int result = fileChooser.showSaveDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				fileSavingPath = fileChooser.getSelectedFile().getAbsolutePath();
			}
		}

		if (fileSavingPath != null) {
			GraphProjectBO.updateGraph(this.graphProject, this.graph);

			this.graphProject.setLastDate(new Date());
			this.graphProject.setUser(HardwareUtil.getComputerName());

			if (GraphProjectBO.save(fileSavingPath, this.graphProject)) {
				JOptionPane.showMessageDialog(null, "Project successfully saved.", "Attention", JOptionPane.INFORMATION_MESSAGE);
				this.graphProject.setLauncher(l);
			}
		}

		this.updateControllers();
	}

	private void closeProject() {
		if (this.graphComponent != null) {
			this.contentPane.remove(this.graphComponent);
		}

		this.refreshContentPane();

		this.graphProject = null;

		this.updateControllers();
	}

	private void displayProjectProperties() {
		if (this.graphProject != null) {
			
			if (this.graphProject.getIsWebProject()) {
				WebProjectPropertiesDialog dialog = new WebProjectPropertiesDialog(this.graphProject);
				
				dialog.setVisible(true);
			} else {
				ProjectPropertiesDialog dialog = new ProjectPropertiesDialog(this.graphProject);
	
				dialog.setVisible(true);
			}
		}
	}

	private void showEventPairs() {
		if (this.graphProject != null) {
			ArrayList<String> errorMsgs = GraphConverter.verifyESG(this.graph);
			if(errorMsgs.isEmpty()) {
				try {
					GraphConverter.convertToESG(this.graph);

					String eventPairs = GraphConverter.getEventFlow();

					ExtractEventFlowDialog dialog = new ExtractEventFlowDialog(this.graphProject, eventPairs);

					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
			else {
				JOptionPane.showMessageDialog(null, setUpMessages(errorMsgs), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	
	private String setUpMessages(ArrayList<String> msgs) {
		StringBuilder setupMsg = new StringBuilder("");
		for(String msg : msgs)
			setupMsg.append(msg + "\n");
		
		return setupMsg.toString();
	}

	private void extractCESs() {
		if (this.graphProject != null) {
			ArrayList<String> errorMsgs = GraphConverter.verifyESG(this.graph);
			if(errorMsgs.isEmpty()) {
				try {
					GraphSolver.solve(this.graph);
	
					List<List<Vertex>> cess = GraphSolver.getCess();
	
					String cessAsString = GraphSolver.getCESsAsString();
	
					ExtractCESsDialog dialog = new ExtractCESsDialog(this.graph, this.graphProject, cess, cessAsString);
	
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, setUpMessages(errorMsgs), "Error", JOptionPane.ERROR_MESSAGE);
			}				
		}
	}

	private void generateReusedESG() {
		GraphProjectBO.generateReusedESG(this.graph, this.graphProject);
	}

	private void exportToPng() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
		fileChooser.setDialogTitle("Specify a file to export PNG");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.exportToPNG(this.graph, this.graphComponent, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void exportToXml() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		fileChooser.setDialogTitle("Specify a file to export XML");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.exportToXML(this.graph, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void importFromXml() {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		fileChooser.setDialogTitle("Specify a file to import XML");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				GraphProjectBO.importFromXML(this.graph, fileChooser.getSelectedFile().getAbsolutePath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void displayPreferences() {

	}

	private void exitApplication() {
		int result = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

		if (result == JOptionPane.YES_OPTION) {
			this.dispose();
		}
	}

	private void init() {
		JToolBar toolBarHeader = new JToolBar();
		toolBarHeader.setFloatable(false);
		toolBarHeader.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.contentPane.add(toolBarHeader, BorderLayout.NORTH);

		this.btnNew = new JButton("");
		this.btnNew.setToolTipText("New");
		this.btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.newProject();
			}
		});
		this.btnNew.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/new.png")));
		this.btnNew.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnNew);

		this.btnOpen = new JButton("");
		this.btnOpen.setToolTipText("Open");
		this.btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.openProject();
			}
		});
		this.btnOpen.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/open.png")));
		this.btnOpen.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnOpen);

		this.btnSave = new JButton("");
		this.btnSave.setToolTipText("Save");
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.saveProject();
			}
		});
		this.btnSave.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/save.png")));
		this.btnSave.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnSave);

		this.btnPreferences = new JButton("");
		this.btnPreferences.setToolTipText("Preferences");
		this.btnPreferences.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/preferences.png")));
		this.btnPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayPreferences();
			}
		});

		this.btnClose = new JButton("");
		this.btnClose.setToolTipText("Close");
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.closeProject();
			}
		});
		this.btnClose.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/close.png")));
		this.btnClose.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnClose);

		toolBarHeader.addSeparator();

		this.btnExecuteGraph = new JButton("");
		this.btnExecuteGraph.setToolTipText("Extract CESs");
		this.btnExecuteGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.executeGraph();
			}
		});

		this.btnProperties = new JButton("");
		this.btnProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.displayProjectProperties();
			}
		});
		this.btnProperties.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/properties.png")));
		this.btnProperties.setToolTipText("Properties");
		this.btnProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnProperties.setEnabled(false);
		toolBarHeader.add(this.btnProperties);

		this.btnExtractEventFlow = new JButton("");
		this.btnExtractEventFlow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.showEventPairs();
			}
		});
		this.btnExtractEventFlow.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/eventflow.png")));
		this.btnExtractEventFlow.setToolTipText("Show edges (event pairs)");
		this.btnExtractEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnExtractEventFlow.setEnabled(false);
		toolBarHeader.add(this.btnExtractEventFlow);
		this.btnExecuteGraph.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/executegraph.png")));
		this.btnExecuteGraph.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnExecuteGraph);

		this.btnGenerateReusedEsg = new JButton("");
		this.btnGenerateReusedEsg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.generateReusedESG();
			}
		});
		this.btnGenerateReusedEsg.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/generate.png")));
		this.btnGenerateReusedEsg.setToolTipText("Generate reused ESG");
		this.btnGenerateReusedEsg.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.btnGenerateReusedEsg.setEnabled(false);
		toolBarHeader.add(this.btnGenerateReusedEsg);

		toolBarHeader.addSeparator();

		this.btnPreferences.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnPreferences);

		toolBarHeader.addSeparator();

		this.btnExit = new JButton("");
		this.btnExit.setToolTipText("Exit");
		this.btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainView.this.exitApplication();
			}
		});
		this.btnExit.setIcon(new ImageIcon(MainView.class.getResource("/com/general/mbts4ma/view/framework/images/exit.png")));
		this.btnExit.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarHeader.add(this.btnExit);

		JToolBar toolBarFooter = new JToolBar();
		toolBarFooter.setFloatable(false);
		this.contentPane.add(toolBarFooter, BorderLayout.SOUTH);

		JLabel lblModelbasedTesting = new JLabel("Model-Based Test Suite For Mobile Applications (MBTS4MA)");
		lblModelbasedTesting.setFont(new Font("Verdana", Font.PLAIN, 12));
		toolBarFooter.add(lblModelbasedTesting);
	}

	private void executeGraph() {
		this.extractCESs();
	}

	private void configureKeyboardEvents() {
		new mxKeyboardHandler(this.graphComponent) {

			@Override
			protected InputMap getInputMap(int condition) {
				InputMap map = null;

				if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
					map = (InputMap) UIManager.get("ScrollPane.ancestorInputMap");
				} else if (condition == JComponent.WHEN_FOCUSED) {
					map = new InputMap();

					map.put(KeyStroke.getKeyStroke("DELETE"), "delete");
					map.put(KeyStroke.getKeyStroke("F1"), "displayid");
					map.put(KeyStroke.getKeyStroke("F2"), "edit");
					map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK), "selectall");
				}

				return map;
			}

			@Override
			protected ActionMap createActionMap() {
				ActionMap map = (ActionMap) UIManager.get("ScrollPane.actionMap");

				map.put("delete", CustomGraphActions.getDeleteAction());
				map.put("displayid", CustomGraphActions.getDisplayIdAction());
				map.put("edit", CustomGraphActions.getEditAction());
				map.put("selectall", CustomGraphActions.getSelectAllAction());

				return map;
			}

		};
	}

	private void setCustomStylesheet() {
		mxStylesheet stylesheet = new mxStylesheet();

		Map<String, Object> edge = new HashMap<String, Object>();
		edge.put(mxConstants.STYLE_ROUNDED, true);
		edge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// edge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		edge.put(mxConstants.STYLE_STROKECOLOR, "#000000");
		edge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		edge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		edge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		edge.put(mxConstants.STYLE_FONTSIZE, "10");
		edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);

		Map<String, Object> markedEdge = new HashMap<>(edge);
		markedEdge.put(mxConstants.STYLE_ROUNDED, true);
		markedEdge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// markedEdge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		markedEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		markedEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		markedEdge.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
		markedEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		markedEdge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		markedEdge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		markedEdge.put(mxConstants.STYLE_FONTSIZE, "10");
		markedEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		markedEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);		

		Map<String, Object> generatedEdge = new HashMap<>(edge);
		generatedEdge.put(mxConstants.STYLE_ROUNDED, true);
		generatedEdge.put(mxConstants.STYLE_ORTHOGONAL, false);
		// generatedEdge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
		generatedEdge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
		generatedEdge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
		generatedEdge.put(mxConstants.STYLE_STROKECOLOR, "#339900");
		generatedEdge.put(mxConstants.STYLE_STROKEWIDTH, "2");
		generatedEdge.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		generatedEdge.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		generatedEdge.put(mxConstants.STYLE_FONTSIZE, "10");
		generatedEdge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
		generatedEdge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
		generatedEdge.put(mxConstants.STYLE_EDITABLE, false);
		generatedEdge.put(mxConstants.STYLE_DELETABLE, false);
		generatedEdge.put(mxConstants.STYLE_MOVABLE, false);
				
		Map<String, Object> startVertex = new HashMap<String, Object>();
		startVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		startVertex.put(mxConstants.STYLE_FILLCOLOR, "#88FFAA");
		startVertex.put(mxConstants.STYLE_STROKECOLOR, "#009933");
		startVertex.put(mxConstants.STYLE_STROKEWIDTH, "3");
		startVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		startVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		startVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		startVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

		Map<String, Object> endVertex = new HashMap<String, Object>();
		endVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
		endVertex.put(mxConstants.STYLE_FILLCOLOR, "#FF9090");
		endVertex.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
		endVertex.put(mxConstants.STYLE_STROKEWIDTH, "3");
		endVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		endVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		endVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		endVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);

		Map<String, Object> normalVertex = new HashMap<String, Object>();
		normalVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		normalVertex.put(mxConstants.STYLE_FILLCOLOR, "#9DE7FF");
		normalVertex.put(mxConstants.STYLE_STROKECOLOR, "#006688");
		normalVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		normalVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		normalVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		normalVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		normalVertex.put(mxConstants.STYLE_ROUNDED, true);
		normalVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> eventVertex = new HashMap<String, Object>();
		eventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		eventVertex.put(mxConstants.STYLE_FILLCOLOR, "#FFFCDD");
		eventVertex.put(mxConstants.STYLE_STROKECOLOR, "#9A9E40");
		eventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		eventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		eventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		eventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		eventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		eventVertex.put(mxConstants.STYLE_ROUNDED, true);
		eventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		
		Map<String, Object> parameterVertex = new HashMap<String, Object>();
		parameterVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		parameterVertex.put(mxConstants.STYLE_FILLCOLOR, "#E6E3C4");
		parameterVertex.put(mxConstants.STYLE_STROKECOLOR, "#666344");
		parameterVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		parameterVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		parameterVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		parameterVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		parameterVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		parameterVertex.put(mxConstants.STYLE_ROUNDED, true);
		parameterVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> generatedEventVertex = new HashMap<String, Object>();
		generatedEventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		generatedEventVertex.put(mxConstants.STYLE_FILLCOLOR, "#CFBBEE");
		generatedEventVertex.put(mxConstants.STYLE_STROKECOLOR, "#996699");
		generatedEventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		generatedEventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		generatedEventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		generatedEventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		generatedEventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		generatedEventVertex.put(mxConstants.STYLE_ROUNDED, true);
		generatedEventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);

		Map<String, Object> webGeneratedNormalEventVertex = new HashMap<String, Object>();
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_FILLCOLOR, "#c2f0f0");
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_STROKECOLOR, "#996699");
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_ROUNDED, true);
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_EDITABLE, false);
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_MOVABLE, false);
		webGeneratedNormalEventVertex.put(mxConstants.STYLE_DELETABLE, false);
		
		Map<String, Object> webGeneratedAssertEventVertex = new HashMap<String, Object>();
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_FILLCOLOR, "#6fdcdc");
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_STROKECOLOR, "#996699");
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_STROKEWIDTH, "2");
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_FONTFAMILY, "Verdana");
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_ITALIC);
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_FONTSIZE, "10");
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_ROUNDED, true);
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ENTITY_RELATION);
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_EDITABLE, false);
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_MOVABLE, false);
		webGeneratedAssertEventVertex.put(mxConstants.STYLE_DELETABLE, false);		
		
		stylesheet.setDefaultEdgeStyle(edge);

		stylesheet.putCellStyle(MARKED_EDGE, markedEdge);

		stylesheet.putCellStyle(GENERATED_EDGE, generatedEdge);

		stylesheet.putCellStyle(START_VERTEX, startVertex);

		stylesheet.putCellStyle(END_VERTEX, endVertex);

		stylesheet.putCellStyle(NORMAL_VERTEX, normalVertex);

		stylesheet.putCellStyle(EVENT_VERTEX, eventVertex);
		
		stylesheet.putCellStyle(PARAMETER_VERTEX, parameterVertex);

		stylesheet.putCellStyle(GENERATED_EVENT_VERTEX, generatedEventVertex);
		
		stylesheet.putCellStyle(WEB_GENERATED_NORMAL_EVENT_VERTEX, webGeneratedNormalEventVertex);

		stylesheet.putCellStyle(WEB_GENERATED_ASSERT_EVENT_VERTEX, webGeneratedAssertEventVertex);

		this.graph.setStylesheet(stylesheet);

		this.graph.setAllowDanglingEdges(false);
		this.graph.setMultigraph(false);
		this.graph.setAllowLoops(true);
	}

}

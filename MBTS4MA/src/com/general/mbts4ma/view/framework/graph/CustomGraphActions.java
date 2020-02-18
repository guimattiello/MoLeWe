package com.general.mbts4ma.view.framework.graph;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.jgraph.graph.Edge;

import com.general.mbts4ma.EventInstance;
import com.general.mbts4ma.view.MainView;
import com.general.mbts4ma.view.dialog.EventPropertiesDialog;
import com.general.mbts4ma.view.dialog.ParametersDialog;
import com.general.mbts4ma.view.framework.util.FileUtil;
import com.general.mbts4ma.view.framework.util.MapUtil;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.util.StringUtil;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

public class CustomGraphActions {

	static final Action deleteAction = new DeleteAction("delete");

	public static Action getDeleteAction() {
		return deleteAction;
	}

	static final Action editAction = new EditAction("edit");

	public static Action getEditAction() {
		return editAction;
	}

	static final Action displayIdAction = new DisplayIdAction("displayid");

	public static Action getDisplayIdAction() {
		return displayIdAction;
	}

	static final Action selectAllEdgesAction = new SelectAllEdgesAction("selectalledges");

	public static Action getSelectAllEdgesAction() {
		return selectAllEdgesAction;
	}

	static final Action selectAllVerticesAction = new SelectAllVerticesAction("selectallvertices");

	public static Action getSelectAllVerticesAction() {
		return selectAllVerticesAction;
	}
	
	public static Action getNewEventInstanceRestriction(GraphProjectVO graphProject) {
		return new NewEventInstanceRestrictionAction("neweventinstancerestriction", graphProject);
	}
	
	public static Action getNewEdgeRestriction(GraphProjectVO graphProject) {
		return new NewEdgeRestrictionAction("neweventinstancerestriction", graphProject);
	}
	
	public static Action getParametersAction(GraphProjectVO graphProject) {
		return new ParametersAction("parameters", graphProject);
	}

	public static Action getDefineMethodTemplateAction(GraphProjectVO graphProject, String label) {
		return new DefineMethodTemplateAction("definemethodtemplate", graphProject, label);
	}

	public static Action getDefineEdgeTemplateAction(GraphProjectVO graphProject, String label) {
		return new DefineEdgeTemplateAction("defineedgetemplate", graphProject, label);
	}

	public static Action getClearMethodTemplateAction(GraphProjectVO graphProject) {
		return new ClearMethodTemplateAction("clearmethodtemplate", graphProject);
	}

	public static Action getClearEdgeTemplateAction(GraphProjectVO graphProject) {
		return new ClearEdgeTemplateAction("clearedgetemplate", graphProject);
	}
	
	public static Action getCreateNewAbstractMethod(GraphProjectVO graphProject, PageObject pageObject) {
		return new CreateNewAbstractMethod("createnewabstractmethod", graphProject, pageObject);
	}
	
	public static Action getCreateNewPageObjectClass(GraphProjectVO graphProject) {
		return new CreateNewPageObjectClass("createnewpageobjectclass", graphProject);
	}
	
	static final Action selectAllAction = new SelectAllAction("selectall");

	public static Action getSelectAllAction() {
		return selectAllAction;
	}
	
	public static class CreateNewAbstractMethod extends AbstractAction {

		private GraphProjectVO graphProject = null;
		private PageObject pageObject = null;
		
		public CreateNewAbstractMethod(String name, GraphProjectVO graphProject, PageObject pageObject) {
			super(name);
			this.graphProject = graphProject;
			this.pageObject = pageObject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					if (!hasStartOrEndEdges(selectedCells)) {
						String methodName = JOptionPane.showInputDialog("Enter the SIGNATURE of the new abstract method", "public LoginPage setPassword (String password)");//og(null, "Enter the SIGNATURE of the new abstract method", "Attention", JOptionPane.INFORMATION_MESSAGE);						
						
						if (methodName != null && !methodName.equals("")) {
							CtClass<?> l = Launcher.parseClass("class A { @TODO\n" + methodName + "{throw new RuntimeException(\"This method need to be implemented!\");}}");
							CtMethod<?> newMethod = (CtMethod<?>)l.getMethods().iterator().next();
							//String qualifiedPOName = this.pageObject.getQualifiedClassName();
							//CtClass<?> pageObject = this.graphProject.getLauncher().getFactory().Class().get(qualifiedPOName);
							//pageObject.addMethod(newMethod);
							this.pageObject.getParsedClass().addMethod(newMethod);
							this.pageObject.setContent(this.pageObject.getParsedClass().toString());
							//this.graphProject.pageObjectsRefresh();
						}
					}
				}
			}
		}
	}
	
	public static class CreateNewPageObjectClass extends AbstractAction {

		private GraphProjectVO graphProject = null;
		
		public CreateNewPageObjectClass(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					if (!hasStartOrEndEdges(selectedCells)) {
						String className = JOptionPane.showInputDialog("Enter the NAME of the new class");						
						
						if (className != null && !className.equals("")) {
							PageObject po = new PageObject(className, "class " + className + " { }", className, true);							
							this.graphProject.getPageObjects().add(po);
						}
					}
				}
			}
		}
	}
	
	public static class NewEventInstanceRestrictionAction extends AbstractAction {

		private GraphProjectVO graphProject = null;		
		
		public NewEventInstanceRestrictionAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					for (Object selectedCell : selectedCells) {
						mxCell vertice = (mxCell) selectedCell;
						String eventInstanceName = JOptionPane.showInputDialog("Enter the event instance name");//og(null, "Enter the SIGNATURE of the new abstract method", "Attention", JOptionPane.INFORMATION_MESSAGE);						
						
						Map<String,ArrayList<String>> eventinstancerestriction = graphProject.getEventInstanceToVertexRestrictions();
						
						if (eventinstancerestriction.get(eventInstanceName) != null && !eventinstancerestriction.get(eventInstanceName).contains(vertice.getId()))	{
							eventinstancerestriction.get(eventInstanceName).add(vertice.getId());
						} else {
							ArrayList<String> cellsId = new ArrayList<String>();
							cellsId.add(vertice.getId());
							eventinstancerestriction.put(eventInstanceName, cellsId);
						}
					}
				}
			}
		}
	}
	
	public static class NewEdgeRestrictionAction extends AbstractAction {

		private GraphProjectVO graphProject = null;		
		
		public NewEdgeRestrictionAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length == 2) {
					mxCell cell1 = (mxCell) selectedCells[0];
					mxCell cell2 = (mxCell) selectedCells[1];

					Map<String,ArrayList<String>> edgerestriction = graphProject.getEdgesToVertexRestrictions();
					
					if (cell1.isEdge() && cell2.isVertex()) {
						
						if (edgerestriction.get(cell1.getId()) != null && !edgerestriction.get(cell1.getId()).contains(cell2.getId()))	{
							edgerestriction.get(cell1.getId()).add(cell2.getId());
						} else {
							ArrayList<String> nodesId = new ArrayList<String>();
							nodesId.add(cell2.getId());
							edgerestriction.put(cell1.getId(), nodesId);
						}
						
						JOptionPane.showMessageDialog(null, "Restriction created succesfully!");
						
					} else if (cell2.isEdge() && cell1.isVertex()){
						
						if (edgerestriction.get(cell2.getId()) != null && !edgerestriction.get(cell2.getId()).contains(cell1.getId()))	{
							edgerestriction.get(cell2.getId()).add(cell1.getId());
						} else {
							ArrayList<String> nodesId = new ArrayList<String>();
							nodesId.add(cell1.getId());
							edgerestriction.put(cell2.getId(), nodesId);
						}
						
						JOptionPane.showMessageDialog(null, "Restriction created succesfully!");
						
					} else {
						JOptionPane.showMessageDialog(null, "Select at least one node and one edge!");
					}
					
					
				} else {
					JOptionPane.showMessageDialog(null, "Select at least one node and one edge!");
				}
			}
		}
	}

	public static class DeleteAction extends AbstractAction {

		public DeleteAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					if (!hasStartOrEndEdges(selectedCells)) {
						int result = JOptionPane.showOptionDialog(null, "Are you sure you want to delete?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

						if (result == JOptionPane.YES_OPTION) {
							graph.removeCells();
						}
					}
				}
			}
		}
	}

	public static class EditAction extends AbstractAction {

		public EditAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graphComponent != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					if (!hasStartOrEndEdges(selectedCells)) {
						int result = JOptionPane.showOptionDialog(null, "Are you sure you want to edit?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes", "No" }, "default");

						if (result == JOptionPane.YES_OPTION) {
							graphComponent.startEditing();
						}
					}
				}
			}
		}

	}
	
	public static class ParametersAction extends AbstractAction {
		
		private GraphProjectVO graphProject = null;

		public ParametersAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}
		

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object selectedCell = graph.getSelectionCell();

				if (selectedCell != null) {	
					graph.getModel().beginUpdate();
					
					mxCell vertice = (mxCell) selectedCell;

					if (isVertex(vertice) && !isStartVertex(vertice) && !isEndVertex(vertice) && !isGeneratedEventVertex(vertice)) {

						//vertice.setStyle(MainView.PARAMETER_VERTEX);
						
						ArrayList<EventInstance> values = this.graphProject.getEventInstanceByVertice(vertice.getId());
						
						ParametersDialog dialog = new ParametersDialog(this.graphProject, values, vertice);

						dialog.setVisible(true);

						this.graphProject.updateEventInstanceByVertices(vertice.getId(), dialog.getValues());

						graph.getModel().endUpdate();

						graphComponent.refresh();
					}
					
				}
			}
		}

	}

	public static class DisplayIdAction extends AbstractAction {

		public DisplayIdAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					
					for (Object selectedCell : selectedCells) {
						mxCell cell = (mxCell) selectedCell;
						String message = (isVertex(cell) ? "Vertex" : "Edge") + " ID";

						JOptionPane.showMessageDialog(null, message + " : " + cell.getId(), message, JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}

	}

	public static class SelectAllEdgesAction extends AbstractAction {

		public SelectAllEdgesAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				graph.selectEdges();
			}
		}

	}

	public static class SelectAllVerticesAction extends AbstractAction {

		public SelectAllVerticesAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				graph.selectVertices();
			}
		}

	}

	public static class DefineMethodTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;
		private String label = null;

		public DefineMethodTemplateAction(String name, GraphProjectVO graphProject, String label) {
			super(name);
			this.graphProject = graphProject;
			this.label = label;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);
			
			String parentPath = "";
			try {
				parentPath = (new File("..")).getCanonicalPath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();
				
				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();
					
					for (Object selectedCell : selectedCells) {
						mxCell vertice = (mxCell) selectedCell;

						if (isVertex(vertice) && !isStartVertex(vertice) && !isEndVertex(vertice) && !isGeneratedEventVertex(vertice)) {
							vertice.setStyle(MainView.EVENT_VERTEX);
							
							if (this.graphProject.getIsWebProject()) {
								if (this.label.contains("java.org.junit.Assert")) {
									if (this.label.contains("assertEquals"))
										vertice.setValue("ASSERT \n assertEquals");
									else if (this.label.contains("assertTrue"))
										vertice.setValue("ASSERT \n assertTrue");
								} else {
									String[] aux = this.label.split("\\(");
									vertice.setValue((aux.length > 0 ? aux[0] : this.label));
								}
							}

							this.graphProject.updateMethodTemplateByVertice(vertice.getId(), this.label);

							String methodTemplateContent = FileUtil.readFile(new File("templates" + File.separator + "robotium-templates" + File.separator + "robotium-methods" + File.separator + this.label.replace(" ", "") + ".java"));

							List<String> values = StringUtil.getValuesWithRegEx(methodTemplateContent, "\\{\\{([a-z]+)\\}\\}");

							if (values != null && !values.isEmpty()) {
								EventPropertiesDialog dialog = new EventPropertiesDialog(this.graphProject, MapUtil.fromList(values));

								dialog.setVisible(true);

								this.graphProject.updateMethodTemplatePropertiesByVertice(vertice.getId(), dialog.getValues());
							}
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class DefineEdgeTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;
		private String label = null;

		public DefineEdgeTemplateAction(String name, GraphProjectVO graphProject, String label) {
			super(name);
			this.graphProject = graphProject;
			this.label = label;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell edge = (mxCell) selectedCell;

						if (!isVertex(edge) && !isGeneratedEdge(edge)) {
							edge.setValue(this.label);

							edge.setStyle(MainView.MARKED_EDGE);

							this.graphProject.updateEdgeTemplateByVertice(edge.getId(), this.label);
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class ClearMethodTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;

		public ClearMethodTemplateAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell vertice = (mxCell) selectedCell;

						if (isVertex(vertice) && !isStartVertex(vertice) && !isEndVertex(vertice) && !isGeneratedEventVertex(vertice)) {
							vertice.setStyle(MainView.NORMAL_VERTEX);

							this.graphProject.removeEventInstanceByVertices(vertice.getId());
							this.graphProject.removeMethodTemplatePropertiesByVertice(vertice.getId());
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class ClearEdgeTemplateAction extends AbstractAction {

		private GraphProjectVO graphProject = null;

		public ClearEdgeTemplateAction(String name, GraphProjectVO graphProject) {
			super(name);
			this.graphProject = graphProject;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);
			mxGraphComponent graphComponent = getGraphComponent(e);

			if (graph != null) {
				Object[] selectedCells = graph.getSelectionCells();

				if (selectedCells != null && selectedCells.length > 0) {
					graph.getModel().beginUpdate();

					for (Object selectedCell : selectedCells) {
						mxCell edge = (mxCell) selectedCell;

						if (!isVertex(edge) && !isGeneratedEdge(edge)) {
							edge.setValue("");
							edge.setStyle("");

							this.graphProject.removeEdgeTemplateByVertice(edge.getId());
						}
					}

					graph.getModel().endUpdate();

					graphComponent.refresh();
				}
			}
		}

	}

	public static class SelectAllAction extends AbstractAction {

		public SelectAllAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mxGraph graph = getGraph(e);

			if (graph != null) {
				graph.selectAll();
			}
		}

	}

	public static final mxGraphComponent getGraphComponent(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof mxGraphComponent) {
			return (mxGraphComponent) source;
		}

		return null;
	}

	public static final mxGraph getGraph(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) source;

			return graphComponent.getGraph();
		}

		return null;
	}

	private static synchronized boolean hasStartOrEndEdges(Object[] selectedCells) {
		for (Object o : selectedCells) {
			mxCell cell = (mxCell) o;

			if (cell.getId().equalsIgnoreCase(MainView.ID_START_VERTEX) || cell.getId().equalsIgnoreCase(MainView.ID_END_VERTEX)) {
				return true;
			}
		}

		return false;
	}

	private static synchronized boolean isVertex(mxCell cell) {
		return cell.isVertex();
	}

	private static synchronized boolean isStartVertex(mxCell cell) {
		return cell.getId().equalsIgnoreCase(MainView.ID_START_VERTEX);
	}

	private static synchronized boolean isEndVertex(mxCell cell) {
		return cell.getId().equalsIgnoreCase(MainView.ID_END_VERTEX);
	}

	private static synchronized boolean isGeneratedEventVertex(mxCell cell) {
		return cell.getStyle().equalsIgnoreCase(MainView.GENERATED_EVENT_VERTEX);
	}

	private static synchronized boolean isGeneratedEdge(mxCell cell) {
		return cell.getStyle().equalsIgnoreCase(MainView.GENERATED_EDGE);
	}

}

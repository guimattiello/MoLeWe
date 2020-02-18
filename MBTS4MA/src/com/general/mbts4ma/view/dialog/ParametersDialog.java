package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableColumnModelEvent;

import com.general.mbts4ma.EventInstance;
import com.general.mbts4ma.Parameter;
import com.general.mbts4ma.view.MainView;
import com.general.mbts4ma.view.framework.util.SpoonUtil;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.svg.ParseException;

import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;

public class ParametersDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tblEventProperties;
	public static ArrayList<String> header = null;

	private GraphProjectVO graphProject = null;
	private ArrayList<EventInstance> values = null;
	private String eventInstanceName = "";
	private mxCell vertice = null;
	public static int lastColumnIndex = 0;

	private void loadHead() {
		header.add("id");
		for (Parameter p : this.values.get(0).getParameters()) {
			header.add(p.getName() + " : " + p.getType());
		}
		if (graphProject.getIsWebProject()) {
			header.add("testCaseName");
			header.add("createdAutomatically");
		}
	}

	private void printTableModelInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("TABLE INFO:\n");
		sb.append("\tgetColumnCount = " + tblEventProperties.getColumnCount() + "\n");
		sb.append("\tgetRowCount = " + tblEventProperties.getRowCount() + "\n");
		sb.append("\t\tgetValueAt(0,0) = " + tblEventProperties.getValueAt(0, 0) + "\n");
		sb.append("\t\tgetValueAt(0,1) = " + tblEventProperties.getValueAt(0, 1) + "\n");
		sb.append("MODEL INFO:\n");
		sb.append("\tgetColumnCount = " + ((DefaultTableModel) this.tblEventProperties.getModel()).getColumnCount()
				+ "\n");
		sb.append("\tgetRowCount = " + ((DefaultTableModel) this.tblEventProperties.getModel()).getRowCount() + "\n");
		sb.append("\t\tgetValueAt(0,0) = " + ((DefaultTableModel) this.tblEventProperties.getModel()).getValueAt(0, 0)
				+ "\n");
		sb.append("\t\tgetValueAt(0,1) = " + ((DefaultTableModel) this.tblEventProperties.getModel()).getValueAt(0, 1)
				+ "\n");
		System.out.println(sb.toString());
	}

	private void eventNameGenerator() {
		StringBuilder sb = new StringBuilder();
		for (String s : ((String) this.vertice.getValue()).toLowerCase().replaceAll("[^\\w\\s]", "").split(" ")) {
			sb.append(s.charAt(0));
		}
		this.eventInstanceName = sb.toString();
	}

	private void buildHeader() {				
		if (this.values != null && !this.values.isEmpty()) {
			this.tblEventProperties.setAutoCreateColumnsFromModel(true);
			DefaultTableModel model = (DefaultTableModel) this.tblEventProperties.getModel();			
			loadHead();
			for (String s : header)
				model.addColumn(s);
			this.tblEventProperties.setModel(model);
			this.tblEventProperties.setAutoCreateColumnsFromModel(false);
		} else {
			header.add("id");
			if (graphProject.getIsWebProject()) {
				String idVertex = this.vertice.getId();
				String methodSignature = this.graphProject.getMethodTemplatesByVertices().get(idVertex);
				String[] split = methodSignature.split("::");		
				String className = split[0];
				if (methodSignature.contains("assertEquals") || methodSignature.contains("assertTrue") || methodSignature.equals("assertFalse")) {
					
					if (methodSignature.contains("assertEquals")) {
						header.add("String expected : String");
						header.add("String real : String");						
					} else if (methodSignature.contains("assertTrue")){
						header.add("boolean condition : boolean");	
					} else if (methodSignature.contains("assertFalse")){
						header.add("boolean condition : boolean");
					}
					
				} else {
					//Alterado o método chamado, pois não estava autalizado o launcher e não achava os métodos abstratos que eram criados
					//CtMethod<?> method = SpoonUtil.getMethodBySignature(SpoonUtil.getSignatureFromMethodTemplate(methodSignature), this.graphProject.getLauncher().getFactory().Class().getAll(), className, this.graphProject.getPageObjects());
					CtMethod<?> method = SpoonUtil.getCtMethodFromMethodSignatureAndClassName(SpoonUtil.getSignatureFromMethodTemplate(methodSignature), className, this.graphProject.getLauncher(), this.graphProject.getPageObjects());
					for (CtParameter<?> param : method.getParameters()) {
						header.add(param.getType() + " " + param.getSimpleName() + " : " + param.getType());
					}
				}
				
				header.add("testCaseName");
				header.add("createdAutomatically");
				
				String[] headerStr = header.toArray(new String[header.size()]);
				this.tblEventProperties.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "-", null } }, headerStr));
				//this.tblEventProperties.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "-", null } }, new String[] { "id", "P1 : String", "testCaseName", "createdAutomatically" }));
			} else {
				header.add("P1 : String");	
				this.tblEventProperties.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "-", null } },
					new String[] { "id", "P1 : String" }));
			}
			this.tblEventProperties.setAutoCreateColumnsFromModel(false);
		}
	}

	private void loadTable() {
		header = new ArrayList<String>();
		buildHeader();

		this.tblEventProperties.getColumnModel().getColumn(0).setPreferredWidth(65);
		for (int i = 1; i < this.tblEventProperties.getColumnCount(); i++) {
			this.tblEventProperties.getColumnModel().getColumn(i).setPreferredWidth(165);
		}

		if (this.getValues() != null && !this.getValues().isEmpty()) {
			String[] addRow = null;
			if (graphProject.getIsWebProject())
				addRow = new String[header.size() + 2];
			else
				addRow = new String[header.size()];
			int pos = 0;
			ArrayList<Parameter> plist = null;
			for (int i = 0; i < this.values.size(); i++) {
				addRow[pos++] = this.eventInstanceName + i;
				plist = this.values.get(i).getParameters();
				for (Parameter p : plist) {
					addRow[pos++] = p.getValue();
				}
				if (graphProject.getIsWebProject()) {
					addRow[pos++] = this.values.get(i).getTestCaseMethodName();
					addRow[pos++] = this.values.get(i).getCreatedAutomatically()+"";
				}
				((DefaultTableModel) this.tblEventProperties.getModel()).addRow(addRow);
				addRow = new String[header.size() + 1];
				pos = 0;
			}
		}
	}

	public ParametersDialog(GraphProjectVO graphProject, ArrayList<EventInstance> values, mxCell vertice) {
		this.graphProject = graphProject;
		this.values = values;
		this.vertice = vertice;

		this.eventNameGenerator();

		this.setTitle("Parameters");

		this.setBounds(100, 100, 560 + 100, 450 + 100);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.tblEventProperties = new JTable(0, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0 ? true : false;
			}

			@Override
			public void columnAdded(TableColumnModelEvent tcme) {
				lastColumnIndex = tcme.getToIndex();
			}
		};
		
		this.tblEventProperties.getTableHeader().addMouseListener(new HeaderSelector(this.values));

		this.tblEventProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.tblEventProperties.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.tblEventProperties.getTableHeader().setReorderingAllowed(false);

		this.contentPanel.add(this.tblEventProperties);
		{
			JPanel buttonPane = new JPanel();
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			JButton btnConfirm = new JButton("");
			btnConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ParametersDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(
					ParametersDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));
			btnConfirm.setToolTipText("Confirm");
			JButton btnCancel = new JButton("");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "Remove Parameters?") == JOptionPane.YES_OPTION) {
						ParametersDialog.this.cancel();
					}
				}
			});
			btnCancel.setIcon(new ImageIcon(
					ParametersDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			btnCancel.setToolTipText("Delete");

			JButton btnAddRow = new JButton("");
			btnAddRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ParametersDialog.this.addRowTable();
				}
			});
			btnAddRow.setIcon(new ImageIcon(
					ParametersDialog.class.getResource("/com/general/mbts4ma/view/framework/images/addrow.png")));
			btnAddRow.setToolTipText("Add row");

			JButton btnDeleteRow = new JButton("");
			btnDeleteRow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ParametersDialog.this.deleteRowTable();
				}
			});
			btnDeleteRow.setIcon(new ImageIcon(
					ParametersDialog.class.getResource("/com/general/mbts4ma/view/framework/images/deleterow.png")));
			btnDeleteRow.setToolTipText("Delete selected row");

			JButton btnAddColumn = new JButton("");
			btnAddColumn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ParametersDialog.this.addColumnTable();
				}
			});
			btnAddColumn.setIcon(new ImageIcon(
					ParametersDialog.class.getResource("/com/general/mbts4ma/view/framework/images/addcolumn.png")));
			btnAddColumn.setToolTipText("Add column");

			JButton btnDeleteColumn = new JButton("");
			btnDeleteColumn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ParametersDialog.this.deleteColumnTable();
				}
			});
			btnDeleteColumn.setIcon(new ImageIcon(
					ParametersDialog.class.getResource("/com/general/mbts4ma/view/framework/images/deletecolumn.png")));
			btnDeleteColumn.setToolTipText("Delete selected column");

			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup().addContainerGap()
							.addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
							.addComponent(btnAddRow, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnDeleteRow, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnAddColumn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnDeleteColumn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addContainerGap()));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane
					.createSequentialGroup().addContainerGap()
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnAddRow, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnDeleteRow, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnAddColumn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnDeleteColumn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

		this.contentPanel.add(new JScrollPane(this.tblEventProperties));

		this.loadTable();
	}

	private boolean validType(String value, int col) {
		String type = header.get(col).split(" : ")[1];
		switch (type) {
		case "int":
			try {
				Integer.parseInt(value);
			} catch (ParseException | NumberFormatException e) {
				return false;
			}
			break;
		case "float":
			try {
				Float.parseFloat(value);
			} catch (ParseException | NumberFormatException e) {
				return false;
			}
			break;
		}
		return true;
	}

	private boolean validateTable() {
		int columnCount = 0;
		if (graphProject.getIsWebProject())
			columnCount = this.tblEventProperties.getColumnCount()-2;
		else
			columnCount = this.tblEventProperties.getColumnCount();
		
		for (int i = 0; i < this.tblEventProperties.getRowCount(); i++) {
			for (int j = 1; j < columnCount; j++) {
				if (!this.validType((String) this.tblEventProperties.getValueAt(i, j), j))
					return false;
			}
		}
		return true;
	}

	private void confirm() {
		if (!header.isEmpty()) {
			if (!validateTable()) {
				JOptionPane.showMessageDialog(null, "Incompatibility between type and value", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		vertice.setStyle(MainView.PARAMETER_VERTEX);
		this.getValues().clear();
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		String eventId = "";
		Parameter p = null;
		EventInstance ei = new EventInstance();
		for (int i = 0; i < this.tblEventProperties.getRowCount(); i++) {
			eventId = this.eventInstanceName + i;
			for (int j = 1; j < (graphProject.getIsWebProject() ? header.size()-2 : header.size()); j++) {
				p = new Parameter(header.get(j).split(" : ")[1], (String) this.tblEventProperties.getValueAt(i, j),
						header.get(j).split(" : ")[0]);
				parameters.add(p);
			}
			if (graphProject.getIsWebProject()) {
				ei.setTestCaseMethodName((String)this.tblEventProperties.getValueAt(i, header.size()-2));
				ei.setCreatedAutomatically(this.tblEventProperties.getValueAt(i, header.size()-1).equals("true") ? Boolean.TRUE : Boolean.FALSE);
			}
			ei.setId(eventId);
			ei.setParameters(parameters);
			this.values.add(ei);
			parameters = new ArrayList<Parameter>();
			ei = new EventInstance();
		}
		this.dispose();
	}

	private void cancel() {
		vertice.setStyle(MainView.NORMAL_VERTEX);
		this.getValues().clear();
		this.dispose();
	}

	private void addRowTable() {
		if (graphProject.getIsWebProject()) {
			((DefaultTableModel) this.tblEventProperties.getModel()).addRow(new Object[] { "-" });
			this.tblEventProperties.getModel().setValueAt("false", this.tblEventProperties.getModel().getRowCount()-1, this.tblEventProperties.getModel().getColumnCount()-1);
		} else 
			((DefaultTableModel) this.tblEventProperties.getModel()).addRow(new Object[] { "-" });
	}

	private void deleteRowTable() {
		int row = this.tblEventProperties.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(null, "You must select a row to delete.", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		// LINHA PRESENTE NO ARRAY EVENTINSTANCE
		if (!this.values.isEmpty()) {
			String eventId = (String) this.tblEventProperties.getValueAt(row, 0);
			EventInstance targetEvent = new EventInstance();
			for (EventInstance ei : this.values) {
				if (ei.getId().equals(eventId)) {
					targetEvent = ei;
				}
			}
			this.values.remove(targetEvent);
		}
		((DefaultTableModel) this.tblEventProperties.getModel()).removeRow(row);
	}

	private void addColumnTable() {
		DefaultTableModel model = (DefaultTableModel) this.tblEventProperties.getModel();
		TableColumn col = new TableColumn(model.getColumnCount());
		int indexColumn = this.tblEventProperties.getColumnCount();
		col.setHeaderValue("P" + indexColumn + " : String");
		this.tblEventProperties.addColumn(col);
		ParametersDialog.header.add("P" + indexColumn + " : String");
		model.addColumn("P" + indexColumn + " : String", new Object[] {});
	}

	private void deleteColumnTable() {
		int col = this.tblEventProperties.getSelectedColumn();
		if (col == -1) {
			JOptionPane.showMessageDialog(null, "You must select a column to delete.", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if (col == 0 || col == 1) {
			JOptionPane.showMessageDialog(null, "You must select a valid column to delete.", "Warning",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		TableColumn column = this.tblEventProperties.getTableHeader().getColumnModel().getColumn(col);
		// if (!header.contains(column.getHeaderValue().toString())) {
		// printTableModelInfo();
		// printHeader();
		// System.out.println("\nIF QUE N�O REMOVE DO HEADER");
		// System.out.println(
		// "\nVALOR DO COLUMN QUE VEM DO HEADER = " +
		// column.getHeaderValue().toString().split(" : ")[0]);
		// this.tblEventProperties.removeColumn(column);
		// return;
		// }
		if (column != null) {
			this.tblEventProperties.removeColumn(column);
			// COLUNA PRESENTE NO HEADER, MAS N�O NO ARRAY DE EVENTINSTANCE
			if (header.get(col).equals(column.getHeaderValue().toString())) {
				header.remove(col);
				// COLUNA PRESENTE NO HEADER E NO ARRAY DE EVENTINSTANCE
				//printHeader();
				//printValues();
				if (!this.values.isEmpty()) {
					for (EventInstance e : this.values) {
						e.getParameters().remove(col - 1);
					}
				}
			}
		}
	}

	public ArrayList<EventInstance> getValues() {
		if (this.values == null) {
			this.values = new ArrayList<EventInstance>();
		}
		return this.values;
	}
}

class HeaderSelector extends MouseAdapter {
	String[] typeStrings = {"String", "int", "float"};
	HeaderEditor editor;
	ArrayList<EventInstance> values;

	public HeaderSelector(ArrayList<EventInstance> values) {
		this.editor = new HeaderEditor(this, typeStrings);
		this.values = values;
	}

	public void mousePressed(MouseEvent e) {
		if ((JTableHeader) e.getSource() == null)
			return;
		JTableHeader th = (JTableHeader) e.getSource();
		Point p = e.getPoint();
		int col = getColumn(th, p);
		TableColumn column = th.getColumnModel().getColumn(col);
		String oldValue = (String) column.getHeaderValue();
		Object value = editor.showEditor(th, col, oldValue, values);
		column.setHeaderValue(value);
		th.resizeAndRepaint();
	}

	private int getColumn(JTableHeader th, Point p) {
		TableColumnModel model = th.getColumnModel();
		for (int col = 1; col < model.getColumnCount(); col++)
			if (th.getHeaderRect(col).contains(p)) {
				return col;
			}
		return -1;
	}
}

class HeaderEditor {
	HeaderSelector selector;
	String[] items;

	public HeaderEditor(HeaderSelector hs, String[] typeStrings) {
		items = typeStrings;
	}

	public Object showEditor(Component parent, int col, String currentValue, ArrayList<EventInstance> values) {
		JPanel myPanel = new JPanel();
		JTextField inputName = new JTextField(10);
		inputName.setText(currentValue.split(" : ")[0]);
		JComboBox<String> inputTye = new JComboBox<String>(items);
		inputTye.setSelectedItem(currentValue.split(" : ")[1]);
		myPanel.add(new JLabel("Parameter name:"));
		myPanel.add(inputName);
		myPanel.add(new JLabel("Parameter type:"));
		myPanel.add(inputTye);

		String title = "Select name and type for Parameter " + (col) + ":";
		String parameterType = currentValue.split(" : ")[1];
		String parameterName = currentValue.split(" : ")[0];

		int result = JOptionPane.showConfirmDialog(parent, myPanel, title, JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			if (!inputName.getText().isEmpty()) {
				parameterName = inputName.getText();
				parameterType = (String) inputTye.getSelectedItem();
			}
			if (ParametersDialog.header.contains(currentValue))
				ParametersDialog.header.set(col, parameterName + " : " + parameterType);
			else
				ParametersDialog.header.add(parameterName + " : " + parameterType);
		}
		return parameterName + " : " + parameterType;
	}
}
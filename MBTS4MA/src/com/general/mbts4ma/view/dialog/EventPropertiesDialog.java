package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.general.mbts4ma.view.framework.bo.AndroidProjectBO;
import com.general.mbts4ma.view.framework.util.MapUtil;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;

public class EventPropertiesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tblEventProperties;

	private GraphProjectVO graphProject = null;
	private Map<String, String> values = null;

	private void loadTable() {
		DefaultTableModel model = (DefaultTableModel) this.tblEventProperties.getModel();

		model.setRowCount(0);
		model.setColumnCount(0);

		for (String columnName : new String[] { "Property", "Value" }) {
			model.addColumn(columnName);
		}

		for (int i = 0; i < this.tblEventProperties.getColumnCount(); i++) {
			this.tblEventProperties.getColumnModel().getColumn(i).setPreferredWidth(165);
		}

		Iterator<String> iValues = this.values.keySet().iterator();

		while (iValues.hasNext()) {
			String key = iValues.next();
			String value = this.values.get(key);

			model.addRow(new String[] { key, value });
		}
	}

	public EventPropertiesDialog(GraphProjectVO graphProject, Map<String, String> values) {
		this.graphProject = graphProject;
		this.values = values;

		this.setTitle("Event Properties");

		this.setBounds(100, 100, 360, 250);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.tblEventProperties = new JTable();
		this.tblEventProperties.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.tblEventProperties.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

		this.contentPanel.add(this.tblEventProperties);
		{
			JPanel buttonPane = new JPanel();
			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			JButton btnConfirm = new JButton("");
			btnConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EventPropertiesDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(EventPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));
			btnConfirm.setToolTipText("Confirm");
			JButton btnCancel = new JButton("");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EventPropertiesDialog.this.cancel();
				}
			});
			btnCancel.setIcon(new ImageIcon(EventPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			btnCancel.setToolTipText("Cancel");

			JButton btnSelectStringLabel = new JButton("");
			btnSelectStringLabel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EventPropertiesDialog.this.selectStringLabel();
				}
			});
			btnSelectStringLabel.setIcon(new ImageIcon(EventPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/label.png")));
			btnSelectStringLabel.setToolTipText("Select string label");
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE).addComponent(btnSelectStringLabel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap()));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addComponent(btnSelectStringLabel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

		this.loadTable();
	}

	private void confirm() {
		this.getValues().clear();

		for (int i = 0; i < this.tblEventProperties.getRowCount(); i++) {
			this.getValues().put((String) this.tblEventProperties.getModel().getValueAt(i, 0), (String) this.tblEventProperties.getModel().getValueAt(i, 1));
		}

		this.dispose();
	}

	private void cancel() {
		this.getValues().clear();

		this.dispose();
	}

	private void selectStringLabel() {
		int selectedRow = this.tblEventProperties.getSelectedRow();

		if (selectedRow != -1) {
			Map<String, Object> values = AndroidProjectBO.getStrings(this.graphProject.getAndroidProjectPath(), null);

			String value = (String) JOptionPane.showInputDialog(null, "Select the string label", "Attention", JOptionPane.QUESTION_MESSAGE, null, values.values().toArray(), values.values().toArray()[0]);

			if (value != null && !"".equalsIgnoreCase(value)) {
				this.tblEventProperties.getModel().setValueAt(MapUtil.getKeyFromValue(values, value), selectedRow, 1);
			}
		}
	}

	public Map<String, String> getValues() {
		if (this.values == null) {
			this.values = new LinkedHashMap<String, String>();
		}

		return this.values;
	}

}

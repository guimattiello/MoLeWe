package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.general.mbts4ma.view.framework.bo.AndroidProjectBO;
import com.general.mbts4ma.view.framework.util.DatabaseRegression;
import com.general.mbts4ma.view.framework.util.PageObject;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

public class NewEdgeEventInstancePriorityDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	ArrayList<String> eventInstancesToUse;

	private JComboBox comboPriority1;
	private JComboBox comboPriority2;
	private JComboBox comboPriority3;
	private JComboBox comboPriority4;
	private JTextField txtScriptDatabasePath;
	
	private boolean isUpdate = false;
	
	private GraphProjectVO graphProject = null;

	public GraphProjectVO getGraphProject() {
		return this.graphProject;
	}

	public NewEdgeEventInstancePriorityDialog(GraphProjectVO graphProject) {
		this.graphProject = graphProject;

		if (this.graphProject != null) {
			this.isUpdate = true;
		}

		ArrayList<String> tcNames = new ArrayList<String>();
		
		for (String tcname : this.graphProject.getImportedTestCaseNames()) {
			tcNames.add(tcname);
		}
		
		for (String tcname : graphProject.getDistinctGroupNameOfEventInstances()) {
			tcNames.add(tcname);
		}
		
		Object[] testCaseNames = tcNames.toArray();
		
		eventInstancesToUse = new ArrayList<String>();
		
		this.setTitle("Event Instances to Use in Edge");

		this.setBounds(100, 100, 300, 400);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		
		JLabel lblDBHost = new JLabel("Priority 1");
		lblDBHost.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.comboPriority1 = new JComboBox(testCaseNames);
		this.comboPriority1.setFont(new Font("Verdana", Font.PLAIN, 12));
		comboPriority1.setSelectedIndex(-1);
		
		JLabel lblDBName= new JLabel("Priority 2");
		lblDBName.setFont(new Font("Verdana", Font.PLAIN, 12));		

		this.comboPriority2 = new JComboBox(testCaseNames);
		this.comboPriority2.setFont(new Font("Verdana", Font.PLAIN, 12));
		comboPriority2.setSelectedIndex(-1);
		
		JLabel lblDBUser = new JLabel("Priority 3");
		lblDBUser.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.comboPriority3 = new JComboBox(testCaseNames);
		this.comboPriority3.setFont(new Font("Verdana", Font.PLAIN, 12));
		comboPriority3.setSelectedIndex(-1);
		
		JLabel lblDBPassword = new JLabel("Priority 4");
		lblDBPassword.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.comboPriority4 = new JComboBox(testCaseNames);
		this.comboPriority4.setFont(new Font("Verdana", Font.PLAIN, 12));
		comboPriority4.setSelectedIndex(-1);
		
		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);

		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
			addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().
				addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
					
					addComponent(lblDBHost, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.comboPriority1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDBName, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.comboPriority2, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDBUser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.comboPriority3, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDBPassword, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.comboPriority4, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)					
				).addContainerGap()
			)
		);
		
		
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
			addGroup(gl_contentPanel.createSequentialGroup().
				addContainerGap().
				addComponent(lblDBHost).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.comboPriority1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDBName).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.comboPriority2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDBUser).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.comboPriority3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDBPassword).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.comboPriority4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				
				addContainerGap(49, Short.MAX_VALUE)
			)
		);
		this.contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();

			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton btnConfirm = new JButton("");
			btnConfirm.setToolTipText("Confirm");
			btnConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					NewEdgeEventInstancePriorityDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(NewEdgeEventInstancePriorityDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));

			JButton btnCancel = new JButton("");
			btnCancel.setToolTipText("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					NewEdgeEventInstancePriorityDialog.this.cancel();
				}
			});
			btnCancel.setIcon(new ImageIcon(NewEdgeEventInstancePriorityDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap(288, Short.MAX_VALUE)));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

	}

	private void confirm() {
		if (!this.isUpdate) {
			this.graphProject = new GraphProjectVO();
		}
		
		if (this.comboPriority1.getSelectedIndex() != -1)
			this.eventInstancesToUse.add((String) this.comboPriority1.getSelectedItem());
		if (this.comboPriority2.getSelectedIndex() != -1)
			this.eventInstancesToUse.add((String) this.comboPriority2.getSelectedItem());
		if (this.comboPriority3.getSelectedIndex() != -1)
			this.eventInstancesToUse.add((String) this.comboPriority3.getSelectedItem());
		if (this.comboPriority4.getSelectedIndex() != -1)
			this.eventInstancesToUse.add((String) this.comboPriority4.getSelectedItem());		
				
		this.dispose();
	}

	private void cancel() {
		if (!this.isUpdate) {
			this.graphProject = null;
		}

		this.dispose();
	}
	
	public ArrayList<String> getEventInstancesToUse() {
		
		return this.eventInstancesToUse;
	}
	
}

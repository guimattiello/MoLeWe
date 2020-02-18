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

public class WebProjectDatabasePropertiesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JButton btnSelectScriptDatabasePath;

	private JTextField txtDBHost;
	private JTextField txtDBName;
	private JTextField txtDBUser;
	private JTextField txtDBPassword;
	private JTextField txtScriptDatabasePath;
	
	private String regressionScriptContent;
	
	private boolean isUpdate = false;
	
	private GraphProjectVO graphProject = null;

	public GraphProjectVO getGraphProject() {
		return this.graphProject;
	}

	public WebProjectDatabasePropertiesDialog(GraphProjectVO graphProject) {
		this.graphProject = graphProject;

		if (this.graphProject != null) {
			this.isUpdate = true;
		}

		this.setTitle("Postgres Database Properties");

		this.setBounds(100, 100, 300, 400);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		JLabel lblScriptDatabasePath = new JLabel("Database Regression Script Path");
		lblScriptDatabasePath.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtScriptDatabasePath = new JTextField();
		this.txtScriptDatabasePath.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtScriptDatabasePath.setColumns(10);
		
		JLabel lblDBHost = new JLabel("Postgres Database Host");
		lblDBHost.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtDBHost = new JTextField();
		this.txtDBHost.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtDBHost.setColumns(10);
		
		JLabel lblDBName= new JLabel("Database Name");
		lblDBName.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtDBName = new JTextField();
		this.txtDBName.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtDBName.setColumns(10);
		
		JLabel lblDBUser = new JLabel("Database User");
		lblDBUser.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtDBUser = new JTextField();
		this.txtDBUser.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtDBUser.setColumns(10);
		
		JLabel lblDBPassword = new JLabel("Database Password");
		lblDBPassword.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtDBPassword = new JTextField();
		this.txtDBPassword.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtDBPassword.setColumns(10);
		

		this.btnSelectScriptDatabasePath = new JButton("");
		this.btnSelectScriptDatabasePath.setToolTipText("Select the database regression script");
		this.btnSelectScriptDatabasePath.setIcon(new ImageIcon(WebProjectDatabasePropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/search.png")));
		this.btnSelectScriptDatabasePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebProjectDatabasePropertiesDialog.this.selectWebProjectTestPath();
			}
		});

		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);

		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
			addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().
				addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
					addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().
						addComponent(this.txtScriptDatabasePath, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE).
						addPreferredGap(ComponentPlacement.UNRELATED).
						addComponent(this.btnSelectScriptDatabasePath, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).
						addPreferredGap(ComponentPlacement.RELATED)						
					).
					
					addComponent(lblDBHost, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.txtDBHost, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDBName, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.txtDBName, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDBUser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.txtDBUser, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDBPassword, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE).
					addComponent(this.txtDBPassword, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblScriptDatabasePath, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)					
				).addContainerGap()
			)
		);
		
		
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
			addGroup(gl_contentPanel.createSequentialGroup().
				addContainerGap().
				addComponent(lblDBHost).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtDBHost, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDBName).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtDBName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDBUser).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtDBUser, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDBPassword).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtDBPassword, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				
				addComponent(lblScriptDatabasePath, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
					addGroup(gl_contentPanel.createSequentialGroup().
						addComponent(this.txtScriptDatabasePath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
						addGap(12)						
					).					
					addComponent(this.btnSelectScriptDatabasePath, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
				).
				
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
					WebProjectDatabasePropertiesDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(WebProjectDatabasePropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));

			JButton btnCancel = new JButton("");
			btnCancel.setToolTipText("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WebProjectDatabasePropertiesDialog.this.cancel();
				}
			});
			btnCancel.setIcon(new ImageIcon(WebProjectDatabasePropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap(288, Short.MAX_VALUE)));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

		this.loadProjectProperties();
	}
	
	public DatabaseRegression getDatabaseConfig() {
		DatabaseRegression dr = new DatabaseRegression();
		
		dr.setDbHost(this.txtDBHost.getText());
		dr.setDbName(this.txtDBName.getText());
		dr.setDbUser(this.txtDBUser.getText());
		dr.setDbPassword(this.txtDBPassword.getText());
		dr.setRegressionScriptPath(this.txtScriptDatabasePath.getText());
		dr.refreshContentByPath();
		
		return dr;
	}

	private void loadProjectProperties() {
		if (this.graphProject.getDatabaseRegression() != null) {
			this.txtDBHost.setText(this.graphProject.getDatabaseRegression().getDbHost());
			this.txtDBName.setText(this.graphProject.getDatabaseRegression().getDbName());
			this.txtDBUser.setText(this.graphProject.getDatabaseRegression().getDbUser());
			this.txtDBPassword.setText(this.graphProject.getDatabaseRegression().getDbPassword());
			this.txtScriptDatabasePath.setText(this.graphProject.getDatabaseRegression().getRegressionScriptPath());			
		}
	}

	private void selectWebProjectTestPath() {
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setDialogTitle("Select script database path");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			String path = fileChooser.getSelectedFile().toString();

			this.txtScriptDatabasePath.setText(path);
		}
	}

	private void confirm() {
		if (!this.isUpdate) {
			this.graphProject = new GraphProjectVO();
		}
				
		this.dispose();
	}

	private void cancel() {
		if (!this.isUpdate) {
			this.graphProject = null;
		}

		this.dispose();
	}
}

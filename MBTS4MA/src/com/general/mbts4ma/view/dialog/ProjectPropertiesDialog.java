package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import com.general.mbts4ma.view.framework.bo.AndroidProjectBO;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;

public class ProjectPropertiesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextArea txtDescription;
	private JRadioButton rbtRobotium;
	private JRadioButton rbtOther;
	private ButtonGroup grpFramework;
	private JCheckBox cbxItsAndroidProject;
	private JTextField txtAndroidProjectPath;
	private JButton btnSelectAndroidProject;
	private JButton btnVerifyAndroidProject;
	private JTextField txtApplicationPackage;
	private JComboBox cmbMainTestingActivity;

	private boolean isUpdate = false;

	private GraphProjectVO graphProject = null;

	public GraphProjectVO getGraphProject() {
		return this.graphProject;
	}

	public ProjectPropertiesDialog(GraphProjectVO graphProject) {
		this.graphProject = graphProject;

		if (this.graphProject != null) {
			this.isUpdate = true;
		}

		this.setTitle("Properties");

		this.setBounds(100, 100, 500, 500);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtName = new JTextField();
		this.txtName.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtName.setColumns(10);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("Verdana", Font.PLAIN, 12));
		
		JLabel lblFramework = new JLabel("Framework");
		lblFramework.setFont(new Font("Verdana", Font.PLAIN, 12));
		
		this.rbtRobotium = new JRadioButton("Robotium");
		this.rbtRobotium.setFont(new Font("Verdana", Font.PLAIN, 12));
		
		this.rbtOther = new JRadioButton("Other");
		this.rbtOther.setFont(new Font("Verdana", Font.PLAIN, 12));
		
		this.grpFramework = new ButtonGroup();
		this.grpFramework.add(this.rbtRobotium);
		this.grpFramework.add(this.rbtOther);
		
		
		this.cbxItsAndroidProject = new JCheckBox("It´s an Android Project");
		this.cbxItsAndroidProject.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.cbxItsAndroidProject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				ProjectPropertiesDialog.this.disableAndroidFields();
			}
		});

		JLabel lblAndroidProjectPath = new JLabel("Android Project Path");
		lblAndroidProjectPath.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtAndroidProjectPath = new JTextField();
		this.txtAndroidProjectPath.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtAndroidProjectPath.setColumns(10);

		this.txtDescription = new JTextArea();
		this.txtDescription.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtDescription.setWrapStyleWord(true);
		this.txtDescription.setLineWrap(true);
		this.txtDescription.setRows(4);

		this.btnSelectAndroidProject = new JButton("");
		this.btnSelectAndroidProject.setToolTipText("Select project");
		this.btnSelectAndroidProject.setIcon(new ImageIcon(ProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/search.png")));
		this.btnSelectAndroidProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProjectPropertiesDialog.this.selectAndroidProjectPath();
			}
		});

		btnVerifyAndroidProject = new JButton("");
		btnVerifyAndroidProject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ProjectPropertiesDialog.this.verifyAndroidProject();
			}
		});
		btnVerifyAndroidProject.setToolTipText("Verify project");
		btnVerifyAndroidProject.setIcon(new ImageIcon(ProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/verify.png")));

		JLabel lblApplicationPackage = new JLabel("Application Package");
		lblApplicationPackage.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtApplicationPackage = new JTextField();
		this.txtApplicationPackage.setEditable(false);
		this.txtApplicationPackage.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtApplicationPackage.setColumns(10);

		JLabel lblMainTestingActivity = new JLabel("Main Testing Activity");
		lblMainTestingActivity.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.cmbMainTestingActivity = new JComboBox();
		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().addComponent(this.txtAndroidProjectPath, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSelectAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnVerifyAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)).addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE).addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(lblDescription).addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE).addComponent(lblFramework, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(this.rbtRobotium, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(this.rbtOther, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(this.cbxItsAndroidProject, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE).addComponent(lblAndroidProjectPath, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(lblApplicationPackage, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(lblMainTestingActivity, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(this.txtApplicationPackage, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE).addComponent(this.cmbMainTestingActivity, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblFramework, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addComponent(rbtRobotium, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addComponent(rbtOther, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addComponent(this.cbxItsAndroidProject, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addComponent(lblAndroidProjectPath, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(this.txtAndroidProjectPath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(12).addComponent(lblApplicationPackage, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtApplicationPackage, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblMainTestingActivity, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cmbMainTestingActivity, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)).addComponent(btnVerifyAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).addComponent(this.btnSelectAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)).addContainerGap(49, Short.MAX_VALUE)));
		this.contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();

			this.getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton btnConfirm = new JButton("");
			btnConfirm.setToolTipText("Confirm");
			btnConfirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProjectPropertiesDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(ProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));

			JButton btnCancel = new JButton("");
			btnCancel.setToolTipText("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ProjectPropertiesDialog.this.cancel();
				}
			});
			btnCancel.setIcon(new ImageIcon(ProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap(288, Short.MAX_VALUE)));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

		this.disableAndroidFields();
		this.loadProjectProperties();
	}

	private void loadProjectProperties() {
		if (this.graphProject != null) {
			this.txtName.setText(this.graphProject.getName());
			this.txtDescription.setText(this.graphProject.getDescription());
			
			if(this.graphProject.getFramework().equals("robotium")){
				this.rbtRobotium.setSelected(true);
			} else {
				this.rbtOther.setSelected(true);
			}
			
			this.cbxItsAndroidProject.setSelected(this.graphProject.getItsAndroidProject());		
			
			this.txtAndroidProjectPath.setText(this.graphProject.getAndroidProjectPath());

			this.loadAndroidProjectInfo(this.graphProject.getAndroidProjectPath());
		}
	}

	private void selectAndroidProjectPath() {
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setDialogTitle("Select Android project path");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			String path = fileChooser.getSelectedFile().toString();

			this.txtAndroidProjectPath.setText(path);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadAndroidProjectInfo(String path) {
		this.txtApplicationPackage.setText(null);

		this.cmbMainTestingActivity.removeAllItems();

		if (AndroidProjectBO.isAndroidProject(path)) {
			String applicationPackage = AndroidProjectBO.getPackage(path);
			List<String> activities = AndroidProjectBO.getActivities(path);

			this.txtApplicationPackage.setText(applicationPackage);

			this.cmbMainTestingActivity.setModel(new DefaultComboBoxModel(activities.toArray()));
		}
	}

	private void verifyAndroidProject() {
		if (this.txtAndroidProjectPath.getText() != null && !"".equalsIgnoreCase(this.txtAndroidProjectPath.getText())) {
			String path = this.txtAndroidProjectPath.getText();

			this.loadAndroidProjectInfo(path);
		} else {
			this.txtApplicationPackage.setText(null);

			this.cmbMainTestingActivity.removeAllItems();
		}
	}

	private void confirm() {
		if (!this.isUpdate) {
			this.graphProject = new GraphProjectVO();
		}

		this.graphProject.setName(this.txtName.getText());
		this.graphProject.setDescription(this.txtDescription.getText());
		this.graphProject.setItsAndroidProject(this.cbxItsAndroidProject.isSelected());
		
		if(this.rbtRobotium.isSelected()){
			this.graphProject.setFramework("robotium");
		} else {
			this.graphProject.setFramework("other");
		}

		String path = this.txtAndroidProjectPath.getText();

		//this.loadAndroidProjectInfo(path);

		if (AndroidProjectBO.isAndroidProject(path)) {
			this.graphProject.setAndroidProjectPath(path);
			this.graphProject.setApplicationPackage(this.txtApplicationPackage.getText());
			this.graphProject.setMainTestingActivity((String) this.cmbMainTestingActivity.getSelectedItem());
		} else {
			this.graphProject.setAndroidProjectPath(null);
			this.graphProject.setApplicationPackage(null);
			this.graphProject.setMainTestingActivity(null);
		}

		this.dispose();
	}

	private void cancel() {
		if (!this.isUpdate) {
			this.graphProject = null;
		}

		this.dispose();
	}
	
	private void disableAndroidFields(){
		boolean arg;
		
		if(this.cbxItsAndroidProject.isSelected()){
			arg = true;
		} else {
			arg = false;
		}
		this.txtAndroidProjectPath.setEditable(arg);
		this.txtApplicationPackage.setEditable(arg);
		this.cmbMainTestingActivity.setEnabled(arg);
		this.btnSelectAndroidProject.setEnabled(arg);
		this.btnVerifyAndroidProject.setEnabled(arg);
	}
}

package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import com.general.mbts4ma.view.framework.util.TestClass;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;

import spoon.Launcher;
import spoon.reflect.CtModelImpl;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.filter.AnnotationFilter;
import spoon.reflect.visitor.filter.TypeFilter;

public class WebProjectPropertiesDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextArea txtDescription;
	private JTextField txtWebProjectURL;
	private JTextField txtWebProjectPageObject;
	private JTextField txtWebProjectTestPath;
	private JButton btnSelectWebProjectTestPath;
	private JTextField txtApplicationPackage;
	private JComboBox cmbMainTestingActivity;
	private JTable tablePageObjects;
	private JButton btnSelectPageObject;
	private JButton btnUpdatePageObject;
	private JButton btnDatabaseProperties;
	
	private JTextField txtDBHost;
	private JTextField txtDBName;
	private JTextField txtDBUser;
	private JTextField txtDBPassword;
	
	private boolean isUpdate = false;

	private Launcher spoonLauncher;
	
	private List<PageObject> pageObjects;
	private DatabaseRegression databaseRegression;
	
	private GraphProjectVO graphProject = null;

	public GraphProjectVO getGraphProject() {
		return this.graphProject;
	}

	public WebProjectPropertiesDialog(GraphProjectVO graphProject) {
		this.graphProject = graphProject;

		if (this.graphProject != null) {
			this.isUpdate = true;
		}

		this.setTitle("Properties");

		this.setBounds(100, 100, 500, 600);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.pageObjects = new ArrayList<PageObject>();
		this.databaseRegression = new DatabaseRegression();
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtName = new JTextField();
		this.txtName.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtName.setColumns(10);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("Verdana", Font.PLAIN, 12));

		JLabel lblWebProjectTestPath = new JLabel("Web Project Test Path");
		lblWebProjectTestPath.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtWebProjectTestPath = new JTextField();
		this.txtWebProjectTestPath.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtWebProjectTestPath.setColumns(10);
		
		
		
		JLabel lblDBHost = new JLabel("Database Host");
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
		
		this.btnDatabaseProperties = new JButton("");
		this.btnDatabaseProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebProjectPropertiesDialog.this.databaseProperties();
			}
		});
		this.btnDatabaseProperties.setToolTipText("Insert database regression script");
		this.btnDatabaseProperties.setText("Config/Upload database regression script");


		JLabel lblWebProjectURL = new JLabel("Web Project URL (ex.: http://localhost:8080/projectname/)");
		lblWebProjectURL.setFont(new Font("Verdana", Font.PLAIN, 12));
		
		this.txtWebProjectURL = new JTextField();
		this.txtWebProjectURL.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtWebProjectURL.setColumns(10);
		this.txtWebProjectURL.setText("http://");
		
		JLabel lblWebProjectPageObject = new JLabel("Web Project Page Object");
		lblWebProjectPageObject.setFont(new Font("Verdana", Font.PLAIN, 12));
		
		this.txtWebProjectPageObject = new JTextField();
		this.txtWebProjectPageObject.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtWebProjectPageObject.setColumns(10);
		
		this.txtDescription = new JTextArea();
		this.txtDescription.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtDescription.setWrapStyleWord(true);
		this.txtDescription.setLineWrap(true);
		this.txtDescription.setRows(4);

		this.btnSelectWebProjectTestPath = new JButton("");
		this.btnSelectWebProjectTestPath.setToolTipText("Select project test class");
		this.btnSelectWebProjectTestPath.setIcon(new ImageIcon(WebProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/search.png")));
		this.btnSelectWebProjectTestPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebProjectPropertiesDialog.this.selectWebProjectTestPath();
			}
		});

		JButton btnRemovePageObject = new JButton("");
		btnRemovePageObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebProjectPropertiesDialog.this.removePageObject();
			}
		});
		btnRemovePageObject.setToolTipText("Remove Page Object File");
		btnRemovePageObject.setIcon(new ImageIcon(WebProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/remove.png")));

		JLabel lblApplicationPackage = new JLabel("Application Package");
		lblApplicationPackage.setFont(new Font("Verdana", Font.PLAIN, 12));

		this.txtApplicationPackage = new JTextField();
		this.txtApplicationPackage.setEditable(false);
		this.txtApplicationPackage.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtApplicationPackage.setColumns(10);

		JLabel lblMainTestingActivity = new JLabel("Main Testing Activity");
		lblMainTestingActivity.setFont(new Font("Verdana", Font.PLAIN, 12));

		DefaultTableModel model = new DefaultTableModel();
		this.tablePageObjects = new JTable(model){

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }
        };
        this.tablePageObjects.setPreferredScrollableViewportSize(this.tablePageObjects.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(this.tablePageObjects);
        
		model.addColumn("Page Object");
		model.addColumn("is a PageObject class?");
		
		this.btnSelectPageObject = new JButton("");
		this.btnSelectPageObject.setToolTipText("Select New Page Objects File");
		this.btnSelectPageObject.setIcon(new ImageIcon(WebProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/add.png")));
		this.btnSelectPageObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebProjectPropertiesDialog.this.selectPageObjectPath();
			}
		});
		
		this.btnUpdatePageObject = new JButton("");
		this.btnUpdatePageObject.setToolTipText("Update Page Objects Content");
		this.btnUpdatePageObject.setIcon(new ImageIcon(WebProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/refresh.png")));
		this.btnUpdatePageObject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebProjectPropertiesDialog.this.updatePageObjectContent();
			}
		});
		
		this.cmbMainTestingActivity = new JComboBox();
		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
		//gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().addComponent(this.txtAndroidProjectPath, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(this.btnSelectAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnVerifyAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)).addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE).addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(lblDescription).addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE).addComponent(lblWebProjectURL, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE).addComponent(this.txtWebProjectURL, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(lblWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE).addComponent(this.txtWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(lblAndroidProjectPath, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(lblApplicationPackage, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(lblMainTestingActivity, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).addComponent(this.txtApplicationPackage, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE).addComponent(this.cmbMainTestingActivity, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		//gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblWebProjectURL).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtWebProjectURL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblWebProjectPageObject).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblAndroidProjectPath, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addComponent(this.txtAndroidProjectPath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addGap(12).addComponent(lblApplicationPackage, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtApplicationPackage, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblMainTestingActivity, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.cmbMainTestingActivity, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)).addComponent(btnVerifyAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).addComponent(this.btnSelectAndroidProject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)).addContainerGap(49, Short.MAX_VALUE)));
		//gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()).addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE).addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(lblDescription).addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE).addComponent(lblWebProjectURL, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE).addComponent(this.txtWebProjectURL, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(lblWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE).addComponent(this.txtWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(this.tablePageObjects, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).addComponent(this.btnSelectPageObject, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
		//gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblWebProjectURL).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtWebProjectURL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblWebProjectPageObject).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.txtWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.tablePageObjects, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(this.btnSelectPageObject, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup())).addContainerGap(49, Short.MAX_VALUE)));
		
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
			addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().
				addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
					addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().
						addComponent(this.txtWebProjectTestPath, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE).
						addPreferredGap(ComponentPlacement.UNRELATED).
						addComponent(this.btnSelectWebProjectTestPath, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).
						addPreferredGap(ComponentPlacement.RELATED)						
					).
					addComponent(this.btnDatabaseProperties, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE).					
					addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup().
						addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE).
						addPreferredGap(ComponentPlacement.UNRELATED).
						addComponent(this.btnSelectPageObject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).
						addPreferredGap(ComponentPlacement.RELATED).
						addComponent(btnRemovePageObject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).
						addComponent(this.btnUpdatePageObject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					).
					addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE).
					addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblDescription).
					addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE).					
					addComponent(lblWebProjectURL, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE).					
					addComponent(this.txtWebProjectURL, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE).
					addComponent(lblWebProjectTestPath, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE).
					addComponent(lblWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
				).addContainerGap()
			)
		);
		
		
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
			addGroup(gl_contentPanel.createSequentialGroup().
				addContainerGap().
				addComponent(lblNewLabel).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtName, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblDescription, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtDescription, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblWebProjectURL).addPreferredGap(ComponentPlacement.RELATED).
				addComponent(this.txtWebProjectURL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblWebProjectTestPath, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
					addGroup(gl_contentPanel.createSequentialGroup().
						addComponent(this.txtWebProjectTestPath, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
						addGap(12)						
					).					
					addComponent(this.btnSelectWebProjectTestPath, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
				).
				addComponent(this.btnDatabaseProperties, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).
				addComponent(lblWebProjectPageObject, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE).
				addPreferredGap(ComponentPlacement.RELATED).				
				addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).
					addGroup(gl_contentPanel.createSequentialGroup().
						addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE).addGap(12)
					).
					addComponent(btnRemovePageObject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).
					addComponent(this.btnSelectPageObject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE).
					addComponent(this.btnUpdatePageObject, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
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
					WebProjectPropertiesDialog.this.confirm();
				}
			});
			btnConfirm.setIcon(new ImageIcon(WebProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/confirm.png")));

			JButton btnCancel = new JButton("");
			btnCancel.setToolTipText("Cancel");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WebProjectPropertiesDialog.this.cancel();
				}
			});
			btnCancel.setIcon(new ImageIcon(WebProjectPropertiesDialog.class.getResource("/com/general/mbts4ma/view/framework/images/cancel.png")));
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap(288, Short.MAX_VALUE)));
			gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addGroup(gl_buttonPane.createParallelGroup(Alignment.TRAILING).addComponent(btnConfirm, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
			buttonPane.setLayout(gl_buttonPane);
		}

		this.loadProjectProperties();
	}

	protected void removePageObject() {
		
		String pageObjectToRemove = (String)tablePageObjects.getValueAt(tablePageObjects.getSelectedRow(), 0);
		
		for (Iterator iterator = pageObjects.iterator(); iterator.hasNext();) {
			PageObject pageObject = (PageObject) iterator.next();
			
			if (pageObject.getClassName().equals(pageObjectToRemove)) {
				iterator.remove();
			}
			
		}
		
		((DefaultTableModel)tablePageObjects.getModel()).removeRow(tablePageObjects.getSelectedRow());
		
	}

	private void loadProjectProperties() {
		if (this.graphProject != null) {
			this.txtName.setText(this.graphProject.getName());
			this.txtDescription.setText(this.graphProject.getDescription());
			this.txtWebProjectPageObject.setText(this.graphProject.getWebProjectPageObject());
			this.txtWebProjectURL.setText(this.graphProject.getWebProjectURL());
			this.txtWebProjectTestPath.setText(this.graphProject.getWebProjectDirTestPath());
			this.pageObjects = this.graphProject.getPageObjects();
			
			DefaultTableModel model = (DefaultTableModel) this.tablePageObjects.getModel();
			
			for (PageObject pageObject : this.pageObjects) {
				model.addRow(new String[]{pageObject.getClassName()});
			}
			
			this.databaseRegression = this.graphProject.getDatabaseRegression();
			
		}
	}

	private void selectWebProjectTestPath() {
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setDialogTitle("Select Web project test path");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(true);

		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			String path = fileChooser.getSelectedFile().toString();

			this.txtWebProjectTestPath.setText(path);
			
			Launcher launcher = new Launcher();
			launcher.getEnvironment().setNoClasspath(true);
			launcher.getEnvironment().setAutoImports(true);
			launcher.addInputResource(path);			
			launcher.buildModel();
			
			this.spoonLauncher = launcher;
						
			List<CtType<?>> classesList = launcher.getFactory().Class().getAll();
			
			for (CtType<?> clazz : classesList) {
				
				DefaultTableModel modelTable = (DefaultTableModel) this.tablePageObjects.getModel();
				modelTable.addRow(new Object[]{clazz.getSimpleName(), isAPossiblePageObjectClass(clazz)});
				
			}
		}
	}
	
	private boolean isAPossiblePageObjectClass(CtType<?> pageObjectClass) {
		
		if (pageObjectClass.getSimpleName().contains("Page") || pageObjectClass.getPath().toString().contains("po"))
			return true;
		
		return false;
	}
	
	private void databaseProperties() {
		WebProjectDatabasePropertiesDialog dialog = new WebProjectDatabasePropertiesDialog(this.graphProject);

		dialog.setVisible(true);
		
		this.databaseRegression = dialog.getDatabaseConfig();
	}
	
	private void selectPageObjectPath() {
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setDialogTitle("Select Page Object path");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setMultiSelectionEnabled(true);

		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();
			
			for (File f : files) {
				//String path = fileChooser.getSelectedFile().toString();
				//String name = fileChooser.getSelectedFile().getName();
				String path = f.toString();
				String name = f.getName();
				
				DefaultTableModel model = (DefaultTableModel) this.tablePageObjects.getModel();
				model.addRow(new Object[]{name});
				
				//PageObject newPageObject = new PageObject(name, path);
				
				//pageObjects.add(newPageObject);
			}
			
		}
	}
	
	private void updatePageObjectContent() {
		
		for (Iterator iterator = pageObjects.iterator(); iterator.hasNext();) {
			PageObject pageObject = (PageObject) iterator.next();
			
			pageObject.refreshContentByPath();
			
		}
		
		JOptionPane.showMessageDialog(null, "The page objects has been refreshed succesfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		
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

			JOptionPane.showMessageDialog(null, "The selected path is an Android project.", "Attention", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "The selected path is not an Android project.", "Attention", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void verifyAndroidProject() {
		if (this.txtWebProjectTestPath.getText() != null && !"".equalsIgnoreCase(this.txtWebProjectTestPath.getText())) {
			String path = this.txtWebProjectTestPath.getText();

			this.loadAndroidProjectInfo(path);
		} else {
			this.txtApplicationPackage.setText(null);

			this.cmbMainTestingActivity.removeAllItems();
		}
	}

	private void confirm() {
		if (this.spoonLauncher == null) {
			JOptionPane.showMessageDialog(null, "Select the project path and configure the project!", "Attention", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (!this.isUpdate) {
			this.graphProject = new GraphProjectVO();
		}

		this.graphProject.setName(this.txtName.getText());
		this.graphProject.setDescription(this.txtDescription.getText());
		this.graphProject.setWebProjectURL(this.txtWebProjectURL.getText());
		//this.graphProject.setWebProjectPageObject(this.txtWebProjectPageObject.getText());
		
		this.graphProject.setWebProjectDirTestPath(this.txtWebProjectTestPath.getText());
		
		ArrayList<String> pageObjectsName = new ArrayList<String>();
		
		for (int i = 0; i < this.tablePageObjects.getRowCount(); i++) {
			if (this.tablePageObjects.getValueAt(i, 1) == Boolean.TRUE)
				pageObjectsName.add(this.tablePageObjects.getValueAt(i, 0).toString());
		}
		
		this.graphProject.setLauncher(this.spoonLauncher);
		
		List<CtType<?>> classesList = this.graphProject.getLauncher().getFactory().Class().getAll();
		
		for (CtType<?> clazz : classesList) {
			if (pageObjectsName.contains(clazz.getSimpleName())) {
				
				PageObject newPageObject = new PageObject(clazz.getSimpleName(), clazz.toString(), clazz.getQualifiedName(), false);
				
				pageObjects.add(newPageObject);
				
			}
		}
		
		this.graphProject.setPageObjects(this.pageObjects);
		
		/*
		String pageObjectConcat = "";
		
		for (Iterator iterator = pageObjects.iterator(); iterator.hasNext();) {
			PageObject pageObject = (PageObject) iterator.next();
			
			pageObjectConcat += pageObject.getClassName();
			
			if (iterator.hasNext()){
				pageObjectConcat += ",";
			}
			
		}*/
		
		/*List<TestClass> testClasses = new ArrayList<TestClass>();
		TestClass tc = new TestClass("Teste", this.txtWebProjectTestPath.getText());
		testClasses.add(tc);
		
		this.graphProject.setTestClasses(testClasses);
		*/
		//this.graphProject.setWebProjectPageObject(pageObjectConcat);
		this.graphProject.setDatabaseRegression(this.databaseRegression);

		this.dispose();
	}

	private void cancel() {
		if (!this.isUpdate) {
			this.graphProject = null;
		}

		this.dispose();
	}
}

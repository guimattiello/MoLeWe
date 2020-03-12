package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import spoon.reflect.declaration.CtMethod;

public class NewTestCasesDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JTextArea txtCESs = null;
	private JPanel buttonPane;

	private JScrollPane scrollPane;

	public NewTestCasesDialog(List<CtMethod<?>> methods) {
		
		this.setTitle("New Test Cases Generated");

		this.setBounds(100, 100, 500, 500);
		this.getContentPane().setLayout(new BorderLayout());
		this.setResizable(true);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.txtCESs = new JTextArea();
		this.txtCESs.setEditable(false);
		this.txtCESs.setWrapStyleWord(true);
		this.txtCESs.setLineWrap(true);
		this.txtCESs.setFont(new Font("Verdana", Font.PLAIN, 12));

		((DefaultCaret) this.txtCESs.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		this.scrollPane = new JScrollPane(this.txtCESs);

		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE).addContainerGap()));
		this.contentPanel.setLayout(gl_contentPanel);

		this.buttonPane = new JPanel();
		this.getContentPane().add(this.buttonPane, BorderLayout.SOUTH);
		
		if (methods != null && !"".equalsIgnoreCase(methods.toString())) {
			this.txtCESs.setText(methods.toString().replace(", public  ", "\n\n@Test\npublic void "));
		} else {
			this.txtCESs.setText("");
		}
	}

}

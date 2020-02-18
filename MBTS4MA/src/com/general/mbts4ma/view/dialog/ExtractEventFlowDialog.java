package com.general.mbts4ma.view.dialog;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import com.general.mbts4ma.view.framework.bo.GraphProjectBO;
import com.general.mbts4ma.view.framework.vo.GraphProjectVO;

public class ExtractEventFlowDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private JTextArea txtEventFlow = null;

	private JScrollPane scrollPane = null;

	private GraphProjectVO graphProject = null;
	private String content = null;

	public ExtractEventFlowDialog(GraphProjectVO graphProject, String content) {
		this.graphProject = graphProject;
		this.content = content;

		this.setTitle("Show edges (event pairs)");

		this.setBounds(100, 100, 500, 420);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setResizable(true);
		this.setModal(true);
		this.setLocationRelativeTo(null);

		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.txtEventFlow = new JTextArea();
		this.txtEventFlow.setEditable(false);
		this.txtEventFlow.setFont(new Font("Verdana", Font.PLAIN, 12));
		this.txtEventFlow.setWrapStyleWord(true);
		this.txtEventFlow.setLineWrap(true);

		((DefaultCaret) this.txtEventFlow.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

		this.scrollPane = new JScrollPane(this.txtEventFlow);

		GroupLayout gl_contentPanel = new GroupLayout(this.contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE).addContainerGap()));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE).addContainerGap()));
		this.contentPanel.setLayout(gl_contentPanel);

		JPanel buttonPane = new JPanel();
		this.getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnExportToTxt = new JButton("");
		btnExportToTxt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExtractEventFlowDialog.this.exportToTxt();
			}
		});
		btnExportToTxt.setIcon(new ImageIcon(ExtractEventFlowDialog.class.getResource("/com/general/mbts4ma/view/framework/images/txtfile.png")));
		btnExportToTxt.setToolTipText("Export to TXT");
		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(gl_buttonPane.createSequentialGroup().addContainerGap().addComponent(btnExportToTxt, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap(429, Short.MAX_VALUE)));
		gl_buttonPane.setVerticalGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(btnExportToTxt, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addContainerGap()));
		buttonPane.setLayout(gl_buttonPane);

		if (content != null && !"".equalsIgnoreCase(content)) {
			this.txtEventFlow.setText(content);
		} else {
			this.txtEventFlow.setText("");
		}
	}

	private void exportToTxt() {
		JFileChooser fileChooser = new JFileChooser(this.graphProject.getFileSavingDirectory());

		fileChooser.setSelectedFile(new File(this.graphProject.getName() + " - Event Pairs"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("Text file (*.txt)", "txt"));
		fileChooser.setDialogTitle("Specify a file to save");

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			String fileSavingPath = fileChooser.getSelectedFile().getAbsolutePath();

			if (fileSavingPath != null) {
				if (GraphProjectBO.save(fileSavingPath, this.content, "txt")) {
					JOptionPane.showMessageDialog(null, "Text file successfully exported.", "Attention", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
}

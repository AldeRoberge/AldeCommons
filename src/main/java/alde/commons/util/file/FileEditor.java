
package alde.commons.util.file;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.util.text.StackTraceToString;

/**
 * Simple JPanel to allow the user to edit a file on disk
 */
public class FileEditor extends JPanel {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(FileEditor.class);

	private File file;
	private JButton saveButton;
	private JTextArea fileEditor;

	/**
	 * Create the application.
	 */
	public FileEditor(File file) {

		super();

		this.file = file;

		if (file == null) {
			log.error("File is null!");
			return;
		} else if (!file.exists()) {
			log.error("File '" + file.getAbsolutePath() + "' does not exist!");
			return;
		}

		setLayout(new BorderLayout());

		setBounds(100, 100, 450, 300);

		JPanel fileEditorPanel = new JPanel();
		add(fileEditorPanel, BorderLayout.CENTER);
		fileEditorPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane fileEditorScrollPane = new JScrollPane();
		fileEditorPanel.add(fileEditorScrollPane, BorderLayout.CENTER);

		fileEditor = new JTextArea();
		populateTextArea();
		fileEditorScrollPane.setViewportView(fileEditor);

		fileEditor.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				setEdited(true);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				setEdited(true);
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				setEdited(true);
			}
		});

		JPanel savePanel = new JPanel();
		add(savePanel, BorderLayout.NORTH);

		JLabel lblInfo = new JLabel("Content of file '" + file.getName() + "'.");
		savePanel.add(lblInfo);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		savePanel.add(horizontalStrut);

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateFile();
				setEdited(false);
			}
		});
		savePanel.add(saveButton);

		saveButton.setVisible(false);
	}

	private void populateTextArea() {

		StringBuilder content = new StringBuilder();

		try (FileReader fileStream = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileStream)) {

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				content.append(line).append("\n");
			}

		} catch (IOException ex) {
			log.error(StackTraceToString.sTTS(ex));
			ex.printStackTrace();
		}

		fileEditor.setText(content.toString());
	}

	private void updateFile() {
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			bw.write(fileEditor.getText());

			log.debug("File '" + file.getName() + "' saved.");

			bw.close();
			fw.close();

		} catch (IOException ex) {
			log.error(StackTraceToString.sTTS(ex));
			ex.printStackTrace();
		}

		populateTextArea();

	}

	private void setEdited(boolean isEdited) {
		saveButton.setVisible(isEdited);
	}

}

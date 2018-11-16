/*
  FileImporter is a great way to let your user import files.
  Requires a class (here FileImporterAgent) that implements the method importFiles(ArrayList<File> files);
  Call "new FileImporter(FileImporterAgent me)"
 */

package alde.commons.fileImporter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.util.file.extensions.ExtensionFilter;
import alde.commons.util.window.UtilityJFrame;

public class FileImporter extends UtilityJFrame {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(FileImporter.class);

	public static void main(String[] args) {
		FileImporter f = new FileImporter(k -> {
			log.info("Received " + k.size() + " files.");
		}, false, ExtensionFilter.PICTURE_FILES);
	}

	private ExtensionFilter acceptedFileTypes;

	private List<File> filesToImport = new ArrayList<File>();
	private DropPane dropPanel;
	private JButton btnNext;

	/**
	 * Create the frame.
	 */
	public FileImporter(Consumer<List<File>> fileListConsumer, boolean includeSubfolders, ExtensionFilter acceptedFileTypes) {

		this.acceptedFileTypes = acceptedFileTypes;

		//setIconImage(Icons.IMPORT.getImage());
		setTitle("Import");

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				filesToImport.clear();
				setVisible(false);
			}
		});

		setBounds(100, 100, 571, 289);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel dragAndDropPanel = new JPanel();
		contentPane.add(dragAndDropPanel, BorderLayout.CENTER);
		dragAndDropPanel.setLayout(new BoxLayout(dragAndDropPanel, BoxLayout.X_AXIS));

		dropPanel = new DropPane(new Consumer<File>() {
			@Override
			public void accept(File file) {
				importFile(file);
			}

		});
		dragAndDropPanel.add(dropPanel);

		JPanel actionsPanel = new JPanel();
		contentPane.add(actionsPanel, BorderLayout.SOUTH);
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));

		JPanel includeSubfolderPanel = new JPanel();
		includeSubfolderPanel.setVisible(includeSubfolders);
		actionsPanel.add(includeSubfolderPanel);

		JCheckBox chckBoxIncludeSubFolders = new JCheckBox("Include subfolders");
		chckBoxIncludeSubFolders.setSelected(includeSubfolders);
		includeSubfolderPanel.add(chckBoxIncludeSubFolders);

		JPanel actionPanel = new JPanel();
		actionsPanel.add(actionPanel);

		JButton btnOpenFileBrowser = new JButton("Open file browser");
		actionPanel.add(btnOpenFileBrowser);

		btnNext = new JButton("Next");
		actionPanel.add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filesToImport.size() > 0) {
					log.info("Sent files...");
					
					fileListConsumer.accept(filesToImport);
					filesToImport.clear();
				} else {
					log.info("No files to import...");
				}

				setVisible(false);
				dispose();
			}
		});
		btnOpenFileBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				log.info("Selecting a folder...");

				JFileChooser chooser = new JFileChooser();

				//chooser.setCurrentDirectory(new File(Properties.LAST_OPENED_LOCATION.getValue()));
				chooser.setDialogTitle("Import folders and samples");
				chooser.setApproveButtonText("Choose");

				// from https://stackoverflow.com/questions/16292502/how-can-i-start-the-jfilechooser-in-the-details-view
				Action details = chooser.getActionMap().get("viewTypeDetails"); // show details view
				details.actionPerformed(null);

				// Format supported by AudioPlayer :
				// WAV, AU, AIFF ,MP3 and Ogg Vorbis files

				chooser.setFileFilter(acceptedFileTypes);
				chooser.setMultiSelectionEnabled(true); // shift + click to select multiple files
				chooser.setPreferredSize(new Dimension(800, 600));
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				//replace null with a JPanel that has an icon to set an icon to chooser
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					btnNext.requestFocus();

					String directory = chooser.getCurrentDirectory().toString();

					//Properties.LAST_OPENED_LOCATION.setNewValue(directory);

					File[] files = chooser.getSelectedFiles();

					for (File f : files) {
						importFile(f);
					}

					//
				} else {
					log.info(" (Folder Selector)", "No selection");
				}

			}

		});

		setVisible(true);
	}

	private void importFile(File file) {
		if (file.isDirectory()) {
			getAllFiles(file.getAbsolutePath(), true, 0);
		} else {
			addFileToImport(file);
		}
	}

	private void getAllFiles(String directoryName, boolean includeSubfolders, int totalOfFiles) {

		log.info("Getting all files for directory : " + directoryName + ", including subfolders : " + includeSubfolders);

		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();

		if (fList != null) {

			for (File file : fList) {

				if (file.isFile()) {
					addFileToImport(file);
				} else if (file.isDirectory() && includeSubfolders) {
					getAllFiles(file.getAbsolutePath(), includeSubfolders, totalOfFiles);
				}
			}

		}
	}

	private void addFileToImport(File f) {
		if (filesToImport.contains(f)) {
			log.warn("File to import is already imported.");
		} else {
			boolean accept = false;

			if (acceptedFileTypes == null) {
				accept = true;
			} else if (acceptedFileTypes.accept(f)) {
				accept = true;
			}

			if (accept) {
				filesToImport.add(f);
			}
		}
	}

	public int getTotalFilesToImport() {
		return filesToImport.size();
	}

}

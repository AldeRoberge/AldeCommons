/*
  FileImporter is a great way to let your user import files.
  Requires a class (here FileImporterAgent) that implements the method importFiles(ArrayList<File> files);
  Call "new FileImporter(FileImporterAgent me)"
 */

package alde.commons.fileImporter;

import alde.commons.util.file.extensions.ExtensionFilter;
import alde.commons.util.window.UtilityJFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.function.Consumer;

public class FileImporter extends UtilityJFrame {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(FileImporter.class);

	public static void main(String[] args) {
		FileImporter f = new FileImporter(k -> {
			System.out.println("Received " + k.size() + " files.");
		}, true, null);
	}

	private ExtensionFilter acceptedFileTypes;

	private List<File> filesToImport = new ArrayList<File>();
	private DropPane dropPanel;
	private JButton btnImport;

	/**
	 * Create the frame.
	 */
	private FileImporter(Consumer<List<File>> fileListConsumer, boolean includeSubfolders,
			ExtensionFilter acceptedFileTypes) {

		this.acceptedFileTypes = acceptedFileTypes;

		//setIconImage(Icons.IMPORT.getImage());
		setTitle("Import");

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				filesToImport.clear();
				dropPanel.updateMessage();
				setVisible(false);
			}
		});

		setBounds(100, 100, 553, 262);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel container = new JPanel();
		contentPane.add(container, BorderLayout.CENTER);
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		dropPanel = new DropPane(this);
		container.add(dropPanel);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);

		JCheckBox chckBoxIncludeSubFolders = new JCheckBox("Include subfolders");
		panel_1.add(chckBoxIncludeSubFolders);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);

		JButton btnOpenFileBrowser = new JButton("Open file browser");
		panel_2.add(btnOpenFileBrowser);

		btnImport = new JButton("Import");
		panel_2.add(btnImport);
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filesToImport.size() > 0) {
					fileListConsumer.accept(filesToImport);
					filesToImport.clear();
					setVisible(false);
					dropPanel.updateMessage();
				}
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
					btnImport.requestFocus();

					String directory = chooser.getCurrentDirectory().toString();

					//Properties.LAST_OPENED_LOCATION.setNewValue(directory);

					File[] files = chooser.getSelectedFiles();

					importAll(Arrays.asList(files));

					//
				} else {
					log.info(" (Folder Selector)", "No selection");
				}

			}

		});

		setVisible(true);
	}

	void importAll(List<File> files) {
		for (File file : files) { //we do this because the user might choose more than 1 folder
			if (file.isDirectory()) {
				getAllFiles(file.getAbsolutePath(), true, 0);
			} else {
				addFileToImport(file);
			}
		}

	}

	private void getAllFiles(String directoryName, boolean includeSubfolders, int totalOfFiles) {

		log.info(
				"Getting all files for directory : " + directoryName + ", including subfolders : " + includeSubfolders);

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
				dropPanel.updateMessage();
			}
		}
	}

	public int getTotalFilesToImport() {
		return filesToImport.size();
	}

}

package alde.commons.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class WriteToFile {

	private File file;

	private ArrayList<String> fileContent;

	public WriteToFile(String filePath) {

		fileContent = new ArrayList<String>();

		file = new File(filePath); // Create file if it doesn't exist
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) { // Populate 'fileContent' with existing lines in file
			for (String line; (line = br.readLine()) != null;) {
				fileContent.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean isAlreadyRunning;
	private ArrayList<String> bufferedText = new ArrayList<String>();

	/**
	 * Write
	 * @param text : String to write to file
	 */
	public void write(String text) {
		if (!isAlreadyRunning) {

			isAlreadyRunning = true;

			BufferedWriter bw = null;
			try {
				// APPEND MODE SET HERE
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(text);
				bw.newLine();

				Iterator<String> it = bufferedText.iterator();
				while (it.hasNext()) {
					String pair = it.next();
					bw.write(pair);
					it.remove();
				}

				try {
					bw.close();
				} catch (IOException ignored) {
				}

				fileContent.add(text);

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			isAlreadyRunning = false;

		} else {
			bufferedText.add(text);
		}
	}

	public boolean contains(String line) {
		return fileContent.contains(line);
	}

	public int size() {
		return fileContent.size();
	}

	public ArrayList<String> get() {
		return fileContent;
	}

}

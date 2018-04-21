package alde.commons.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFileAsList {

	private static Logger log = LoggerFactory.getLogger(GetFileAsList.class);

	/**
	 * Returns a List<String> from the content of a file
	 */
	public static List<String> getFileAsList(File f) {

		if (f == null) {
			log.error("File is null.");
			return null;
		}

		if (!f.exists()) {
			log.error("File does not exist.");
			return null;
		}

		List<String> strings = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				strings.add(line);
			}

		} catch (IOException e) {
			log.error("Error with file '" + f.getAbsolutePath() + "', exception thrown : ");
			e.printStackTrace();
		}

		return strings;

	}
}

package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Get a file as a List of Strings
 * 
 */
public class GetFileAsString {

	private static Logger log = LoggerFactory.getLogger(GetFileAsString.class);

	public static List<String> getFileAsList(File f) {

		List<String> strings = new ArrayList<>();

		if (f == null) {
			log.error("File is null.");
			return null;
		}

		if (!f.exists()) {
			log.error("File does not exist.");
			return null;
		}

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				strings.add(line);
			}

		} catch (IOException e) {
			log.error("Error with file " + f.getAbsolutePath() + ", exception thrown : ");
			e.printStackTrace();
		}

		return strings;

	}
}

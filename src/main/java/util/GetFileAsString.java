package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFileAsString {

	private static Logger log = LoggerFactory.getLogger(GetFileAsString.class);

	/**
	 * @param f file to read from
	 * @return Returns a list of all the strings inside the file
	 */
	public static List<String> getFileAsList(File f) {

		List<String> strings = new ArrayList<>();

		if ((f == null) || !f.exists()) {
			log.error("File does not exist, returning.");
			return strings;
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

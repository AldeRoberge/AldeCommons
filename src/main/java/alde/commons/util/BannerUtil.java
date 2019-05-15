package alde.commons.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.util.file.GetFileAsList;
import alde.commons.util.file.WriteToFile;

public class BannerUtil {

	private static Logger log = LoggerFactory.getLogger(BannerUtil.class);

	public static void printBanner(String bannerFilePath) {

		File f = new File(bannerFilePath);

		if (!f.exists()) {
			log.debug("No banner file found. Consider adding a banner inside " + bannerFilePath
					+ " at the root of the program.");

			WriteToFile w = new WriteToFile(bannerFilePath);
			w.write("Replace this file (" + bannerFilePath + ") with a banner to print on every launch.");
		} else {

			StringBuilder ss = new StringBuilder();

			for (String s : GetFileAsList.getFileAsList(f)) {
				ss.append(s + "\n");
			}

			log.info(ss.toString());
		}
	}

}

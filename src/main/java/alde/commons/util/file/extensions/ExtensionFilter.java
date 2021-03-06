package alde.commons.util.file.extensions;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Original credits : 
 * @author Java2s.com
 * 
 * FileFilter default for basic types
 */
public class ExtensionFilter extends FileFilter {

	public static final ExtensionFilter AUDIO_FILES = new ExtensionFilter("Audio Files", ".aiff", "au", "mp3", "ogg", "mp4", "wav");

	public static final ExtensionFilter VIDEO_FILES = new ExtensionFilter("Video Files", "webm", "mkv", "flv", "ogg", "gif", "avi", "mov", "wmv", "mp4", "mpg", "m4v");

	public static final ExtensionFilter PICTURE_FILES = new ExtensionFilter("Picture Files", "jpeg", "jpg", "tiff", "gif", "bmp", "png");

	public static final ExtensionFilter TEXT_FILES = new ExtensionFilter("Text Files", "text", "txt");

	private String extensions[];
	private String description;

	private ExtensionFilter(String title, String... extensions) {
		this.extensions = extensions;
		this.description = generateDescription(title, extensions);
	}

	/*
	title : "Picture Files"
	extensions : {".jpeg", ".png"}
	
	return Picture Files (*.jpeg, *.png)
	 */
	private static String generateDescription(String title, String[] extensions) {
		StringBuilder s = new StringBuilder("");

		for (int i = 0; i < extensions.length; i++) {
			if (i == 0) {
				s = new StringBuilder(title + " ( *." + extensions[i]);
			} else {
				s.append(", *.").append(extensions[i]);
			}

			if (i == extensions.length - 1) {
				s.append(")");
			}
		}
		return s.toString();
	}

	/**
	 * @param file File to verify
	 * @return accepts the file (compares the file extension)
	 */
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}

		String path = file.getAbsolutePath();
		for (String ext : extensions) {

			if (path.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
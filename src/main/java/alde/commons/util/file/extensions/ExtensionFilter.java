package alde.commons.util.file.extensions;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 	Code from Java2s.com
 *
 */

public class ExtensionFilter extends FileFilter {

	private String extensions[];

	private String description;

	public ExtensionFilter(String description, String extension) {
		this(description, new String[] { extension });
	}

	public ExtensionFilter(String description, String extensions[]) {
		this.description = description;
		this.extensions = extensions.clone();
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}

		String path = file.getAbsolutePath();
		for (String ext : extensions) {
			if (path.endsWith(ext) && (path.charAt(path.length() - ext.length()) == '.')) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return (description == null ? extensions[0] : description);
	}
}
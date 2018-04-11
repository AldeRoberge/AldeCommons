package alde.commons.util.file;


/**
 * Get only the name (no extension)
 * Get only the extension
 * 
 * from https://stackoverflow.com/questions/924394/how-to-get-the-filename-without-the-extension-in-java
 */
public class FileNameUtil {

	

	/**
	 * Remove the file extension from a filename, that may include a path.
	 * 
	 * e.g. /path/to/myfile.jpg -> /path/to/myfile 
	 */
	public static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}

		int index = indexOfExtension(filename);

		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	/**
	 * Return the file extension from a filename, including the "."
	 * 
	 * e.g. /path/to/myfile.jpg -> .jpg
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}

		int index = indexOfExtension(filename);

		if (index == -1) {
			return filename;
		} else {
			return filename.substring(index);
		}
	}

	private static final char EXTENSION_SEPARATOR = '.';
	private static final char DIRECTORY_SEPARATOR = '/';

	private static int indexOfExtension(String filename) {

		if (filename == null) {
			return -1;
		}

		// Check that no directory separator appears after the 
		// EXTENSION_SEPARATOR
		int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);

		int lastDirSeparator = filename.lastIndexOf(DIRECTORY_SEPARATOR);

		if (lastDirSeparator > extensionPos) { //"A directory separator appears after the file extension, assuming there is no file extension"
			return -1;
		}

		return extensionPos;
	}

}

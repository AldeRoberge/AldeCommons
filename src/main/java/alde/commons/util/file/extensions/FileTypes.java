package alde.commons.util.file.extensions;

public class FileTypes {
	
	public static final ExtensionFilter AUDIO_FILES = new ExtensionFilter(
			"Audio Files (*.aiff, *.au, *.mp3, *.ogg, *.mp4, *.wav)",
			new String[] { ".aiff", ".au", ".mp3", ".ogg", ".mp4", ".wav" });

	public static final ExtensionFilter VIDEO_FILES = new ExtensionFilter(
			"Video Files (*.webm, *.mkv, *.flv, *.ogg, *.gif, *.avi, *.mov, *.wmv, *.mp4, *.mpg, *.m4v)",
			new String[] { ".webm", ".mkv", ".flv", ".ogg", ".gif", ".avi", ".mov", ".wmv", ".mp4", ".mpg", ".m4v" });

	public static final ExtensionFilter PICTURE_FILES = new ExtensionFilter(
			"Picture Files (*.jpeg, *.tiff, *.gif, *.bmp, *.png)",
			new String[] { ".jpeg", ".tiff", ".gif", ".bmp", ".png" });

	public static final ExtensionFilter TEXT_FILES = new ExtensionFilter("Text Files (*.text, *.txt)",
			new String[] { ".text", ".txt" });

}

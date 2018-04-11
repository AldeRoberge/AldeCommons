package alde.commons.util.text;

public class SecondsToHMS {

	/**
	 * 120 seconds = 00h02m00s
	 */
	public static String convert(long totalSecs) {
		
		long hours = totalSecs / 3600;
		long minutes = (totalSecs % 3600) / 60;
		long seconds = totalSecs % 60;

		return String.format("%02dh%02dm%02ds", hours, minutes, seconds);
		
	}

}

package util;

public class SecondsToHMS {

	public static String convert(int secs) {

		int hours = secs / 3600, remainder = secs % 3600, minutes = remainder / 60, seconds = remainder % 60;

		String disHour = (hours < 10 ? "0" : "") + hours, disMinu = (minutes < 10 ? "0" : "") + minutes,
				disSec = (seconds < 10 ? "0" : "") + seconds;

		return disHour + "h " + disMinu + "m and " + disSec + "s.";

		// 01:03:48
		// 01h 03m 48s
	}

}

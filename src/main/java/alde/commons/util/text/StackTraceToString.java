package alde.commons.util.text;

public class StackTraceToString {

	/** Convert a stacktrace to a string with \n */
	public static String sTTS(Exception e) {

		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append("\n\tat ");
			sb.append(ste);
		}

		return sb.toString();
	}

}

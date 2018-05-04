package alde.commons.util.text;

public class StackTraceToString {

	/** Convert a Exception's stack trace to a string with \n */
	public static String sTTS(Exception e) {

		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append(ste).append("\n");
		}

		return sb.toString();
	}

}

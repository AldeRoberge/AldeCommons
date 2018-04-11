package util;

public class StackTraceToString {
	public static String sTTS(Exception e) {
		// convert the stacktrace to a string with \n
		StringBuilder sb = new StringBuilder(e.toString());
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append("\n\tat ");
			sb.append(ste);
		}

		return sb.toString();
	}
}

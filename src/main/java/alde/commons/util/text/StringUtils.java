package alde.commons.util.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtils {

	protected static final char[] delimiters = new char[] { ' ', ',', ']', '[', ')', '(', '.', ';', ':' };

	protected static boolean isDelimiterCharacter(char c) {
		for (char d : delimiters) {
			if (d == c) {
				return true;
			}
		}
		return false;
	}

	/** Returns the next delimiter */
	protected static int indexOfNextDelimiter(String string) {
		return indexOfNextDelimiter(string, 0);
	}

	protected static int indexOfNextDelimiter(String string, int beginIndex) {
		char[] chars = string.substring(beginIndex).toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (isDelimiterCharacter(chars[i])) {
				return i;
			}
		}

		return string.length();
	}

	protected static int indexOfPreviousDelimiter(String string) {
		return indexOfPreviousDelimiter(string, 0);
	}

	protected static int indexOfPreviousDelimiter(String string, int beginIndex) {
		char[] chars = string.substring(beginIndex).toCharArray();

		for (int i = 0; i < chars.length; i--) {
			if (isDelimiterCharacter(chars[i])) {
				return i;
			}
		}

		return 0;
	}

	protected static String getInbetween(String line, String start, String end) {
		if (!line.contains(start)) {
			System.err.println("Error : line does not contain start.");
			return "";
		} else if (!line.contains(end)) {
			System.err.println("Error : line does not contain end.");
			return "";
		}

		return line.substring(line.indexOf(start) + start.length(), line.indexOf(end));
	}

	protected static String replace(String line, String string, String string2) {
		if (line.contains(string)) {
			line = line.replace(string, string2);
		}
		return line;
	}

	protected static String rotate(String line, String rotatePoint) {
		return rotate(line, rotatePoint, "");
	}

	protected static String rotate(String line, String rotatePoint, String replaceRotatePointWith) {

		if (line == null || rotatePoint == null) {
			System.err.println("Error, input is null.");
			return "";
		} else if (line.length() == 0 || rotatePoint.length() == 0) {
			System.err.println("Error, input is empty.");
			return "";
		}

		int beginningOfFirstWord = indexOfPreviousDelimiter(line, line.indexOf(rotatePoint));
		int endOfFirstWord = line.indexOf(rotatePoint);

		String firstWord = line.substring(beginningOfFirstWord, endOfFirstWord);

		int beginningOfSecondWord = line.indexOf(rotatePoint) + rotatePoint.length();
		int endOfSecondWord = indexOfNextDelimiter(line, line.indexOf(rotatePoint) + rotatePoint.length());

		String secondWord = line.substring(beginningOfSecondWord, endOfSecondWord);

		// System.out.println("FIRST WORD : '" + firstWord + "', SECOND WORD : '" + secondWord + "'.");

		if (replaceRotatePointWith.equals("")) {
			return secondWord + rotatePoint + firstWord;
		} else {
			return secondWord + replaceRotatePointWith + firstWord;
		}
	}

	@Test
	public void test() {
		assertEquals("Hey there", rotate("there Hey", " ", ""));

		assertEquals(false, isDelimiterCharacter('c'));
		assertEquals(false, isDelimiterCharacter('d'));
		assertEquals(true, isDelimiterCharacter('['));
		assertEquals(true, isDelimiterCharacter(' '));

		assertEquals(5, indexOfNextDelimiter("James bond"));

	}

}

package alde.commons.util.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtils {

	/**
	 * Marks the end of a word
	 */
	protected static final char[] delimiters = new char[] { ' ', ',', ']', '[', ')', '(', '.', ';', ':' };

	/**
	 * @param c character
	 * @return true if character is a delimiter
	 */
	protected static boolean isDelimiterCharacter(char c) {
		for (char d : delimiters) {
			if (d == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param string 
	 * @return index of the next delimiter in a string
	 */
	protected static int indexOfNextDelimiter(String string) {
		return nextDelimiterIndex(string, 0);
	}

	/**
	 * @param string 
	 * @param beginIndex starts looking for delimiter at index
	 * @return index of the next delimiter in a string
	 */
	protected static int nextDelimiterIndex(String string, int beginIndex) {
		char[] chars = string.substring(beginIndex).toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (isDelimiterCharacter(chars[i])) {
				return beginIndex + i;
			}
		}

		return string.length();
	}

	/**
	 * @param string 
	 * @return index of the previous delimiter in a string
	 */
	protected static int previousDelimiterIndex(String string) {
		return previousDelimiterIndex(string, 0);
	}

	/**
	 * @param string
	 * @param beginIndex starts looking for delimiter at index
	 * @return index of the previous delimiter in a string
	 */
	protected static int previousDelimiterIndex(String string, int beginIndex) {
		char[] chars = string.substring(beginIndex).toCharArray();

		for (int i = 0; i < chars.length; i--) {
			if (isDelimiterCharacter(chars[i])) {
				return beginIndex - i;
			}
		}

		return 0;
	}

	/**
	 * @param string 
	 * @return text between 'start' and 'end'
	 */
	protected static String getInbetween(String string, String start, String end) {
		if (!string.contains(start)) {
			System.err.println("Error : line '" + string + "' does not contain start '" + end + "'.");
			return "";
		} else if (!string.contains(end)) {
			System.err.println("Error : line '" + string + "' does not contain end '" + end + "'.");
			return "";
		}

		return string.substring(string.indexOf(start) + start.length(), string.indexOf(end));
	}

	/**
	 * @return replaces text contained inside of string
	 */
	protected static String replace(String string, String replace, String with) {
		if (string.contains(replace)) {
			string = string.replace(replace, with);
		}
		return string;
	}

	/**
	 * Rotates a string using a string point (uses delimiters as boundaries)
	 * I.E. : rotate("world Hello", " "); returns "Hello world"
	 */
	protected static String rotate(String string, String rotatePoint) {
		return rotate(string, rotatePoint, "");
	}

	/**
	 * Rotates a string using a string point (uses delimiters as boundaries)
	 * I.E. : rotate("world Hello", " "); returns "Hello world"
	 * if replaceRotatePointWith is not "", replace rotatePoint with this
	 */
	protected static String rotate(String string, String rotatePoint, String replaceRotatePointWith) {

		if (string == null || rotatePoint == null) {
			System.err.println("Error, input is null.");
			return "";
		} else if (string.length() == 0 || rotatePoint.length() == 0) {
			System.err.println("Error, input is empty.");
			return "";
		}

		// Get first word

		int beginningOfFirstWord = previousDelimiterIndex(string, string.indexOf(rotatePoint));
		int endOfFirstWord = string.indexOf(rotatePoint);

		System.out.println("First word beginning : " + beginningOfFirstWord + ", end : " + endOfFirstWord);

		String firstWord = string.substring(beginningOfFirstWord, endOfFirstWord);

		System.out.println("First word : " + firstWord);

		// Get second word

		int beginningOfSecondWord = string.indexOf(rotatePoint) + rotatePoint.length();
		int endOfSecondWord = nextDelimiterIndex(string, string.indexOf(rotatePoint) + rotatePoint.length());

		System.out.println("Second word beginning : " + beginningOfSecondWord + ", end : " + endOfSecondWord);

		String secondWord = string.substring(beginningOfSecondWord, endOfSecondWord);

		System.out.println("Second word : " + secondWord);

		// Rotate

		if (replaceRotatePointWith.equals("")) {
			return secondWord + rotatePoint + firstWord;
		} else {
			return secondWord + replaceRotatePointWith + firstWord;
		}
	}

	/**
	 * @param string
	 * @param afterKeyword look after this keyword in a string
	 * @return returns following keyword (using delimiters)
	 */
	protected static String getFollowingWord(String string, String afterKeyword) {

		if (!string.contains(afterKeyword)) {
			System.err.println("Error : '" + string + "' does not contain '" + afterKeyword + "'.");
			return "";
		}

		int endOfKeywordIndex = string.indexOf(afterKeyword) + afterKeyword.length();

		int nextDelimiterIndex = endOfKeywordIndex + indexOfNextDelimiter(string.substring(endOfKeywordIndex));

		return string.substring(endOfKeywordIndex, nextDelimiterIndex);
	}

	@Test
	public void test() {

		assertEquals("Alde", getFollowingWord("My name is Alde.", " is "));
		assertEquals("james;", getFollowingWord("package james;", "package "));

		assertEquals("Hey there", rotate("there Hey", " ", ""));

		assertEquals(false, isDelimiterCharacter('c'));
		assertEquals(false, isDelimiterCharacter('d'));
		assertEquals(true, isDelimiterCharacter('['));
		assertEquals(true, isDelimiterCharacter(' '));

		assertEquals(5, indexOfNextDelimiter("James bond"));

	}

}

package alde.commons.util.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StringUtils {

	public static String newLine = "\n";

	public static Logger stringUtilsLog = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * Marks the end of a word
	 */
	//@formatter:off
	private static final char[] delimiters = new char[]{
			' ',
			',',
			']',
			'[',
			')',
			'(',
			'.',
			';',
			':',
			'!',
			'='
	};
	//@formatter:on

	/**
	 * @param c
	 * @return true if c is a delimiter
	 */
	public static boolean isDelimiterCharacter(char c) {
		for (char d : delimiters) {
			if (d == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param line
	 * @return index of the next delimiter in a string
	 */
	public static int nextDelimiterIndex(String line) {
		return nextDelimiterIndex(line, 0);
	}

	/**
	 * @param line
	 * @param beginIndex starts looking for delimiter at index
	 * @return index of the next delimiter in a string
	 */
	public static int nextDelimiterIndex(String line, int beginIndex) {

		if (beginIndex > line.length()) {
			stringUtilsLog.error(
					"Begin index '" + beginIndex + "' cannot be further than string length '" + line.length() + "'.");
			return 0;
		}

		//

		char[] chars = line.toCharArray();

		for (int i = beginIndex + 1; i < chars.length; i++) {
			if (isDelimiterCharacter(chars[i])) {
				return i;
			}
		}

		return line.length();
	}

	/**
	 * @param line
	 * @return index of the previous delimiter in a string
	 */
	public static int previousDelimiterIndex(String line) {
		return previousDelimiterIndex(line, 0);
	}

	/**
	 * @param line
	 * @param beginIndex starts looking for delimiter at index
	 * @return index of the previous delimiter in a string
	 */
	public static int previousDelimiterIndex(String line, int beginIndex) {

		if (beginIndex > line.length()) {
			stringUtilsLog.error(
					"Begin index '" + beginIndex + "' cannot be further than string length '" + line.length() + "'.");
			return 0;
		}

		// 

		char[] chars = line.toCharArray();

		for (int i = beginIndex - 1; i >= 0; i--) {
			if (isDelimiterCharacter(chars[i])) {
				return i + 1;
			}
		}
		return 0;
	}

	/**
	 * @param line
	 * @return text between 'start' and 'end'
	 */
	public static String getInbetween(String line, String start, String end) {
		if (!line.contains(start)) {
			stringUtilsLog.error("Error : line '" + line + "' does not contain start '" + end + "'.");
			return "";
		} else if (!line.contains(end)) {
			stringUtilsLog.error("Error : line '" + line + "' does not contain end '" + end + "'.");
			return "";
		}

		return line.substring(line.indexOf(start) + start.length(), line.indexOf(end));
	}

	/**
	 * @return replaces text contained inside of string
	 */
	public static String replace(String line, String replace, String with) {
		if (line.contains(replace)) {
			line = line.replace(replace, with);
		}
		return line;
	}

	/**
	 * Rotates a string using a string point (uses delimiters as boundaries)
	 * I.E. : rotate("world Hello", " "); returns "Hello world"
	 */
	public static String rotate(String line, String rotatePoint) {
		return rotate(line, rotatePoint, "");
	}

	/**
	 * Rotates a string using a string point (uses delimiters as boundaries)
	 * I.E. : rotate("world Hello", " "); returns "Hello world"
	 * if replaceRotatePointWith is not "", replace rotatePoint with this
	 */
	public static String rotate(String line, String rotatePoint, String replaceRotatePointWith) {

		if (line == null || rotatePoint == null) {
			stringUtilsLog.error("Error, input is null.");
			return "";
		} else if (line.length() == 0 || rotatePoint.length() == 0) {
			stringUtilsLog.error("Error, input is empty.");
			return "";
		}

		// Get first word

		int beginningOfFirstWord = previousDelimiterIndex(line, line.indexOf(rotatePoint));
		int endOfFirstWord = line.indexOf(rotatePoint);

		//log.info("First word beginning : " + beginningOfFirstWord + ", end : " + endOfFirstWord);

		String firstWord = line.substring(beginningOfFirstWord, endOfFirstWord);

		stringUtilsLog.info("First word : " + firstWord);

		// Get second word

		int beginningOfSecondWord = line.indexOf(rotatePoint) + rotatePoint.length();
		int endOfSecondWord = nextDelimiterIndex(line, line.indexOf(rotatePoint) + rotatePoint.length());

		//log.info("Second word beginning : " + beginningOfSecondWord + ", end : " + endOfSecondWord);

		String secondWord = line.substring(beginningOfSecondWord, endOfSecondWord);

		stringUtilsLog.info("Second word : " + secondWord);

		// Rotate

		String beginningOfString = line.substring(0, beginningOfFirstWord);
		String endOfString = line.substring(endOfSecondWord);

		if (replaceRotatePointWith.equals("")) {
			return beginningOfString + secondWord + rotatePoint + firstWord + endOfString;
		} else {
			return beginningOfString + secondWord + replaceRotatePointWith + firstWord + endOfString;
		}
	}

	/**
	 * @param line
	 * @param afterKeyword look after this keyword in a string
	 * @return returns following keyword (using delimiters)
	 */
	public static String getFollowingWord(String line, String afterKeyword) {

		if (!line.contains(afterKeyword)) {
			stringUtilsLog.error("Error : '" + line + "' does not contain '" + afterKeyword + "'.");
			return "";
		}

		int endOfWord = line.indexOf(afterKeyword) + afterKeyword.length();

		int beginningOfWord = endOfWord + nextDelimiterIndex(line.substring(endOfWord));

		return line.substring(endOfWord, beginningOfWord);
	}

	/**
	 * @param string
	 * @param afterKeyword look after this keyword in a string
	 * @return returns following keyword (using delimiters)
	 */
	public static String getPreviousWord(String line, String beforeKeyword) {
		if (!line.contains(beforeKeyword)) {
			stringUtilsLog.error("Error : '" + line + "' does not contain '" + beforeKeyword + "'.");
			return "";
		}

		int endOfWord = line.indexOf(beforeKeyword);

		int beginningOfWord = previousDelimiterIndex(line, endOfWord);

		stringUtilsLog.info("End of word : " + endOfWord + ", beginning of word : " + beginningOfWord);

		return line.substring(beginningOfWord, endOfWord);
	}

	public static void main(String[] args) {
		stringUtilsLog.info("Loading...");

		stringUtilsLog.info("Get previous word : ");

		stringUtilsLog.info(getPreviousWord("Hey alde.", " alde"));
		stringUtilsLog.info(getPreviousWord("Dell is a good company", " company"));
		stringUtilsLog.info(getPreviousWord("(!(param1 in redrawCaches))", " in "));

	}


	public static String generateSeparatedStringFromStringList(List<String> params, String delimiter) {
		StringBuilder s = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			s.append(params.get(i));

			if (!(i == params.size() - 1)) {
				s.append(delimiter);
			}
		}
		return s.toString();
	}

	public static String generateSeparatedStringFromStringList(List<String> params) {
		return generateSeparatedStringFromStringList(params, ", ");
	}



}


package alde.commons.util.text;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtils {

	@Test
	public void test() {

		for (char c : delimiters) {
			System.out.println("'" + c + "'");
			assertEquals(true, isDelimiterCharacter(c));
		}

		assertEquals(5, nextDelimiterIndex("Hello my name is Alde", 0));
		assertEquals(5, nextDelimiterIndex("Hello my name is Alde", 0));
		assertEquals(5, previousDelimiterIndex("Hello my name is Alde", 7));
		assertEquals(5, previousDelimiterIndex("Hello my name is Alde", 7));

		assertEquals(7, previousDelimiterIndex("package com.utils.alde;", 11));

	}

	/**
	 * Marks the end of a word
	 */
	//@formatter:off
	private static final char[] delimiters = new char[] { 
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
	 * @param string 
	 * @return index of the next delimiter in a string
	 */
	public static int nextDelimiterIndex(String string) {
		return nextDelimiterIndex(string, 0);
	}

	/**
	 * @param string 
	 * @param beginIndex starts looking for delimiter at index
	 * @return index of the next delimiter in a string
	 */
	public static int nextDelimiterIndex(String string, int beginIndex) {

		if (beginIndex > string.length()) {
			System.err.println(
					"Begin index '" + beginIndex + "' cannot be further than string length '" + string.length() + "'.");
			return 0;
		}

		//

		char[] chars = string.toCharArray();

		for (int i = beginIndex + 1; i < chars.length; i++) {
			if (isDelimiterCharacter(chars[i])) {
				return i;
			}
		}

		return string.length();
	}

	/**
	 * @param string 
	 * @return index of the previous delimiter in a string
	 */
	public static int previousDelimiterIndex(String string) {
		return previousDelimiterIndex(string, 0);
	}

	/**
	 * @param string
	 * @param beginIndex starts looking for delimiter at index
	 * @return index of the previous delimiter in a string
	 */
	public static int previousDelimiterIndex(String string, int beginIndex) {

		if (beginIndex > string.length()) {
			System.err.println(
					"Begin index '" + beginIndex + "' cannot be further than string length '" + string.length() + "'.");
			return 0;
		}

		// 

		char[] chars = string.toCharArray();

		for (int i = beginIndex - 1; i >= 0; i--) {
			if (isDelimiterCharacter(chars[i])) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * @param string 
	 * @return text between 'start' and 'end'
	 */
	public static String getInbetween(String string, String start, String end) {
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
	public static String replace(String string, String replace, String with) {
		if (string.contains(replace)) {
			string = string.replace(replace, with);
		}
		return string;
	}

	/**
	 * Rotates a string using a string point (uses delimiters as boundaries)
	 * I.E. : rotate("world Hello", " "); returns "Hello world"
	 */
	public static String rotate(String string, String rotatePoint) {
		return rotate(string, rotatePoint, "");
	}

	/**
	 * Rotates a string using a string point (uses delimiters as boundaries)
	 * I.E. : rotate("world Hello", " "); returns "Hello world"
	 * if replaceRotatePointWith is not "", replace rotatePoint with this
	 */
	public static String rotate(String string, String rotatePoint, String replaceRotatePointWith) {

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

		//System.out.println("First word beginning : " + beginningOfFirstWord + ", end : " + endOfFirstWord);

		String firstWord = string.substring(beginningOfFirstWord, endOfFirstWord);

		System.out.println("First word : " + firstWord);

		// Get second word

		int beginningOfSecondWord = string.indexOf(rotatePoint) + rotatePoint.length();
		int endOfSecondWord = nextDelimiterIndex(string, string.indexOf(rotatePoint) + rotatePoint.length());

		//System.out.println("Second word beginning : " + beginningOfSecondWord + ", end : " + endOfSecondWord);

		String secondWord = string.substring(beginningOfSecondWord, endOfSecondWord);

		System.out.println("Second word : " + secondWord);

		// Rotate

		String beginningOfString = string.substring(0, beginningOfFirstWord);
		String endOfString = string.substring(endOfSecondWord);

		if (replaceRotatePointWith.equals("")) {
			return beginningOfString + secondWord + rotatePoint + firstWord + endOfString;
		} else {
			return beginningOfString + secondWord + replaceRotatePointWith + firstWord + endOfString;
		}
	}

	/**
	 * @param string
	 * @param afterKeyword look after this keyword in a string
	 * @return returns following keyword (using delimiters)
	 */
	public static String getFollowingWord(String string, String afterKeyword) {

		if (!string.contains(afterKeyword)) {
			System.err.println("Error : '" + string + "' does not contain '" + afterKeyword + "'.");
			return "";
		}

		int endOfKeywordIndex = string.indexOf(afterKeyword) + afterKeyword.length();

		int nextDelimiterIndex = endOfKeywordIndex + nextDelimiterIndex(string.substring(endOfKeywordIndex));

		return string.substring(endOfKeywordIndex, nextDelimiterIndex);
	}

}

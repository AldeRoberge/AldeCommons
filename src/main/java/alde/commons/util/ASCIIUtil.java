package alde.commons.util;

/**
 * This file requries you to have Eclipse set to UTF-8
 * 
 * Window -> Preferences -> General -> Workspace -> UTF-8
 */
public class ASCIIUtil {

	public static String getLoadingBarFor(int size, int current, int max) {

		//System.out.println("Size : " + size + ", current : " + current + ", max : " + max);

		int percentage, amountBlack, amountWhite;

		if (current == 0 || max == 0) {
			percentage = 0;
			amountBlack = 0;
			amountWhite = size;
		} else {
			percentage = current * 100 / max;
			amountBlack = (size * current / max);
			amountWhite = size - amountBlack;
		}

		//System.out.println("Amount black : " + amountBlack + " amount white : " + amountWhite);

		StringBuilder s = new StringBuilder();

		for (int i = 0; i <= amountBlack; i++) {
			s.append("▓");
		}

		for (int i = 0; i <= amountWhite; i++) {
			s.append("░");
		}

		//System.out.println("Percentage : " + percentage);

		return s.toString();

	}

}

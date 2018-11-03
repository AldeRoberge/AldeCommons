package alde.commons.util.math;

import java.util.Random;

public class MathUtil {

	public static int getMaxValue(int[] arr) {

		int max = 0;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}

	public static int percentage(int current, int max) {
		if (max == 0) {
			return 100;
		}

		return current * 100 / max;

	}

	public static int getMinValue(int[] arr) {

		int min = 0;
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
			}
		}
		return min;
	}

	public static int getRandomNumberInRange(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

}

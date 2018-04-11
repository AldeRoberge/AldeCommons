package alde.commons.util.math;

class MathUtil {

	public static int getMaxValue(int[] arr) {

		int max = 0;
		for (int i = 1; i < arr.length; i++ ) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
		return max;
	}

	public static int getMinValue(int[] arr) {

		int min = 0;
		for (int i = 1; i < arr.length; i++ ) {
			if (arr[i] < min) {
				min = arr[i];
			}
		}
		return min;
	}
}

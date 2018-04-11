package util;

public class StringGenerator {

	private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String randomAlphaNumeric(int count) {

		StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		return builder.toString();

	}

	private static final String NUMERIC_STRING = "0123456789";

	public static String getRandomInt() {
		StringBuilder builder = new StringBuilder();

		int character = (int) (Math.random() * NUMERIC_STRING.length());
		builder.append(NUMERIC_STRING.charAt(character));

		return builder.toString();
	}

}

package alde.commons.network.batch;

import java.util.Arrays;

public class Test {

	private static Handler handler = new Handler(10);

	public static void main(String[] args) {

		System.out.println("Running...");

		handler.addTask(new GetWebsiteTaskAvoidAnswer("https://www.google.com/", strings -> {
			System.out.println("Got website answer : " + Arrays.asList(strings));
		}, "", 1));

		handler.addTask(new GetWebsiteTaskAvoidAnswer("https://www.facebook.com/", strings -> {
			System.out.println("Got website answer : " + Arrays.asList(strings));
		}, "", 1));

		handler.addTask(new GetWebsiteTaskAvoidAnswer("https://www.google.com/", strings -> {
			System.out.println("Got website answer : " + Arrays.asList(strings));
		}, "", 1));

		System.exit(0);

	}

}

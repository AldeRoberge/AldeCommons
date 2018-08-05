package alde.commons.network.batch;

import java.util.List;
import java.util.function.Consumer;

public class Test {

	private static Handler handler = new Handler(10);

	public static void main(String[] args) {

		System.out.println("Running...");

		handler.addTask(new GetWebsiteTask("google.com", "hey", 5, new Consumer<List<String>>() {
			@Override
			public void accept(List<String> strings) {
				System.out.println("Received content : " + strings.size());
			}
		}));

		handler.addTask(new GetWebsiteTask("facebook.com", "hey", 5, new Consumer<List<String>>() {
			@Override
			public void accept(List<String> strings) {
				System.out.println("Hey");
			}
		}));


		handler.addTask(new GetWebsiteTask("reddit.com", "hey", 5, new Consumer<List<String>>() {
			@Override
			public void accept(List<String> strings) {
				System.out.println("Hey");
			}
		}));


		System.exit(0);

	}

}

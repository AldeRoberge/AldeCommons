package alde.commons.network.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import alde.commons.task.Task;

public class GetWebsiteTask extends Task {

	String URL;
	String avoid;

	int currentAttempt = 0;
	int maxAttempt;

	Consumer<List<String>> websiteContentConsumer;

	List<String> answer;

	public GetWebsiteTask(String URL, String avoid, int maxAttempt, Consumer<List<String>> websiteContentConsumer) {
		this.URL = URL;
		this.avoid = avoid;
		this.maxAttempt = maxAttempt;
		this.websiteContentConsumer = websiteContentConsumer;

		answer = new ArrayList<String>();
	}

	@Override
	protected void completed() {
		super.completed();
		websiteContentConsumer.accept(answer);
	}

}

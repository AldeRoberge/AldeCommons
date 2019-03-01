package alde.commons.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import alde.commons.task.Task;

public class GetWebsiteTask extends Task {

	String URL;
	String avoid;

	int currentAttempt = 0;
	int numberOfMaximumAttempts;

	private Consumer<List<String>> websiteContentConsumer;

	List<String> answer;

	public GetWebsiteTask(String URL, String toAvoid, int numberOfMaximumAttempts, Consumer<List<String>> websiteContentConsumer) {
		this.URL = URL;
		this.avoid = toAvoid;
		this.numberOfMaximumAttempts = numberOfMaximumAttempts;
		this.websiteContentConsumer = websiteContentConsumer;

		answer = new ArrayList<String>();
	}

	@Override
	protected void completed() {
		super.completed();
		websiteContentConsumer.accept(answer);
	}

}

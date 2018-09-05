package alde.commons.network.batch;

import alde.commons.network.GetWebsite;

import java.util.List;
import java.util.function.Consumer;

public class GetWebsiteTaskAvoidAnswer extends GetWebsiteTask {

	public String avoidAnswer;
	public int maxRetry;

	public GetWebsiteTaskAvoidAnswer(String url, Consumer<List<String>> consumer, String avoidAnswer, int maxRetry) {
		super(url, consumer);
		this.maxRetry = maxRetry;
		this.avoidAnswer = avoidAnswer;
	}

}

class GetWebsiteTask {

	public String url;
	public Consumer<List<String>> consumer;

	public GetWebsiteTask(String url, Consumer<List<String>> consumer) {
		this.url = url;
		this.consumer = consumer;
	}

}

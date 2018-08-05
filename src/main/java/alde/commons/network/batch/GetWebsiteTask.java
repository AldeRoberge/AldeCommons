package alde.commons.network.batch;

import java.util.List;
import java.util.function.Consumer;

public class GetWebsiteTask {

	public String url;
	public String avoidAnswer;
	public int maxRetry;
	public Consumer<List<String>> consumer;

	public GetWebsiteTask(String url, String avoidAnswer, int maxRetry, Consumer<List<String>> consumer) {
		this.maxRetry = maxRetry;
		this.avoidAnswer = avoidAnswer;
		this.url = url;
		this.consumer = consumer;
	}

}

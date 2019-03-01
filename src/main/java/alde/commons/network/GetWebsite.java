package alde.commons.network;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get website as string list using Proxy
 * <p>
 * Currently it gets free proxies from 4 website (github lists, free-proxy.net, etc).
 * <p>
 * It isn't thread safe.
 *
 * @author Alde
 */
public class GetWebsite {

	private static final Logger log = LoggerFactory.getLogger(GetWebsite.class);

	private static GetWebsite proxyHandlerImpl;

	private GetWebsiteWorkerHandler proxyHandler;

	private GetWebsite() {
		proxyHandler = GetWebsiteWorkerHandler.get();
	}

	public static GetWebsite get() {
		if (proxyHandlerImpl == null) {
			proxyHandlerImpl = new GetWebsite();
		}

		return proxyHandlerImpl;
	}

	/**
	 * @param URL      Website to get
	 * @param avoid    Website should not contain to be sure we haven't reached max requests per IP. Otherwise, change proxy.
	 * @param maxAttempt Number of max retries, will return the last string we got from the website (might be null)
	 */
	public void getWebsiteAsStringListUsingProxy(String URL, Consumer<List<String>> websiteContentConsumer,
														 String avoid, int maxAttempt) {
		proxyHandler.addTask(new GetWebsiteTask(URL, avoid, maxAttempt, websiteContentConsumer));
	}


}

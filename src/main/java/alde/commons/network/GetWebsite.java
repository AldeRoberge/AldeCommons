package alde.commons.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy Handler is aimed to be a top level utlity method of type 'get website as string'.
 * <p>
 * Currently it gets free proxies from 4 website (github lists, free-proxy.net, etc).
 * <p>
 * It isn't thread safe.
 *
 * @author Alde
 */
public class GetWebsite {

	public static boolean debug = false;

	private static final Logger log = LoggerFactory.getLogger(GetWebsite.class);

	private static GetWebsite proxyHandlerImpl;

	GetWebsiteWorkerHandler proxyHandler = GetWebsiteWorkerHandler.get();

	private static alde.commons.network.proxy.ProxyLeecher proxyLeecher = new alde.commons.network.proxy.ProxyLeecher();

	private GetWebsite() {
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
	private void getWebsiteAsStringListUsingProxy(String URL, Consumer<List<String>> websiteContentConsumer,
			String avoid, int maxAttempt) {
		proxyHandler.addTask(new GetWebsiteTask(URL, avoid, maxAttempt, websiteContentConsumer));
	}

	/**
	 * Returns the website as a String (without a proxy)
	 */
	public String getWebsiteAsString(String url) {

		List<String> websiteContent = getWebsiteAsStringList(url);

		StringBuilder output = new StringBuilder();
		for (String s : websiteContent) {
			output.append(s).append(System.lineSeparator());
		}
		return output.toString();

	}

	/**
	 * Returns the website as a String List (without a proxy)
	 */
	public List<String> getWebsiteAsStringList(String url) {

		List<String> websiteAsString = new ArrayList<>();

		URL oracle;
		try {
			oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				websiteAsString.add(inputLine);
			}
			in.close();
		} catch (IOException e) {
			//log.error("Error with url : '" + url + "'.");
			//e.printStackTrace();
		}

		return websiteAsString;
	}

}

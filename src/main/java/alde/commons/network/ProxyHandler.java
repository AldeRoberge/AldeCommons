package alde.commons.network;

import java.util.List;

/**
 * Proxy Handler is aimed to be a top level utlity method of type 'get website as string'.
 *
 * Currently it gets free proxies from 4 website (github lists, free-proxy.net, etc).
 *
 * It isn't thread safe.
 *
 * @see ProxyHandlerImpl
 *
 * @author Alde
 */
public abstract class ProxyHandler {

	/**
	 * @param url Website to get
	 * @param error Website should not contain to be sure we haven't reached max requests per IP. Otherwise, change proxy.
	 * @param maxRetry Number of max retries, will return the last string we got from the website (might be null)
	 * @return returns the website as String (can be null)
	 */
	public abstract List<String> getWebsiteAsStringList(String url, String error, int maxRetry);

	/**
	 * @see getWebsiteAsStringList
	 * @return website content as String delimited by System.lineSeparator()
	 */
	public abstract String getWebsiteAsString(String url, String error, int maxRetry);

}
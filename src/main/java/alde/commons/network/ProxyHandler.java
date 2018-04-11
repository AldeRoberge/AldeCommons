package alde.commons.network;

/**
 * Proxy Handler is aimed to be a top level utlity method of type 'get website as string'.
 *
 * Currently it gets free proxies from 4 website (github lists, free-proxy.net, etc).
 *
 * It isn't thread safe.
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
	public abstract String getWebsiteAsString(String url, String error, int maxRetry);

}

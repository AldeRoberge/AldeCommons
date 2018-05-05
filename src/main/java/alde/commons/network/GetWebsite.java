package alde.commons.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.util.text.StackTraceToString;

/**
 * Proxy Handler is aimed to be a top level utlity method of type 'get website as string'.
 *
 * Currently it gets free proxies from 4 website (github lists, free-proxy.net, etc).
 *
 * It isn't thread safe.
 *
 * @author Alde
 */
public class GetWebsite {

	public static boolean debug = false;

	private static final Logger log = LoggerFactory.getLogger(GetWebsite.class);

	static GetWebsite proxyHandlerImpl;

	/** List of ProxyWrapper (String host, int port) */
	private static List<ProxyWrapper> proxies = new ArrayList<>();

	private GetWebsite() {
		reloadAllProxies();
	}

	public static GetWebsite get() {
		if (proxyHandlerImpl == null) {
			proxyHandlerImpl = new GetWebsite();
		}

		return proxyHandlerImpl;
	}

	/** Get new proxies (from free proxy lists) */
	private void reloadAllProxies() {
		log.info("Reloading proxies...");

		Set<ProxyWrapper> hs = new HashSet<>(ProxyLeecher.getProxies());
		proxies.clear();
		proxies.addAll(hs);

		log.info("Total of " + proxies.size() + " proxies.");
	}

	public String getWebsiteAsStringUsingProxy(String url, String error, int maxRetry) {

		List<String> websiteContent = getWebsiteAsStringListUsingProxy(url, error, maxRetry);

		StringBuilder output = new StringBuilder();
		for (String s : websiteContent) {
			output.append(s + System.lineSeparator());
		}
		return output.toString();

	}

	/**
	 * @param url Website to get
	 * @param error Website should not contain to be sure we haven't reached max requests per IP. Otherwise, change proxy.
	 * @param maxRetry Number of max retries, will return the last string we got from the website (might be null)
	 * @return returns the website as String (can be null)
	 */
	public List<String> getWebsiteAsStringListUsingProxy(String url, String error, int maxRetry) {
		List<String> websiteAsString = new ArrayList<>();

		for (int i = 0; i < maxRetry; i++) {

			if (i > 0) { // Second attempt
				log.debug("Attempt #" + (i + 1) + " out of " + maxRetry + " for website '" + url + "'.");
			}

			ProxyWrapper proxy = getProxy();

			websiteAsString = proxy.getWebsiteAsStringList(url);

			if (websiteAsString.contains(error)) {
				proxy.isValid = false;

				log.debug("Answer has error... Retrying...");
			} else {
				if (!websiteAsString.isEmpty()) {
					return websiteAsString;
				} else {
					log.error("String answer for '" + url + "' is all blank... Retrying...");
				}
			}
		}

		log.warn("Error : Max retry (" + maxRetry + ") reached! Returning '" + websiteAsString + "'.");

		return websiteAsString;
	}

	private ProxyWrapper getProxy() {

		ProxyWrapper proxy = null;

		// Find a valid proxy
		for (ProxyWrapper proxyWrapper : proxies) {
			if (proxyWrapper.isValid) {
				proxy = proxyWrapper;
				break;
			}
		}

		if (proxy == null) {
			log.debug("No valid proxies... Reloading proxies...");

			reloadAllProxies();

			while (proxy == null) {

				for (ProxyWrapper proxyWrapper : proxies) {
					if (proxyWrapper.isValid) {
						proxy = proxyWrapper;
					}
				}

				log.info("Retrying in 1 sec...");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return proxy;
	}

	/** Returns the website as a String (without a proxy) */
	public String getWebsiteAsString(String url) {

		List<String> websiteContent = getWebsiteAsStringList(url);

		StringBuilder output = new StringBuilder();
		for (String s : websiteContent) {
			output.append(s + System.lineSeparator());
		}
		return output.toString();

	}

	/** Returns the website as a String List (without a proxy) */
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
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return websiteAsString;
	}

}

class ProxyLeecher {

	private static final Logger log = LoggerFactory.getLogger(ProxyLeecher.class);

	public static List<ProxyWrapper> getProxies() {

		List<ProxyWrapper> proxies = new ArrayList<>();

		log.info("Getting all proxies...");

		proxies.addAll(getProxiesFromFreeProxyListDotNet("https://www.us-proxy.org/"));
		proxies.addAll(getProxiesFromFreeProxyListDotNet("https://free-proxy-list.net/"));
		proxies.addAll(getProxiesFromGithub());
		proxies.addAll(getProxiesFromSecondGithub());

		log.info("Total of " + proxies.size() + " proxies.");

		Collections.shuffle(proxies);

		return proxies;
	}

	private static List<ProxyWrapper> getProxiesFromSecondGithub() {

		List<ProxyWrapper> proxies = new ArrayList<>();

		URL oracle;
		try {
			oracle = new URL("https://raw.githubusercontent.com/clarketm/proxy-list/master/proxy-list.txt");

			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {

				try {

					String[] line = inputLine.split(" ");

					String host = line[0].substring(0, inputLine.indexOf(":"));
					String port = line[0].substring(inputLine.indexOf(":") + 1);

					proxies.add(new ProxyWrapper(host, Integer.parseInt(port)));

				} catch (Exception e) {
					// Left empty
				}

			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("Found " + proxies.size() + " proxies from github.");

		return proxies;

	}

	private static List<ProxyWrapper> getProxiesFromGithub() {

		List<ProxyWrapper> proxies = new ArrayList<>();

		URL oracle;
		try {
			oracle = new URL("https://raw.githubusercontent.com/a2u/free-proxy-list/master/free-proxy-list.txt");

			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {

				try {

					String host = inputLine.substring(0, inputLine.indexOf(":"));
					String port = inputLine.substring(inputLine.indexOf(":") + 1);

					proxies.add(new ProxyWrapper(host, Integer.parseInt(port)));

				} catch (Exception e) {
					// Left empty
				}
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("Found " + proxies.size() + " proxies from github.");

		return proxies;

	}

	private static List<ProxyWrapper> getProxiesFromFreeProxyListDotNet(String s) {

		List<ProxyWrapper> proxies = new ArrayList<>();

		String website = getWebsiteAsString(s);

		if (website != null) {

			if (website.contains("<tbody>")) {

				String proxiesAKDW = website.substring(website.indexOf("<tbody>") + 7, website.indexOf("</tbody>"));

				String[] proxiesL = proxiesAKDW.split("</tr>");

				for (String proxy : proxiesL) {

					try {

						String[] proxyL = proxy.split("</td>");

						String host = proxyL[0].replace("<tr><td>", "");
						String port = proxyL[1].replace("<td>", "");

						proxies.add(new ProxyWrapper(host, Integer.parseInt(port)));

					} catch (Exception e) {
						// Left empty
					}

				}

			} else {
				log.error("Error with getting proxies from 'free-proxy-list.net', answer returned : ");
				log.error(website);
			}

		} else {
			log.warn("Website is null... Skipping...");
		}

		log.info("Found " + proxies.size() + " proxies from '" + s + "'.");

		return proxies;

	}

	/**
	 * @see getWebsiteAsStringList
	 * @return website content as String delimited by System.lineSeparator()
	 */
	private static String getWebsiteAsString(String s) {

		URLConnection connection;
		try {
			connection = new URL(s).openConnection();

			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			BufferedReader r = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();

		} catch (IOException e) {
			log.error("Error while trying to get website '" + s + "'.");
			log.error(StackTraceToString.sTTS(e));
		}

		return null;

	}

}

class ProxyWrapper {

	private static final Logger log = LoggerFactory.getLogger(ProxyWrapper.class);

	private String host;
	private int port;

	boolean isValid = true;

	public ProxyWrapper(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public List<String> getWebsiteAsStringList(String url) {

		List<String> websiteContent = new ArrayList<String>();

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
		try {
			URLConnection conn = new URL(url).openConnection(proxy);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				websiteContent.add(inputLine);
			}

			in.close();

			return websiteContent;
		} catch (IOException e) {
			if (GetWebsite.debug)
				log.error("Error with proxy : " + e.getMessage());
		}
		return websiteContent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProxyWrapper that = (ProxyWrapper) o;
		return Objects.equals(host, that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(host);
	}

}

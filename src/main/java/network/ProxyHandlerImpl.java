package network;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.text.StackTraceToString;
import util.text.StringGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

public class ProxyHandlerImpl extends ProxyHandler {

	private static final Logger log = LoggerFactory.getLogger(ProxyHandlerImpl.class);

	/** List of ProxyWrapper (String host, int port) */
	private static List<ProxyWrapper> proxies = new ArrayList<>();

	public ProxyHandlerImpl() {
		reloadAllProxies();
	}

	/** Get new proxies (from free proxy lists) */
	private void reloadAllProxies() {
		Set<ProxyWrapper> hs = new HashSet<>(ProxyLeecher.getProxies());
		proxies.clear();
		proxies.addAll(hs);

		Collections.shuffle(proxies);

		log.info("Total of " + proxies.size() + " proxies.");

	}

	@Override
	public String getWebsiteAsString(String url, String error, int maxRetry) {
		String websiteAsString = "null";

		for (int i = 0; i < maxRetry; i++) {

			if (i > 0) { // Second attempt
				log.debug("Attempt #" + (i + 1) + " out of " + maxRetry + " for website '" + url + "'.");
			}

			ProxyWrapper proxy = getProxy();

			websiteAsString = proxy.getWebsiteAsString(url, true);

			if (websiteAsString.contains(error)) {
				proxy.isValid = false;
			} else {
				if (!StringUtils.isAllBlank(websiteAsString)) {
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

		// Get valid proxy
		ProxyWrapper proxy = null;

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

}

class ProxyLeecher {

	private static final Logger log = LoggerFactory.getLogger(ProxyLeecher.class);

	public static List<ProxyWrapper> getProxies() {

		List<ProxyWrapper> allProxies = new ArrayList<>();

		log.info("Getting all proxies...");

		allProxies.addAll(getProxiesFromFreeProxyListDotNet("https://www.us-proxy.org/"));
		allProxies.addAll(getProxiesFromFreeProxyListDotNet("https://free-proxy-list.net/"));
		allProxies.addAll(getProxiesFromGithub());
		allProxies.addAll(getProxiesFromSecondGithub());

		log.info("Total of " + allProxies.size() + " proxies.");

		return allProxies;
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

	private boolean debug = false;

	private static final Logger log = LoggerFactory.getLogger(ProxyWrapper.class);

	private String host;
	private int port;

	boolean isValid = false;

	public ProxyWrapper(String host, int port) {
		this.host = host;
		this.port = port;

		new Thread(new Runnable() {
			public void run() {
				if (debug)
					log.debug("Testing own validity...");

				testValidity();
			}
		}).start();
	}

	private static final String TEST_URL = "http://www.realmofthemadgod.com/char/list?guid=" + StringGenerator.randomAlphaNumeric(10);
	private static final String SUCCESS = "nextCharId";

	private void testValidity() {

		String answer = getWebsiteAsString(TEST_URL, false);

		isValid = answer.contains(SUCCESS);
	}

	public String getWebsiteAsString(String url, boolean debugLog) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
		try {
			URLConnection conn = new URL(url).openConnection(proxy);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder output = new StringBuilder();

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				output.append(inputLine);
			}

			in.close();

			return output.toString();
		} catch (IOException e) {
			if (debugLog)
				log.error("Error with proxy : " + e.getMessage());
		}
		return "";
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

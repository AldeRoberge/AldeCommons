package alde.commons.network.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.fileImporter.FileImporter;
import alde.commons.util.math.MathUtil;
import alde.commons.util.text.StackTraceToString;

public class ProxyLeecher {

	private static Logger log = LoggerFactory.getLogger(ProxyLeecher.class);

	
	private static boolean isLoadingProxies;

	private static List<ProxyWrapper> proxies = new ArrayList<>();

	public static ProxyWrapper takeProxy() {

		synchronized (proxies) {

			if (proxies.size() <= 10 && !isLoadingProxies) {
				isLoadingProxies = true;

				log.info("Reloading proxies...");

				for (ProxyWrapper proxy : getProxies()) {
					proxies.add(proxy);
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			ProxyWrapper p;

			try {

				while (!proxies.isEmpty()) {

					int proxyNum = MathUtil.getRandomNumberInRange(0, proxies.size());
					p = proxies.remove(proxyNum);

					if (p.isValid) {
						return p;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			log.error("No more proxies!");

			return null;

		}
	}


	private static List<ProxyWrapper> getProxies() {

		List<ProxyWrapper> proxies = new ArrayList<>();

		log.info("Getting all proxies...");

		//proxies.addAll(getProxiesFromFateZero());
		proxies.addAll(getProxiesFromFreeProxyListDotNet("https://www.us-proxy.org/"));
		proxies.addAll(getProxiesFromFreeProxyListDotNet("https://free-proxy-list.net/"));
		proxies.addAll(getProxiesFromGithub("https://raw.githubusercontent.com/a2u/free-proxy-list/master/free-proxy-list.txt"));
		proxies.addAll(getProxiesFromGithub("https://raw.githubusercontent.com/x-o-r-r-o/proxy-list/master/proxy-list.txt"));
		proxies.addAll(getProxiesFromGithub("https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks.txt"));
		proxies.addAll(getProxiesFromSecondGithub());

		log.info("Found a total of " + proxies.size() + " proxies.");

		Collections.shuffle(proxies);

		isLoadingProxies = false;

		return proxies;
	}

	public static void main(String[] args) {
		//System.out.println("Loading...");
		/*for (ProxyWrapper proxy : getProxiesFromFateZero()) {
			System.out.println(proxy);
		}*/
	}

	/**private static List<ProxyWrapper> getProxiesFromFateZero() {
	
		List<ProxyWrapper> proxies = new ArrayList<>();
	
		URL oracle;
		try {
			oracle = new URL("https://raw.githubusercontent.com/fate0/proxylist/master/proxy.list");
	
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
	
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
	
				try {
	
					String host = inputLine.substring(inputLine.indexOf("\"host\": \"") + 9, inputLine.indexOf("\", \"country\""));
					String port = inputLine.substring(inputLine.indexOf("\"port\": ") + 8, inputLine.indexOf(", \"type\""));
	
					proxies.add(new ProxyWrapper(host, Integer.parseInt(port)));
	
				} catch (Exception e) {
					e.printStackTrace();
				}
	
			}
	
			in.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		log.info("Found " + proxies.size() + " proxies from fatezero github.");
	
		return proxies;
	
	}*/

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

					proxies.add(new ProxyWrapper(host, Integer.parseInt(port), "second github"));

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

	/**
	 * Get proxies from url
	 * 
	 * format :
	 * 
	 * email : password
	 */
	private static List<ProxyWrapper> getProxiesFromGithub(String url) {

		List<ProxyWrapper> proxies = new ArrayList<>();

		URL oracle;
		try {
			oracle = new URL(url);

			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {

				try {

					String host = inputLine.substring(0, inputLine.indexOf(":"));
					String port = inputLine.substring(inputLine.indexOf(":") + 1);

					proxies.add(new ProxyWrapper(host, Integer.parseInt(port), url));

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

	private static List<ProxyWrapper> getProxiesFromFreeProxyListDotNet(String url) {

		List<ProxyWrapper> proxies = new ArrayList<>();

		String website = getWebsiteAsString(url);

		if (website != null) {

			if (website.contains("<tbody>")) {

				String proxiesAKDW = website.substring(website.indexOf("<tbody>") + 7, website.indexOf("</tbody>"));
				String[] proxiesL = proxiesAKDW.split("</tr>");

				for (String proxy : proxiesL) {
					try {

						String[] proxyL = proxy.split("</td>");

						String host = proxyL[0].replace("<tr><td>", "");
						String port = proxyL[1].replace("<td>", "");

						proxies.add(new ProxyWrapper(host, Integer.parseInt(port), url));

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

		log.info("Found " + proxies.size() + " proxies from '" + url + "'.");

		return proxies;

	}

	/**
	 * @return website content as String delimited by System.lineSeparator()
	 */
	private static String getWebsiteAsString(String s) {

		URLConnection connection;
		try {
			connection = new URL(s).openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			connection.connect();

			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

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
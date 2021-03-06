package alde.commons.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.network.proxy.ProxyLeecher;
import alde.commons.network.proxy.ProxyWrapper;
import alde.commons.task.Worker;

/**
 * GetWebsiteWorker receives GetWebsiteTasks, and using his proxy, gets the data from the website.
 * If the proxy is bad, it changes for a new one.
 */
public class GetWebsiteWorker extends Worker<GetWebsiteTask> {

	private static Logger log = LoggerFactory.getLogger(GetWebsiteWorker.class);

	private ProxyWrapper proxyWrapper;

	public GetWebsiteWorker(String workerName) {
		super(workerName);
		changeProxy();
	}

	private void changeProxy() {
		proxyWrapper = ProxyLeecher.takeProxy();
	}

	@Override
	public void receiveTask(GetWebsiteTask t) {
		super.receiveTask(t);
		checkForWebsiteLoop();
	}

	private void checkForWebsiteLoop() {

		task.currentAttempt++;
		task.answer.clear();

		if (task.currentAttempt > task.numberOfMaximumAttempts) {
			log.info("Max attempt (" + (task.currentAttempt - 1) + "/" + task.numberOfMaximumAttempts + ") reached.");
			completeTask();
		} else {
			boolean failed = false;

			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyWrapper.host, proxyWrapper.port));
			try {
				URLConnection conn = new URL(task.URL).openConnection(proxy);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					task.answer.add(inputLine);
				}
				in.close();
			} catch (IOException e) {
				log.debug("Error with proxy '" + proxyWrapper + "', " + e.getMessage());
				failed = true;
			}

			if (!failed) {

				StringBuilder answer = new StringBuilder();

				for (String content : task.answer) {

					answer.append(content);

					if (content.contains(task.avoid)) {
						log.error("Retrying, website contains the string we're trying to avoid : " + answer.toString());
						failed = true;
					}
				}
			}

			if (!failed) {
				completeTask();
			} else {
				changeProxy();
				checkForWebsiteLoop();
			}

		}

	}

}

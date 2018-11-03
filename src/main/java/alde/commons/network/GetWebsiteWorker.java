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

public class GetWebsiteWorker extends Worker<GetWebsiteTask> {

	private static Logger log = LoggerFactory.getLogger(GetWebsiteWorker.class);

	ProxyWrapper proxyWrapper;

	public GetWebsiteWorker() {
		updateProxy();
	}

	private void updateProxy() {
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

		if (task.currentAttempt > task.maxAttempt) {
			System.out.println("Max attempt (" + task.currentAttempt + "/" + task.maxAttempt + ") reached.");
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
				log.error("Error with proxy : " + e.getMessage());
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
				updateProxy();
				checkForWebsiteLoop();
			}

		}

	}

}

package alde.commons.network.batch;

import alde.commons.network.ProxyLeecher;
import alde.commons.network.ProxyWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Worker {

	private Logger log = LoggerFactory.getLogger(Worker.class);

	private ProxyWrapper proxyWrapper;

	private boolean isBusy;

	public boolean isBusy() {
		return isBusy;
	}

	public Worker() {
		setNewProxy();
	}

	private void setNewProxy() {
		System.out.println("Setting new proxy : ");
		proxyWrapper = ProxyLeecher.takeProxy();
	}

	public void receiveTask(GetWebsiteTaskAvoidAnswer task) {
		isBusy = true;

		new Thread("Website answer") {

			@Override
			public void run() {
				checkForWebsite(task, 0);
			}

			private void accept(List<String> websiteContent) {
				task.consumer.accept(websiteContent);
				isBusy = false;
			}

			private void checkForWebsite(GetWebsiteTaskAvoidAnswer task, int currentAttempt) {

				boolean failed = false;

				List<String> websiteContent = new ArrayList<String>();

				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyWrapper.host, proxyWrapper.port));
				try {
					URLConnection conn = new URL(task.url).openConnection(proxy);
					BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						websiteContent.add(inputLine);
					}

					in.close();

				} catch (IOException e) {
					failed = true;
					log.error("Error with proxy : " + e.getMessage());
				}

				for (String content : websiteContent) {
					if (content.contains(task.avoidAnswer)) {
						log.error("Website contains strings we didn't want");
						failed = true;
					}
				}

				if (failed) {
					if (task.maxRetry >= currentAttempt) {
						setNewProxy();
						checkForWebsite(task, currentAttempt);
					} else {
						accept(websiteContent);
					}
				} else {
					accept(websiteContent);
				}

			}
		}.start();

	}

}

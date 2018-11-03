package alde.commons.network.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper for Proxy
 */
public class ProxyWrapper {

	private static final Logger log = LoggerFactory.getLogger(ProxyWrapper.class);

	public String host;
	public int port;

	boolean isValid = true;

	public ProxyWrapper(String host, int port) {
		this.host = host;
		this.port = port;
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

	@Override
	public String toString() {
		return "Host : '" + host + "', Port : '" + port + "'.";
	}

}

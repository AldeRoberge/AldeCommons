package alde.commons.network.proxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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

    // Website it was leeched from
    public String from;

    boolean isValid = true;

    public ProxyWrapper(String host, int port, String from) {
        this.host = host;
        this.port = port;
        this.from = from;
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
        return "Host : '" + host + "', Port : '" + port + "', From : '" + from + "'.";
    }

}

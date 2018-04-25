package alde.commons.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebsiteReaderImpl extends WebsiteReader {

	public static boolean debug = false;

	private static final Logger log = LoggerFactory.getLogger(WebsiteReaderImpl.class);

	@Override
	public String getWebsiteAsString(String url) {

		List<String> websiteContent = getWebsiteAsStringList(url);

		StringBuilder output = new StringBuilder();
		for (String s : websiteContent) {
			output.append(s + System.lineSeparator());
		}
		return output.toString();

	}

	@Override
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
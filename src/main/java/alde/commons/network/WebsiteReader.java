package alde.commons.network;

import java.util.List;

public abstract class WebsiteReader {

	public abstract List<String> getWebsiteAsStringList(String url);

	public abstract String getWebsiteAsString(String url);

}

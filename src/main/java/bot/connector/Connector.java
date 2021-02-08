package bot.connector;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Connector {

	// User Agent
	private static String myUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36";

	/**
	 * Connects and returns source code of website
	 * 
	 * @param website, String
	 * @return source code of website, String
	 */
	public static String connect(String website) {
		try {
			// Connections with settings and parse to String
			return ((Document) Jsoup.connect(website).ignoreContentType(true).userAgent(myUserAgent)
					.referrer("http://www.google.com").timeout(3000).followRedirects(true).execute().parse())
							.outerHtml();
		} catch (IOException e) {
			System.err.println("Connection Error to: " + website);
		}
		// Error
		return "Error";
	}

	/**
	 * Connect and return webpage
	 * 
	 * @param website, String
	 * @return webpage, Document
	 * @throws IOException
	 */
	public static Document connectDocument(String website) throws IOException {
		// Connection with settings and parsed to Document
		return Jsoup.connect(website).ignoreContentType(true).userAgent(myUserAgent).referrer("http://www.google.com")
				.timeout(3000).followRedirects(true).execute().parse();
	}

	/**
	 * Scrape selected items off a webpage
	 * 
	 * @param page,     Document
	 * @param selector, String
	 * @return components of webpage based of selectors
	 */
	public static String scrape(Document page, String selector) {
		StringBuilder totalElements = new StringBuilder();
		String items = "";
		Elements product;
		ArrayList<String> results = new ArrayList<String>();
		// Selecting components
		product = page.select(selector);
		// Sorting components
		try {
			// Adding selector
			for (Element e : product) {
				results.add(e.text());
			}
			// Sorting results
			for (String s : results) {
				totalElements.append(s);
				totalElements.append("\n");
			}
			items = totalElements.toString();
		} catch (Exception e) {

		}
		return items;
	}
}

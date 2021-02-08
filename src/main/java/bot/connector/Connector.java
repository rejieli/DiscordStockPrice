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
	 * Tries 3 times to connect and return webpage
	 * 
	 * @param website, String
	 * @return webpage, Document
	 */
	public static Document forceConnectDocument(String website) {
		int counter = 0;
		// Tries connecting 3 times
		while (counter < 3) {
			try {
				// Connection
				return connectDocument(website);
			} catch (Exception e) {
				counter++;
				System.err.println("Connection #" + counter + " Failed");
			}
		}
		System.err.println("Connection Error to: " + website);
		// Error
		return Jsoup.parse("Error");
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

	// If you want one string, set isSparate to false and use deepScrape.get(0)
	public static ArrayList<String> deepScrape(Document page, String selector, boolean isSeparate) {
		ArrayList<String> list = new ArrayList<String>();
		// Scraping
		String elements = page.select(selector).toString();
		// Format to desired type
		if (!isSeparate) {
			list.add(elements);
			return list;
		}
		String[] newLine = elements.split("\\r?\\n");
		// Putting array to an Arraylist
		for (String s : newLine) {
			list.add(s);
		}
		return list;
	}

	/**
	 * Scrapes links from website
	 * 
	 * @param aHrefs, ArrayList<String>
	 * @return All links from website based form aHrefs, ArrayList<String>
	 */
	public static ArrayList<String> scrapeLinks(ArrayList<String> aHrefs) {
		ArrayList<String> links = new ArrayList<String>();
		for (String aHref : aHrefs) {
			if (aHref.contains("href")) {
				aHref = aHref.substring(aHref.indexOf("href"));

				// Getting first two '"'
				char[] c = aHref.toCharArray();
				int counter = 0;
				int beginIndex = 0;
				int endIndex = 0;
				// Searching for the opening and closing '"'
				for (int i = 0; i < c.length; i++) {
					if (counter == 0) {
						if (c[i] == '"') {
							beginIndex = i + 1;
							counter++;
						}
					} else {
						if (c[i] == '"') {
							endIndex = i;
							break;
						}
					}
				}
				links.add(aHref.substring(beginIndex, endIndex));
			}
		}
		return links;
	}

	/**
	 * Scrapes a link from website
	 * 
	 * @param aHrefs, String
	 * @return Link from website based form aHrefs, String
	 */
	public static String scrapeLink(String aHref) {
		aHref = aHref.substring(aHref.indexOf("href"));

		// Getting first two "\"\""
		char[] c = aHref.toCharArray();
		int counter = 0;
		int beginIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < c.length; i++) {
			if (counter == 0) {
				if (c[i] == '"') {
					beginIndex = i + 1;
					counter++;
				}
			} else {
				if (c[i] == '"') {
					endIndex = i;
					break;
				}
			}
		}
		return aHref.substring(beginIndex, endIndex);
	}

	/**
	 * Scrape selected items off a webpage
	 * 
	 * @param page,     Document
	 * @param selector, String
	 * @param attr,     String
	 * @return components of webpage based of selectors and attributes
	 */
	public static String scrape(Document page, String selector, String attr) {
		// Getting all attributes into an array
		String[] attrs = attr.replaceAll("\\s+", "").split(",");

		StringBuilder totalElements = new StringBuilder();
		String items = "";
		Elements product;
		ArrayList<String> results = new ArrayList<String>();
		// Scraping page
		product = page.select(selector);
		// Sorting components
		try {
			for (Element e : product) {
				// Adding selector
				results.add(e.text());
				// Adding attributes
				for (int i = 0; i < attrs.length; i++) {
					if (!e.attr(attrs[i]).isBlank()) {
						results.add(e.attr(attrs[i]));
					}
				}
			}
			// formatting results
			for (String s : results) {
				totalElements.append(s);
				totalElements.append("\n");
			}
			items = totalElements.toString();
		} catch (Exception e) {

		}
		return items;
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
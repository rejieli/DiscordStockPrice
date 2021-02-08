package bot.connector;

import java.io.IOException;

import org.jsoup.nodes.Document;

import bot.stock.Stock;

public class PriceFetcher {

	public static Stock getPrice(String stock) {
		try {
			Document page = Connector.connectDocument(getLink(stock));
			String[] options = Connector.scrape(page, "#quote-header-info > div > div > div > span").split("\\R");
			if (options.length == 3) {
				return new Stock(stock, options[1], options[2]);
			}else {
				return new Stock("error", "error", "error");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Stock("error", "error", "error");
	}

	private static String getLink(String stock) {
		return "https://ca.finance.yahoo.com/quote/" + stock + "/history?p=" + stock;
	}

}

package bot.stock;

public class Stock {

	private String ticker;
	private String price;
	private String change;
	
	public Stock(String ticker, String price, String change) {
		this.ticker = ticker.toUpperCase();
		this.price = price;
		this.change = change;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	@Override
	public String toString() {
		return "TICKER: " + ticker + "\nPRICE: " + price + "\nCHANGE: " + change;
	}
	
	
	
}

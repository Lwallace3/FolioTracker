import java.io.Serializable;

public class Stock implements Serializable, stockInterface {

	private String tickerSymbol;
	private String stockName;
	private Integer numberOfStocks;
	private Double currentStockPrice;
	private Double valueHolding;
	private Double stockValueAtPurchase;
	private Double stockPriceHigh;
	private Double stockPriceLow;

	public Stock(String name, String symbol, int numOfStocks) throws NoSuchTickerException, WebsiteDataException {
		stockName = name;
		tickerSymbol = symbol;
		numberOfStocks = numOfStocks;
		currentStockPrice = Double.parseDouble(StrathQuoteServer.getLastValue(tickerSymbol));
		stockValueAtPurchase = currentStockPrice * numberOfStocks;
	}

	/**
	 * Setter
	 * @param name to be set
	 */

	@Override
	public void setShareName(String name) {
		stockName = name;
	}

	/**
	 * Getter
	 * @return name of the stock
	 */
	@Override
	public String getShareName() {
		return stockName;
	}

	/**
	 * Setter
	 * @param s symbol to be set
	 */
	@Override
	public void setTickerSymbol(String s) {
		tickerSymbol = s;
	}

	/**
	 * Getter
	 * @return the symbol of of stock
	 */
	@Override
	public String getTickerSymbol() {
		return tickerSymbol;
	}

	/**
	 * Setter
	 * @param a number to be set
	 */
	@Override
	public void setStocksNumber(Integer a) {
		numberOfStocks = a;
	}

	/**
	 * Getter
	 * @return number of stocks
	 */
	@Override
	public Integer getStocksNumber() {
		return numberOfStocks;
	}

	/**
	 * Setter
	 * @param a price to be set
	 */
	@Override
	public void setStockPrice(Double a) {
		currentStockPrice = a;
	}

	/**
	 * Getter
	 * @return current stock price
	 */
	@Override
	public Double getStockPrice() {
		return currentStockPrice;
	}

	/**
	 * Getter
	 * @return the value of the holding of the stock
	 * based on share number and price
	 */
	@Override
	public Double getValueHolding() {
		valueHolding =numberOfStocks * currentStockPrice;
		return valueHolding;
	}

	/**
	 * Getter
	 * @return the value of stock at the time of purchase
	 */
	@Override
	public Double getInitialStockValue() {
		return stockValueAtPurchase;
	}

	/**
	 * Getter
	 * @return the money made on the value holding of a stock
	 */
	@Override
	public Double getGainLoss() {
		return getValueHolding()- stockValueAtPurchase;
	}

	@Override
	public void purchaseStocks(int num){
		stockValueAtPurchase = stockValueAtPurchase + (num * currentStockPrice);
		numberOfStocks += num;
	}

	@Override
	public void sellStocks(int num){
		stockValueAtPurchase -= num * currentStockPrice;
		numberOfStocks -= num;
	}

	@Override
	public void updateHighsAndLows() {
		if (valueHolding > stockPriceHigh) {
			stockPriceHigh = valueHolding;
		} else if (valueHolding < stockPriceLow) {
			stockPriceLow = valueHolding;
		}
	}
}

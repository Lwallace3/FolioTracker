import java.io.Serializable;
import java.util.*;

public class Folio implements Serializable, portfolio {

    private String folioName;
    private String ownerOfFolioUserName;
    private HashMap<String, Stock> stocks = new HashMap<>();

    public Folio(String folioName, String userName) {
        this.folioName = folioName;
        this.ownerOfFolioUserName = userName;
    }

    /**
     * Getters and Setters
     */
    @Override
    public void setFolioName(String name) { folioName = name; }

    @Override
    public void setOwnerOfFolioUserName(String user) { this.ownerOfFolioUserName = user; }

    @Override
    public String getOwnerOfFolioUserName(){ return ownerOfFolioUserName.toLowerCase().trim(); }

    @Override
    public String getName() { return folioName; }

    @Override
    public ArrayList<Stock> getStocks() { Collection<Stock> values = stocks.values();return new ArrayList<>(values);}

    /**
     * Methods for adding and removing stocks from the arrayList of stocks associated to the folio.
     * @param a
     */
    @Override
    public void addStock(Stock a) { stocks.put(a.getShareName(), a); }

    @Override
    public void removeStock(Stock a) { stocks.remove(a.getShareName()); }

    /**
     * Takes the ArrayList of stocks associated with the folio and returns the list of stocks
     * in different orders for the GUI by value, symbol or name.
     *
     * @return listOfStocks
     */
    @Override
    public ArrayList<Stock> sortStocksByValue() {
		Collection<Stock> values = stocks.values();
		ArrayList<Stock> listOfStocks = new ArrayList<>(values);

		listOfStocks.sort(Comparator.comparing(Stock::getStockPrice));
   		return listOfStocks;
    }

    @Override
    public ArrayList<Stock> sortStocksBySymbol(){
		Collection<Stock> values = stocks.values();
		ArrayList<Stock> listOfStocks = new ArrayList<>(values);

    	listOfStocks.sort((s1, s2) -> s1.getTickerSymbol().compareToIgnoreCase(s2.getTickerSymbol()));
    	return listOfStocks;
	}

	@Override
    public ArrayList<Stock> sortStocksByNameAscending(){
		Collection<Stock> values = stocks.values();
		ArrayList<Stock> listOfStocks = new ArrayList<>(values);

		listOfStocks.sort((s1, s2) -> s1.getShareName().compareToIgnoreCase(s2.getShareName()));

		return listOfStocks;
	}

	@Override
    public ArrayList<Stock> sortStocksByNameDescending(){
		Collection<Stock> values = stocks.values();
		ArrayList<Stock> listOfStocks = new ArrayList<>(values);

		listOfStocks.sort((s1, s2) -> s1.getShareName().compareToIgnoreCase(s2.getShareName()));
		Collections.reverse(listOfStocks);
		return listOfStocks;
	}

    /**
     * Refreshes the price by iterating through each Stock and using the .getLastValue() function from the provided
     * StrathQuoteServer file, it updates the price of every stock
     *
     * @throws NoSuchTickerException
     * @throws WebsiteDataException
     */
    @Override
    public void refreshStocks() throws NoSuchTickerException, WebsiteDataException {
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            String key = entry.getKey();
            String currentStockValue = StrathQuoteServer.getLastValue(stocks.get(key).getTickerSymbol());
            stocks.get(key).setStockPrice(Double.parseDouble(currentStockValue));
//            stocks.get(key).updateHighsAndLows();
        }
    }

    /**
     * Iterated through each stored stock in the folio, calculating and adding up all the values then producing
     * the total value for the given folio.
     *
     * @return totalValue
     */
    @Override
    public int getFolioValue() {
        int totalValue = 0;

        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            Stock currentStock = stocks.get(entry.getKey());
            totalValue += currentStock.getStockPrice() * currentStock.getStocksNumber();
        }

        return totalValue;
    }

}

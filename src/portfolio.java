import java.util.ArrayList;

public interface portfolio {
    void setFolioName(String name);

    void setOwnerOfFolioUserName(String user);

    String getOwnerOfFolioUserName();

    String getName();

    void addStock(Stock a);

    ArrayList<Stock> getStocks();

    ArrayList<Stock> sortStocksByValue();

    ArrayList<Stock> sortStocksBySymbol();

    ArrayList<Stock> sortStocksByNameAscending();

    ArrayList<Stock> sortStocksByNameDescending();

    void removeStock(Stock a);

    void refreshStocks() throws NoSuchTickerException, WebsiteDataException;

    int getFolioValue();
}

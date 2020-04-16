public interface stockInterface {
    void setShareName(String name);

    String getShareName();

    void setTickerSymbol(String s);

    String getTickerSymbol();

    void setStocksNumber(Integer a);

    Integer getStocksNumber();

    void setStockPrice(Double a);

    Double getStockPrice();

    Double getValueHolding();

    Double getInitialStockValue();

    Double getGainLoss();

    void purchaseStocks(int num);

    void sellStocks(int num);

    void updateHighsAndLows();
}

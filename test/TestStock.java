import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
//import org.junit.jupiter.api.Test;

public class TestStock {

	@Test
	public void stockName() throws WebsiteDataException, NoSuchTickerException {
		Stock s = new Stock("t1", "x", 20);
		assertEquals("t1", s.getShareName());
		String name  = "TSL";
		s.setShareName(name);
		assertEquals(name, s.getShareName());
	}
	
	@Test
	public void stockSymbol() throws WebsiteDataException, NoSuchTickerException {
		Stock s = new Stock("t2", "x", 20);
		assertEquals("x",s.getTickerSymbol());
		String symbol  = "TEX";
		s.setTickerSymbol(symbol);
		assertEquals(symbol, s.getTickerSymbol());
	}
	
	@Test
	public void numStocks () throws WebsiteDataException, NoSuchTickerException {
		Stock s = new Stock("t3", "x", 20);
		Integer test1 = 20;
		assertEquals(test1,s.getStocksNumber());
		Integer test2 = 50;
		s.setStocksNumber(test2);
		assertEquals(test2, s.getStocksNumber());
	}
	
	@Test
	public void stockPrice() throws WebsiteDataException, NoSuchTickerException {
		Stock s = new Stock("t4", "x", 20);
		Double a = 10.20;
		s.setStockPrice(a);
		assertEquals(a, s.getStockPrice());
	}
	@Test
	public void valueTotal() throws WebsiteDataException, NoSuchTickerException {
		Stock s = new Stock("a", "x", 20);
		s.setStockPrice(10.0);
		double price = 200.0;
		assertTrue(s.getValueHolding()==price);
	}



}

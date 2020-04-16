import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestFolio {


	//testing getters and setters
		@Test
		public void folioName() {
			Folio f = new Folio("test", "bob");
			assertEquals("test", f.getName());
			f.setFolioName("test1");
			assertEquals("test1",f.getName());
		}
		
		@Test
		public void folioOwnerUsername() {
			Folio f = new Folio("test", "bob");
			assertEquals("bob", f.getOwnerOfFolioUserName());
			f.setOwnerOfFolioUserName("jimmy");
			assertEquals("jimmy",f.getOwnerOfFolioUserName());
		}
		//testing the add stock function
		@Test
		public void folioAddStock() throws NoSuchTickerException, WebsiteDataException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("test1", "b", 20);
			Stock b = new Stock("test2","tex",34);
			assertEquals(0,f.getStocks().size());
			f.addStock(a);
			f.addStock(b);
			assertEquals(2,f.getStocks().size());
			assertTrue(f.getStocks().contains(a)&&f.getStocks().contains(b));
		}
		
		//testing the remove stock function
		@Test
		public void folioRemoveStock() throws NoSuchTickerException, WebsiteDataException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("test1", "x", 20);
			f.addStock(a);
			assertEquals(1,f.getStocks().size());
			f.removeStock(a);
			assertEquals(0,f.getStocks().size());
			assertFalse(f.getStocks().contains(a));
		}

		@Test
		public void folioSortValue() throws NoSuchTickerException, WebsiteDataException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("a", "tex", 10);
			Stock b = new Stock("b", "x", 10);
			Stock c = new Stock("c", "tsl", 10);
			Stock d = new Stock("d", "ezj", 10);
			f.addStock(a);
			f.addStock(b);
			f.addStock(c);
			f.addStock(d);
			ArrayList<Stock> s = f.sortStocksByValue();
			assertSame(s.get(0), b);
			assertSame(s.get(1),c);
			assertSame(s.get(2),a);
			assertSame(s.get(3),d);

		}

		@Test
		public void testSortStocksBySymbol() throws WebsiteDataException, NoSuchTickerException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("a", "tex", 10);
			Stock b = new Stock("b", "x", 10);
			Stock c = new Stock("c", "tsl", 10);
			Stock d = new Stock("d", "ezj", 10);
			f.addStock(a);
			f.addStock(b);
			f.addStock(c);
			f.addStock(d);
			ArrayList<Stock> s = f.sortStocksBySymbol();
			assertSame(s.get(0), d);
			assertSame(s.get(3),b);
		}

		
		@Test
		public void testSortStocksByNameAscending() throws NoSuchTickerException, WebsiteDataException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("steven", "tex", 20);
			Stock b = new Stock("bob", "x", 10);
			Stock c = new Stock("james", "tsl", 30);
			Stock d = new Stock("adam", "ezj", 40);
			f.addStock(a);
			f.addStock(b);
			f.addStock(c);
			f.addStock(d);
			ArrayList<Stock> s = f.sortStocksByNameAscending();
			assertEquals("adam",s.get(0).getShareName());
			assertEquals("steven",s.get(3).getShareName());
		}
		
		@Test
		public void testSortStocksByNameDescending() throws NoSuchTickerException, WebsiteDataException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("adam", "aa", 20);
			Stock b = new Stock("aidan", "bb", 10);
			Stock c = new Stock("bob", "cc", 30);
			Stock d = new Stock("steven", "dd", 40);
			f.addStock(a);
			f.addStock(b);
			f.addStock(c);
			f.addStock(d);
			ArrayList<Stock> s = f.sortStocksByNameDescending();
			assertEquals("steven",s.get(0).getShareName());
			assertEquals("adam",s.get(3).getShareName());
		}

		@Test
		public void testFolioValue() throws WebsiteDataException, NoSuchTickerException {
			Folio f = new Folio("test", "bob");
			Stock a = new Stock("adam", "tex", 20);
			Stock b = new Stock("aidan", "x", 10);
			Stock c = new Stock("bob", "tsl", 30);
			Stock d = new Stock("steven", "ezj", 40);
			a.setStockPrice(2.0);
			f.addStock(a);
			assertEquals(40,f.getFolioValue());

			b.setStockPrice(3.0);
			f.addStock(b);
			assertEquals(70,f.getFolioValue());

			c.setStockPrice(4.0);
			f.addStock(c);
			assertEquals(190,f.getFolioValue());

			d.setStockPrice(5.0);
			f.addStock(d);
			assertEquals(390,f.getFolioValue());
		}
}

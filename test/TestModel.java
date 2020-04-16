import org.junit.Test;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;


public class TestModel {

    @Test
    public void testModelWrite() throws WebsiteDataException, NoSuchTickerException {
        Folio f = new Folio("test", "bob");
        Stock a = new Stock("adam", "tex", 20);
        Stock b = new Stock("aidan", "x", 10);
        Stock c = new Stock("bob", "tsl", 30);
        Stock d = new Stock("steven", "ezj", 40);
        ArrayList<Folio> testList = new ArrayList<>();
        testList.add(f);

        Model.writeToFile(testList);
    }
    @Test
    public void testModelRead() throws WebsiteDataException, NoSuchTickerException, IOException, ClassNotFoundException {
        Folio f = new Folio("test", "bob");
        Stock a = new Stock("adam", "tex", 20);
        Stock b = new Stock("aidan", "x", 10);
        Stock c = new Stock("bob", "tsl", 30);
        Stock d = new Stock("steven", "ezj", 40);
        ArrayList<Folio> testList = new ArrayList<>();
        testList.add(f);
        Model.writeToFile(testList);
        testList.clear();

        HashMap<String, ArrayList<Folio>> testMap = new HashMap<>();
        testMap = Model.readFromFile();
        testList.add(testMap.get("bob").get(0));

        assertEquals("test",testList.get(0).getName());
    }


}

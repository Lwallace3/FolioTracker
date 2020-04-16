import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.HashMap;

public class Model implements Serializable {

    /**
     * Method to write all the folios from every user to a file using an object and file output stream.
     * Stored in an ArrayList of objects consisting of a compressed folio (name and username) followed by
     * all the stocks associated to that folio.
     *
     * @param folios array list of every folio.
     */
    public static void writeToFile(ArrayList<Folio> folios) {
        ArrayList<Object> objectList = new ArrayList<>();
        try {
            FileOutputStream fo = new FileOutputStream(new File("myFolio.txt"));
            ObjectOutputStream o = new ObjectOutputStream(fo);

            for (Folio folio : folios) {
                compressedFolio f = new compressedFolio(folio.getName(), folio.getOwnerOfFolioUserName());
                objectList.add(f);
                System.out.println("writing " + f.getName() + "to file");

                for (Stock s : folio.getStocks()) {
                    objectList.add(s);
                    System.out.println("storing stock: " + s.getShareName());
                }
                System.out.println("folio complete \n");
            }
            o.writeObject(objectList);
            o.close();
            fo.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the file "myFolio.txt" containing a serialized ArrayList of objects. Uses instanceOf to determine
     * whether the current object is a compressed folio or a stock. If it's a compressed folio it uses the information
     * to create a folio and then goes through and adds the following Stocks in the object ArrayList. Finally it stores
     * the folios in a hashMap to return.
     *
     * @return folioMap HashMap containing the username as the key and an ArrayList of all the folios associated to that
     *                  username
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws WebsiteDataException
     * @throws NoSuchTickerException
     */
    public static HashMap<String, ArrayList<Folio>> readFromFile() throws IOException, ClassNotFoundException, WebsiteDataException, NoSuchTickerException {
        Double overridePrice = 0.0;
        ArrayList<Object> loadedObjects = new ArrayList<>();
        ArrayList<Folio> loadedFolios = new ArrayList<>();
        compressedFolio folio;
        Folio currentFolio = null;

        try {
            FileInputStream fi = new FileInputStream(new File("myFolio.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            loadedObjects = (ArrayList<Object>) oi.readObject();

        } catch (EOFException | FileNotFoundException | NoSuchFileException e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("THERE ARE " + loadedObjects.size() + " OBJECTS IN THE LIST");
        for (Object obj : loadedObjects) {
            if (obj instanceof compressedFolio) {
                if (currentFolio != null) {
//                    currentFolio.refreshStocks();
                    loadedFolios.add(currentFolio);
                }
                folio = (compressedFolio) obj;
                System.out.println("READING: " + folio.getName());
                currentFolio = new Folio(folio.getName(), folio.getOwnerOfFolioUserName());
            } else if (obj instanceof Stock) {
                Stock s = (Stock) obj;
                System.out.println(s.getStockPrice());
                currentFolio.addStock(s);
                System.out.println("ADDING STOCK " + s.getShareName());
            }
        }
        if (currentFolio != null) {
//            currentFolio.refreshStocks();
            loadedFolios.add(currentFolio);
        }

        HashMap<String, ArrayList<Folio>> folioMap = new HashMap<>();

        for (Folio f: loadedFolios){
            String user = f.getOwnerOfFolioUserName();
            if (folioMap.containsKey(user)) {
                folioMap.get(user).add(f);
            } else {
                ArrayList<Folio> temp = new ArrayList<>();
                temp.add(f);
                folioMap.put(user,temp);
            }
        }

        return folioMap;
    }

    private static class compressedFolio implements Serializable {
        private String folioName;
        private String ownerOfFolioUserName;

        public compressedFolio(String folioName, String userName) {
            this.folioName = folioName;
            this.ownerOfFolioUserName = userName;
        }

        public String getName() {
            return folioName;
        }

        public String getOwnerOfFolioUserName() {
            return ownerOfFolioUserName;
        }
    }
}



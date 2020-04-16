import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.*;

/**
 * Sets up the main stage for the application
 * Creates and sets actions for the menu bar and its items
 */
public class View extends Application {

    Stage window;
    private boolean loggedIn = false;
    private TabPane tp = new TabPane();
    private ArrayList<Folio> folios = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;

        VBox display = new VBox(10);
        Menu menu = new Menu("File");
        MenuItem portFolio = new MenuItem("New PortFolio");
        MenuItem login = new MenuItem("Login");
        MenuItem save = new MenuItem("Save current portfolio");
        MenuItem load = new MenuItem("Load saved portfolio");
        MenuItem logOut = new MenuItem("Logout");

        if (!loggedIn) {
            save.setDisable(true);
            load.setDisable(true);
            logOut.setDisable(true);
        }
        /*
         *  Creates a new folio with the user's typed name
         *  Sets up the layout to be displayed to the user and displays it in a tab
         */
        portFolio.setOnAction(e -> {
            TextInputDialog setName = new TextInputDialog("");
            setName.setTitle("New Portfolio name");
            setName.setHeaderText("Please Enter the name for your new stock portfolio");
            setName.setContentText("Enter");
            Optional<String> result = setName.showAndWait();
            if (result.get().isEmpty()) {
                AlertBox.display("Error", "Your folio name must not be blank");
            } else {
                Folio folio = new Folio(result.orElse(null), login.getText());
                TableView<Stock> table = new TableView<>();
                Button closeButton = new Button("Close");
                Button refreshButton = new Button("Refresh");
                Button deleteSelectedRowButton = new Button("Delete row");
                VBox temp = buildScene(table, closeButton, refreshButton, deleteSelectedRowButton, folio, false);
                Tab tab = new Tab(result.get(), temp);
                tab.setOnCloseRequest(event -> folios.remove(folio));
                tp.getTabs().add(tab);
            }
        });
        /**
         * Allows the user to log in so they can save and load folios from disk
         * Checks the user name is not blank
         * Enables the load, save and logout buttons as well as disabling the login in button
         */
        login.setOnAction(e -> {
            TextInputDialog enterName = new TextInputDialog("");
            enterName.setHeaderText("Please Enter your user name: ");
            Optional<String> userName = enterName.showAndWait();
            if (userName.get().isEmpty()) {
                AlertBox.display("Error", "User name can't be blank");
            }
            else {
                loggedIn = true;
                save.setDisable(false);
                load.setDisable(false);
                logOut.setDisable(false);
                login.setDisable(true);
                login.setText(userName.get());
                for (Folio folio : folios) {
                    folio.setOwnerOfFolioUserName(userName.get());
                }
            }
        });

        save.setOnAction(e -> Model.writeToFile(folios));
        /*
         * Fetches all the folios for that user
         * Gets the appropriate number of layouts and displays each one in a separate tab
         * Displays if the current logged in user has no saved folios
         */
        load.setOnAction(e -> {
            try {
                HashMap<String, ArrayList<Folio>> f = Model.readFromFile();
                for (Map.Entry<String, ArrayList<Folio>> entry : f.entrySet()) {
                    System.out.println(entry.getKey());
                    String key = entry.getKey();
                    folios.addAll(f.get(key));
                }
                if (f.containsKey(login.getText())) {
                    for (int i = 0; i < f.get(login.getText()).size(); i++) {
                        TableView<Stock> table = new TableView<>();
                        Button closeButton = new Button("Close");
                        Button refreshButton = new Button("Refresh");
                        Button deleteSelectedRowButton = new Button("Delete row");
                        VBox temp = buildScene(table, closeButton, refreshButton, deleteSelectedRowButton, f.get(login.getText()).get(i), true);
                        Tab tab = new Tab(f.get(login.getText()).get(i).getName(), temp);
                        int finalI = i;
                        tab.setOnCloseRequest(event -> folios.remove(f.get(login.getText()).get(finalI)));
                        tp.getTabs().add(tab);
                    }
                } else {
                    AlertBox.display("Error", "This username does not have any saved portfolios");
                }
            }  catch(IOException | ClassNotFoundException | WebsiteDataException | NoSuchTickerException ex){
                ex.printStackTrace();
            }
        });
        /*
         * Enables the login button again, disable the logout, load and save buttons
         * Clears the screen ready to start from the beginning
         */
        logOut.setOnAction(e->{
            login.setDisable(false);
            login.setText("Login");
            save.setDisable(true);
            load.setDisable(true);
            logOut.setDisable(true);
            tp.getTabs().clear();
        });

        menu.getItems().addAll(portFolio, login, save, load, logOut);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(menu);

        display.getChildren().addAll(mb);
        display.getChildren().add(tp);
        Scene scene = new Scene(display, 1000, 675);

        window.setTitle("Stock Portfolio");
        window.setScene(scene);
        window.show();
    }

    /**
     * Constructs and populates a table object based on the folio passed in
     * The table is added to the layout returned
     * Buttons are constructed and functionality is added to them
     * @param table the table object to be edited
     * @param close button to be added to the layout
     * @param refresh button to be added to the layout
     * @param delete button to be added to the layout
     * @param folio The current folio that the information of the stocks need to be extracted from
     * @param load value stating whether the folio is being loaded from disk or is a new portfolio
     * @return VBox to be used
     */
    private VBox buildScene(TableView<Stock> table, Button close, Button refresh, Button delete, Folio folio, Boolean load) {

        VBox layout = new VBox(10);
        table.setMaxWidth(900);
        //Setting up the table object with the relevant columns and rows
        TableColumn<Stock, String> tickerSymbol = new TableColumn<>("Ticker Symbol");
        tickerSymbol.setMinWidth(180);
        tickerSymbol.setCellValueFactory(new PropertyValueFactory<>("tickerSymbol"));

        TableColumn<Stock, String> stockName = new TableColumn<>("Stock Name");
        stockName.setMinWidth(180);
        stockName.setCellValueFactory(new PropertyValueFactory<>("shareName"));

        TableColumn<Stock, Integer> numberOfShares = new TableColumn<>("Number of Shares");
        numberOfShares.setMinWidth(180);
        numberOfShares.setCellValueFactory(new PropertyValueFactory<>("stocksNumber"));

        TableColumn<Stock, Double> pricePerShare = new TableColumn<>("Price per Share");
        pricePerShare.setMinWidth(180);
        pricePerShare.setCellValueFactory(new PropertyValueFactory<>("stockPrice"));

        TableColumn<Stock, Double> valueOfHolding = new TableColumn<>("Value of Holding (£)");
        valueOfHolding.setMinWidth(180);
        valueOfHolding.setCellValueFactory(new PropertyValueFactory<>("valueHolding"));

        table.getColumns().addAll(tickerSymbol, stockName, numberOfShares, pricePerShare, valueOfHolding);

        /*
         * The folio is added to the list of folios if a load is not being performed
         *  Otherwise the list of stocks to put in the table is fetched and then the table is populated with this list
         */
        if (!load) {
            folios.add(folio);
        } else {
            ArrayList<Stock> savedStocks = folio.getStocks();
            for (Stock savedStock : savedStocks) {
                System.out.println(savedStock.getStockPrice());
                table.getItems().add(savedStock);
            }
        }

        // Textfields for adding shares to the folio
        TextField symbol = new TextField();
        TextField name = new TextField();
        TextField numShares = new TextField();
        //Labels to display to the user what each text field is
        Label sym = new Label("Ticker Symbol: ");
        Label sName = new Label("Share Name: ");
        Label shareNum = new Label("Number of Shares: ");
        Button addButton = new Button("Add");

        HBox hBox = new HBox(10);
        VBox vBox = new VBox(10);
        HBox buttons = new HBox(10);

        hBox.getChildren().addAll(sym, symbol, sName, name, shareNum, numShares, addButton);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(8, 0, 0, 0));

        Label folioValue = new Label("Total folio value is: £" + folio.getFolioValue());
        folioValue.setPadding(new Insets(10, 0, 5, 0));

        /*
         * Deletes the current selected row and removes the selected stock from the portfolio
         * The folio value is then updated
         */
        delete.setOnAction(event -> {
            Stock selectedItem = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectedItem);
            folio.removeStock(selectedItem);
            folioValue.setText("Total folio value is: £" + folio.getFolioValue());
        });

        buttons.getChildren().addAll(close, refresh, delete);
        buttons.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox, table, folioValue, buttons);
        vBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(vBox);

        /*
         * Allows the user to add a stock to their current folio
         * Error checks that none of the fields are blank as well as the share number number being greater than 0
         * Doesn't allow for stocks with the same name to be added to the folio
         */
        addButton.setOnAction(event -> {
            if (name.getText().isEmpty() || symbol.getText().isEmpty() || numShares.getText().isEmpty()) {
                AlertBox.display("Error", "Please make sure none of the fields are blank");
            } else {
                try {
                    boolean error = false;
                    ArrayList<Stock> stocks;
                    stocks = folio.getStocks();
                    for (int index = 0; index < stocks.size(); index++) {
                        if (stocks.get(index).getShareName().contains(name.getText().toUpperCase())) {
                            error = true;
                        }
                    }
                    if (error) {
                        AlertBox.display("Error", "That share name already exists in this folio");
                    } else {
                        Stock stock = new Stock(name.getText().toUpperCase(), symbol.getText().toUpperCase(), Integer.parseInt(numShares.getText()));
                        folio.addStock(stock);
                        folioValue.setText("Total folio value is: £" + folio.getFolioValue());
                        table.getItems().add(stock);
                    }
                } catch (NoSuchTickerException ex) {
                    AlertBox.display("Error", "Please enter a valid stock symbol");
                } catch (WebsiteDataException ex) {
                    AlertBox.display("Error", "Sorry something has went wrong");
                } catch (NumberFormatException ex) {
                    AlertBox.display("Error", "Please enter a numerical integer value for the number of shares");
                }
            }
        });

        close.setOnAction(e -> window.close());

        /*
         * Highlights whether the stock has went up, down or hasn't changed
         * This is done by colouring the row of the relevant stock the appropriate colour green for up
         * Red for down and grey for no change
         */
        refresh.setOnAction(e -> {
            table.setRowFactory(row-> new TableRow<>(){
                @Override
                protected void updateItem(Stock stock, boolean b) {
                    super.updateItem(stock, b);
                    if(stock==null || b){
                        setStyle("");
                    }else{
                        if(stock.getGainLoss()<0.0){
                            for(int i=0;i<getChildren().size();i++){
                                ((Labeled)getChildren().get(i)).setTextFill(Color.BLACK);
                                getChildren().get(i).setStyle("-fx-background-color: red");
                            }
                        }else if(stock.getGainLoss()>0.0){
                            for(int i=0;i<getChildren().size();i++){
                                ((Labeled)getChildren().get(i)).setTextFill(Color.BLACK);
                                getChildren().get(i).setStyle("-fx-background-color: green");
                            }
                        }else if(stock.getGainLoss()==0.0){
                            for(int i=0;i<getChildren().size();i++){
                                ((Labeled)getChildren().get(i)).setTextFill(Color.BLACK);
                                getChildren().get(i).setStyle("-fx-background-color: gray");
                            }
                        }
                        else {
                            if(getTableView().getSelectionModel().getSelectedItems().contains(stock)){
                                for(int i=0;i<getChildren().size();i++){
                                    ((Labeled)getChildren().get(i)).setTextFill(Color.BLACK);
                                    getChildren().get(i).setStyle("-fx-background-color: lightblue");
                                }
                            }
                            else{
                               for(int i=0;i<getChildren().size();i++){
                                   ((Labeled)getChildren().get(i)).setTextFill(Color.BLACK);
                                   getChildren().get(i).setStyle("-fx-background-color: white");
                            }
                        }
                    }
                }
            }
            });
            table.refresh();
        });

        /*
         * Enables current selected stock to be edited when a row is double clicked in the table
         * In this case a pop-up window appears for the user to enter new information about the selected stock
         */
        table.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount()==2){
                Stock selectedStock= table.getSelectionModel().getSelectedItem();
                editRow(folio,selectedStock,table,folioValue);
            }
        });
        return layout;
    }

    /**
     * Creates and Displays a pop-up window
     * The window allows the user to edit the current selected stock in their folio
     *
     * @param folio Folio of user to be edited
     * @param stock Current selected stock to be edited
     * @param table The table object in which the row will be edited
     * @param updateValue The label to display the updated folio value
     */
    private void editRow(Folio folio, Stock stock, TableView<Stock> table, Label updateValue){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Edit Stock '" + stock.getTickerSymbol() + "' for " + folio.getName());
        window.setMinWidth(350);

        Label title = new Label("Editing Portfolio: " + folio.getName());
        Label tickerSymbol = new Label(stock.getTickerSymbol());
        Label stockName = new Label(stock.getShareName());
        Label currentValue = new Label("Current Value: " + stock.getStockPrice());
        Label currentShareNumber = new Label("Current No. of shares: "+ stock.getStocksNumber());
        Label numberOfShares = new Label("Number of Shares");
        Label initialValue = new Label("Initial Value");
        Label totalGain = new Label("Total Gain : " + stock.getGainLoss());

        TextField inputNumberOfShares = new TextField();
        TextField inputInitialValue = new TextField();

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        Button delete = new Button("Delete");
        save.setAlignment(Pos.CENTER);
        cancel.setAlignment(Pos.CENTER);
        delete.setAlignment(Pos.CENTER);

        GridPane editWindow = new GridPane();
        HBox editButtons = new HBox(15);

        /*
         * Adding all the labels, buttons and textfields
         * Then setting the positions of the items
         */
        editWindow.setVgap(4);
        editWindow.add(title,1,1);
        editWindow.add(tickerSymbol,1,2);
        editWindow.add(stockName,2,2);
        editWindow.add(currentValue,1,3);
        editWindow.add(currentShareNumber,2,3);
        editWindow.add(numberOfShares,1,4);
        editWindow.add(inputNumberOfShares,2,4);
        editWindow.add(initialValue,1,5);
        editWindow.add(inputInitialValue,2,5);
        editWindow.add(totalGain,1,6);
        editButtons.getChildren().addAll(save,cancel,delete);
        editWindow.add(editButtons,1,7);
        editWindow.setAlignment(Pos.TOP_CENTER);

        if (stock.getGainLoss() > 0.0) {
            totalGain.setTextFill(Color.GREEN);
        } else if(stock.getGainLoss() <0.0){
            totalGain.setTextFill(Color.RED);
        }else{
            totalGain.setTextFill(Color.GRAY);
        }

        /*
         * Saves the user selected information and updates the table object to display this
         * Checks to see if the fields are left blank and are valid before the table is updated
         */
        save.setOnAction(e->{
            if (inputNumberOfShares.getText().isEmpty()|| inputInitialValue.getText().isEmpty()){
                AlertBox.display("Error","Number of shares or Initial value must not be blank");
            }
            if(Integer.parseInt(inputNumberOfShares.getText())<0 || Double.parseDouble(inputInitialValue.getText())<0.0){
                AlertBox.display("Error","Number of shares and initial value must be greater than 0");
            }
            else {
                try{
                  stock.setStocksNumber(Integer.parseInt(inputNumberOfShares.getText()));
                  stock.setStockPrice(Double.parseDouble(inputInitialValue.getText()));
                  table.refresh();
                  updateValue.setText("Total folio value is: £" + folio.getFolioValue());
                  window.close();
                }catch (NumberFormatException E){
                    AlertBox.display("Error", "Please enter a numerical integer value for the number of shares\n Please enter a valid double for the initial value of the stock");
                }
            }
        });
        //Closes editing window
        cancel.setOnAction(e-> window.close());
        /*
         * Deletes the currently being edited row from the currently selected folio
         * The folio value is then updated and the window is closed
         */
        delete.setOnAction(e->{
            table.getItems().remove(stock);
            folio.removeStock(stock);
            updateValue.setText("Total folio value is: £" + folio.getFolioValue());
            window.close();
        });
        Scene scene = new Scene(editWindow,350,200);
        window.setScene(scene);
        window.showAndWait();
    }
}
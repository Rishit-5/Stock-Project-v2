package StockP;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    @FXML
    private ListView lstV;
    private ArrayList<Person> myPeople = new ArrayList<>();
    private ArrayList<Stock> allStocks = new ArrayList<>();
    private ArrayList<ArrayList<Double>> stockams = new ArrayList<>();
    private boolean sopen;
    private int playerChosen = -3;
    private int timesreq = 1;
    @FXML
    private ComboBox cbox;
    @FXML
    private Label lbl1, lbl2, sprice, spurchase, lblInst;
    @FXML
    private TextField txtF;
    @FXML
    private LineChart lChart;
    @FXML
    private NumberAxis xaxis, yaxis;
    @FXML
    private TextArea txtA;
    @FXML
    private ImageView imgV;
    private Image a = new Image("resources/edown.png");
    private Image b = new Image("resources/eup.gif");
    private ArrayList<ArrayList<Double>> prices = new ArrayList<>();
    private ArrayList<ArrayList<Double>> chartArray = new ArrayList<>();
    private boolean api = false;
    private boolean chosen = false;
    private int ammonths = 0;

    @FXML
    //precondition: variables have been created
    //postcondition: api will be signifified as true, and the game will start with an api being loaded in
    private void hist() {
        if (!chosen) {//is used to prevent user from clicking the mode multiple times
            chosen = true;
            lblInst.setText("Wait for players to show up in ListView, then click one");
            handleStart();
            api = true;
            handlebtn1();
        }
    }

    @FXML
    //precondition: variables have been created
    //postcondition: api will be signifified as false, and the game will start
    private void fut() {
        if (!chosen) {
            handleStart();
            api = false;
            chosen = true;
            lblInst.setText("Wait for players to show up in ListView, then click one");
        }
    }

    //precondition: mode has been clicked as api true
    //postcondition: A reference to an api will be created and and array list with all the stock data since 2014 will also be created
    private void handlebtn1() {
        for (int i = 0; i < allStocks.size(); i++) {
            prices.add(new ArrayList<>());//creates a new row in prices
            chartArray.add(new ArrayList<>());
            String url1 = "https://www.nasdaq.com/api/v1/historical/" + allStocks.get(i).getName() + "/stocks/2014-12-08/2019-12-08";
            URL url = null;
            try {
                url = new URL(url1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //"https://www.nasdaq.com/api/v1/historical/AAPL/stocks/2014-12-08/2019-12-08"
            try {
                URL nasdaq = new URL(url.toString());
                URLConnection data = nasdaq.openConnection();
                Scanner input = new Scanner(data.getInputStream());
                if (input.hasNext()) {
                    input.nextLine();
                }
                while (input.hasNextLine()) {
                    String line = input.nextLine();
                    prices.get(i).add(Double.parseDouble(line.substring(line.indexOf("$") + 1, line.indexOf(",", line.indexOf("$")))));
                }
                System.out.println(prices);
            } catch (Exception e) {
                System.err.println(e);
            }
            allStocks.get(i).choosePrice2(prices.get(i).get(prices.get(i).size() - 1));
        }
    }

    //precondition: a mode has been selected
    //postcondition: players will be added, stock will be added through a file reader, and list view and combo box will be set up with what's needed
    private void handleStart() {
        myPeople.add(new Person("Bob", 100000.0));//each player starts out with 100
        myPeople.add(new Person("Sally", 100000.0));
        myPeople.add(new Person("Tyrone", 100000.0));
        myPeople.add(new Person("Sean", 100000.0));

//
//    allStocks.add(new Stock("shs",5.0));
        try {
            FileReader reader = new FileReader("src/resources/stocks");
            Scanner in = new Scanner(reader);
            while (in.hasNextLine()) {
                //temp.substring(0, temp.indexOf("-") - 1)
                //temp.substring(temp.indexOf("$")+1)
                String temp = in.nextLine();
                allStocks.add(new Stock(temp.substring(0, temp.indexOf("-") - 1), Double.parseDouble(temp.substring(temp.indexOf("$") + 1))));
                stockams.add(new ArrayList<>());
            }
            for (int i = 0; i < myPeople.size(); i++) {
                lstV.getItems().add(myPeople.get(i).getName());
            }
            for (int i = 0; i < allStocks.size(); i++) {
                cbox.getItems().add(allStocks.get(i).getName());
            }
            for (int i = 0; i < stockams.size(); i++) {
                stockams.get(i).add(allStocks.get(i).getPrice());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("SOMETHING HAS GONE HORRIBLY WRONG WE'RE ALL GONNA DIE!");//just kept it cause why not
        }

    }

    //precondition: game has already been started with a mode being created
    //postcondition: A new player will be created and the ListView will be updated
    @FXML
    private void mPlayer() {
        if (myPeople.size() > 0) {//if people have been made already
            if (!sopen) {//if stocks aren't in list view
                String p = "player";
                p += (int) (Math.random() * 1000 + 1);
                myPeople.add(new Person(p, 100000.0));
                lstV.getItems().clear();
                for (int i = 0; i < myPeople.size(); i++) {
                    lstV.getItems().add(myPeople.get(i).getName());
                }
            }
        }
    }

    //precondition: game has been started
    //postcondition: will clear list view and put all the players in the list view
    @FXML
    private void handleSP() {
        lstV.getItems().clear();
        for (int i = 0; i < myPeople.size(); i++) {//adds people in the list view
            lstV.getItems().add(myPeople.get(i).getName());
        }
        sopen = false;
    }

    @FXML
    //precondition: game has been started and there ae players
    ///postcondition: clicking on a player will show their portfolio
    private void handlelstV() {
        try {
            if (!sopen) {//makes sure stocs arent open in the list view
                lblInst.setText("Choose a stock, choose an amount, then click buy or sell");
                playerChosen = lstV.getSelectionModel().getSelectedIndex();
                lstV.getItems().clear();
                if (myPeople.get(playerChosen).getStock().size() > 0) {//if user has more than one stock
                    for (int i = 0; i < myPeople.get(playerChosen).getStock().size(); i++) {// puts the number of shares into a list view
                        lstV.getItems().add(myPeople.get(playerChosen).getStock().get(i).getNumShares() + " shares of " + myPeople.get(playerChosen).getStock().get(i).getName());
                    }
                }
                lbl1.setText("Purchasing Power: " + String.valueOf(myPeople.get(playerChosen).getMoney()));
                lbl2.setText("Value: " + String.valueOf(myPeople.get(playerChosen).getValue()));


                sopen = true;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            lstV.getItems().clear();
            for (int i = 0; i < myPeople.size(); i++) {//puts the names of players in the list view
                lstV.getItems().add(myPeople.get(i).getName());
            }
        }

    }

    //precondition: A stock has been selected in the combo box and and amount has been indicated and the user has money left over to spend
    //postcondition: clicking buy will pass a parameter conataining name, shares, and the stock into the Person class indicating that the person has bought shares
    @FXML
    private void handleBuy() {
        try {
            if (cbox.getValue() != null && playerChosen != -3 && Integer.parseInt(txtF.getText()) > 0) {// makes sure values are in a combobox,players have been chosen, and the amount they are trying to buy is an integer
                int ams = 1;
                lblInst.setText("Click Next Month to simulate the next month");
                if (!txtF.getText().equalsIgnoreCase("")) {//if there's a value in combo box
                    ams = Integer.parseInt(txtF.getText());
                }
                try {
                    Stock s = null;
                    for (int i = 0; i < allStocks.size(); i++) {//looks if combo box is in the allstocks array
                        if (cbox.getValue().equals(allStocks.get(i).getName())) {
                            s = allStocks.get(i);
                        }
                    }

                    if (myPeople.get(playerChosen).canBuy(ams, s)) {//if the user can buy a stock
                        if (playerChosen != -3) {//if there's a value of playerchosen
                            myPeople.get(playerChosen).buy(s.getName(), ams, s);
                            lstV.getItems().clear();
                            for (int i = 0; i < myPeople.get(playerChosen).getStock().size(); i++) {//adds players to list view
                                lstV.getItems().add((myPeople.get(playerChosen).getStock().get(i).getNumShares() + " shares of " + myPeople.get(playerChosen).getStock().get(i).getName()));
                            }
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("You didn't select a company to buy");
                }
                lbl1.setText("Purchasing Power: " + myPeople.get(playerChosen).getMoney());
                for (int i = 0; i < myPeople.size(); i++) {//updates the value of people's portfolios
                    myPeople.get(i).changeValue(allStocks);
                }
                lbl2.setText("Value: " + myPeople.get(playerChosen).getValue());
                int temp = cbox.getSelectionModel().getSelectedIndex();
                spurchase.setText("Purchasable Shares: " + (int) ((myPeople.get(playerChosen).getMoney()) / allStocks.get(temp).getPrice()));
            }
        } catch (NumberFormatException ex) {
            System.out.println("That's not an Integer!");
        }
    }

    @FXML
    //precondition: A stock has been selected in the combo box and and amount has been indicated and they currently have shares of that stock
    //postcondition: Parameters containing shares and stock name will be passed into the Person class indicating the user wants to sell hares
    private void handleSell() {
        try {
            if (cbox.getValue() != null && playerChosen != -3 && Integer.parseInt(txtF.getText()) > 0) {
                int ams = 1;
                lblInst.setText("Click Next Month to simulate the next month");
                if (!txtF.getText().equalsIgnoreCase("")) {
                    ams = Integer.parseInt(txtF.getText());
                }
                try {
                    Stock s = null;
                    for (int i = 0; i < allStocks.size(); i++) {//searches all stocks for if stock name is equal to combo box name
                        if (cbox.getValue().equals(allStocks.get(i).getName())) {
                            s = allStocks.get(i);
                        }
                    }

                    if (myPeople.get(playerChosen).doesOwn(s.getName(), ams)) {//checks if the players actually own the shares to sell
                        if (playerChosen != -3) {
                            myPeople.get(playerChosen).sell(s.getName(), ams, s);//sells the stock based on player chosen
                            lstV.getItems().clear();
                            for (int i = 0; i < myPeople.get(playerChosen).getStock().size(); i++) {//goes through the stocks that the player has
                                lstV.getItems().add((myPeople.get(playerChosen).getStock().get(i).getNumShares() + " shares of " + myPeople.get(playerChosen).getStock().get(i).getName()));
                            }
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("You didn't select a company to sell");
                }
                lbl1.setText("Purchasing Power: " + myPeople.get(playerChosen).getMoney());
                for (int i = 0; i < myPeople.size(); i++) {////updates the value of players
                    myPeople.get(i).changeValue(allStocks);
                }
                lbl2.setText("Value: " + myPeople.get(playerChosen).getValue());
                int temp = cbox.getSelectionModel().getSelectedIndex();
                spurchase.setText("Purchasable Shares: " + (int) ((myPeople.get(playerChosen).getMoney()) / allStocks.get(temp).getPrice()));
            }
        } catch (NumberFormatException ex) {
            System.out.println("That's not an Integer!");
        }

    }

    @FXML
    //precondition: a player has been chosen and whether an api was used is signified
    //postcondition: The next month is simulated and each day the price changes, price is then plotted on the graph using an instance of the graph method
    private void nDay() {
        int temp = cbox.getSelectionModel().getSelectedIndex();
        if (playerChosen != -3 && temp!=-1 ) {//if playerchosen and temp have values
            if (!api) {//if api mode false
                ammonths++;
                if (ammonths%3==0){
                    events3(stockams.get(0).size());
                }
                for (int j = 0; j < 30; j++) {//simulates next 30 days
                    int randMarket = (int) (Math.random() * 1000) + 1;//30/1000 chance of stock market event
                    if (randMarket==1){
                        events2(stockams.get(0).size(),randMarket);
                    }
                    for (int i = 0; i < allStocks.size(); i++) {//goes through all stocks
                        int rand = (int) (Math.random() * 2500) + 1;//270/2500 chance that an event occurs on a click
                        allStocks.get(i).priceChange();//changes price of stock
                        if (rand == 5) {//number 5 chosen for an event occurring
                            events(i, stockams.get(i).size(), randMarket, rand);
                            System.out.println(j + allStocks.get(i).getName());
                        }
                        stockams.get(i).add(allStocks.get(i).getPrice());
                    }
                    for (int i = 0; i < myPeople.size(); i++) {
                        myPeople.get(i).changeValue(allStocks);//updates user's value
                    }
//                System.out.println(stockams);
                    lbl2.setText("Value: " + myPeople.get(playerChosen).getValue());
                }
                mChart();
                sprice.setText("Current Price: " + stockams.get(temp).get(stockams.get(temp).size() - 1));
            } else {//api is on
                for (int i = 0; i < allStocks.size(); i++) {
                    for (int j = prices.get(i).size() - 1 - (30 * timesreq); j >= prices.get(i).size() - 31 - (30 * timesreq); j--) {//moves the values added to the array list to later values in the prices arraylist
                        try {
                            chartArray.get(i).add(prices.get(i).get(j));
                            allStocks.get(i).choosePrice(chartArray.get(i));
                        } catch (ArrayIndexOutOfBoundsException ignored) {

                        }
                    }
                }
                for (int k = 0; k < myPeople.size(); k++) {//updates player values
                    myPeople.get(k).changeValue(allStocks);
                }
                lbl2.setText("Value: " + myPeople.get(playerChosen).getValue());
                timesreq++;
                mChart();

            }
        }
    }

    @FXML
    //precondition: 30 days have been simulated and a selection has been made in the combo box
    //postcondition: a graph will be made based on the data collected after clicking next month
    private void mChart() {
        int temp = cbox.getSelectionModel().getSelectedIndex();

        if (temp != -1 && playerChosen != -3) {
            spurchase.setText("Purchasable Shares: " + (int) ((myPeople.get(playerChosen).getMoney()) / allStocks.get(temp).getPrice()));
            lChart.getData().clear();
            lChart.setTitle((String) cbox.getItems().get(temp));
            XYChart.Series series = new XYChart.Series();
            series.setName("Stock Price");
//            series.getNode().setStyle("-fx-stroke: red;");
            series.getData().clear();
            if (!api) {//if the api is turned off
                for (int i = 0; i < stockams.get(0).size(); i++) {
                    series.getData().add(new XYChart.Data(i, stockams.get(temp).get(i)));
                }
                lChart.getData().add(series);
                if (stockams.get(temp).size() > 30) {//if there's more than 30 values in the arraylist
                    if (stockams.get(temp).get(stockams.get(temp).size() - 1) - stockams.get(temp).get(stockams.get(temp).size() - 30) > 0) {
                        series.getNode().setStyle("-fx-stroke: green;" + "    -fx-stroke-width: 1px;\n");
                    } else {
                        series.getNode().setStyle("-fx-stroke: red;" + "    -fx-stroke-width: 1px;\n");
                    }
                }
            } else {
                for (int i = 0; i < chartArray.get(0).size(); i++) {//goes through the array for the chart values
                    series.getData().add(new XYChart.Data(i, chartArray.get(temp).get(i)));
                }
                lChart.getData().add(series);
                if (chartArray.get(temp).size() > 30) {//if the arraylist has more than 30 values
                    if (chartArray.get(temp).get(chartArray.get(temp).size() - 1) - chartArray.get(temp).get(chartArray.get(temp).size() - 30) > 0) {
                        series.getNode().setStyle("-fx-stroke: green;" + "    -fx-stroke-width: 1px;\n");
                    } else {
                        series.getNode().setStyle("-fx-stroke: red;" + "    -fx-stroke-width: 1px;\n");
                    }
                }
            }
            lChart.setCreateSymbols(false);
            if (!api) {
                yaxis.setAutoRanging(false);
                double min = 100000000;
                for (int i = 0; i < stockams.get(temp).size(); i++) {
                    if (stockams.get(temp).get(i) < min) {
                        min = stockams.get(temp).get(i);
                    }
                }
                double max = 0;
                for (int i = 0; i < stockams.get(temp).size(); i++) {
                    if (stockams.get(temp).get(i) > max) {
                        max = stockams.get(temp).get(i);
                    }
                }
                yaxis.setLowerBound(min);
                yaxis.setUpperBound(max);
                sprice.setText("Current Price: " + stockams.get(temp).get(stockams.get(temp).size() - 1));
            } else {
                sprice.setText("Current Price: " + allStocks.get(temp).getPrice());
            }

        }
    }

    //precondition: 30 days have been simulated, and by chance, an event has occurred and an instance of the class caused the method to run
    //postcondition: either a good or bad event will be chosen and an instance of the person class has been made, an image view will be update with a picture indicating fortune or misfortune
    public void events(int i, int asize, int randMarket, int r) {
        int rand = (int) (Math.random() * 2 + 1);//for individual event
        int rand3 = (int) (Math.random() * 2 + 1);//for stock market event
            allStocks.get(i).eventPrice(rand);
            if (rand == 1) {//bad event
                int rand2 = (int) (Math.random() * 3);
                imgV.setImage(a);
                String[] badevents = {" suffered an earthquake today, with the headquarters being completely smashed ", "'s CEO unfortunately died in a car crash today ", " suffers as lawsuit causes them to lose millions "};
                if (txtA.getText().length() > 0) {//if there's more than one value in the text area
                    txtA.setText(allStocks.get(i).getName() + badevents[rand2] + "on day " + asize + "\n" + "-----------------------------------" + "\n" + txtA.getText());
                } else {//if there's nothing else in the text area
                    txtA.setText(allStocks.get(i).getName() + badevents[rand2] + "on day " + asize + txtA.getText());
                }
            } else if (rand == 2) {//good event
                int rand2 = (int) (Math.random() * 3);
                imgV.setImage(b);
                String[] goodevents = {" made a giant scientific breakthrough, revolutionizing the market ", " released a new product that received incredible reviews ", " had a leprechaun visit their headquarters "};
                if (txtA.getText().length() > 0) {
                    txtA.setText(allStocks.get(i).getName() + goodevents[rand2] + "on day " + asize + "\n" + "-----------------------------------" + "\n" + txtA.getText());
                } else {
                    txtA.setText(allStocks.get(i).getName() + goodevents[rand2] + "on day " + asize + txtA.getText());
                }

            }

    }
    private void events2(int asize, int randMarket){
        int rand3 = (int) (Math.random() * 2 + 1);//for stock market event
        if (randMarket == 1) {
            if (rand3 == 1) {
                imgV.setImage(a);//bad market event
                for (int j = 0; j < allStocks.size(); j++) {
                    allStocks.get(j).eventPrice(rand3);
                }
                int rand2 = (int) (Math.random() * 3);
                String[] badevents = {" The US had tariffs placed ", " The Stock Market unfortunately crashed ", " The United States was defeated in a war "};
                if (txtA.getText().length() > 0) {
                    txtA.setText(badevents[rand2] + "on day " + asize + "\n" + "-----------------------------------" + "\n" + txtA.getText());
                } else {
                    txtA.setText(badevents[rand2] + "on day " + asize);
                }
            } else {
                imgV.setImage(b);
                for (int j = 0; j < allStocks.size(); j++) {
                    allStocks.get(j).eventPrice(rand3);
                }
                int rand2 = (int) (Math.random() * 3);
                String[] goodevents = {" The US lifted sanctions with China ", " Donald Trump completely removed taxes ", " Aliens provided all the necessary features to develop the human race "};
                if (txtA.getText().length() > 0) {
                    txtA.setText(goodevents[rand2] + "on day " + asize + "\n" + "-----------------------------------" + "\n" + txtA.getText());
                } else {
                    txtA.setText(goodevents[rand2] + "on day " + asize);
                }
            }
        }
    }
    private void events3(int asize){
        for (int i = 0; i < allStocks.size(); i++) {
            int rand3 = (int) (Math.random() * 2 + 1);//for interval event
            if (rand3 == 1) {//bad market event
                allStocks.get(i).eventPrice2(rand3);
                if (txtA.getText().length() > 0) {//checks if there's values in the text area so it can add dashes
                    txtA.setText(allStocks.get(i).getName() + " had earnings that were down 30% from last quarter " + "on day " + asize + "\n" + "-----------------------------------" + "\n" + txtA.getText());
                } else {
                    txtA.setText(allStocks.get(i).getName() +" had earnings that were down 30% from last quarter " + "on day " + asize);
                }
            } else {
                allStocks.get(i).eventPrice2(rand3);
                if (txtA.getText().length() > 0) {//checks if there's values in the text area so it can add dashes
                    txtA.setText(allStocks.get(i).getName() +" had earnings that were up 20% from last quarter " + "on day " + asize + "\n" + "-----------------------------------" + "\n" + txtA.getText());
                } else {
                    txtA.setText(allStocks.get(i).getName() +" had earnings that were up 20% from last quarter " + "on day " + asize);
                }
            }
        }

    }
}

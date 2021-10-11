package StockP;

import java.util.ArrayList;

public class Person {
    private String name;
    private Double money, value;
    private ArrayList<PersonStocks> myStocks = new ArrayList<>();

    public Person(String n, Double m){
        name = n;
        money = m;
        value = m;
    }
    //precondition: user has clicked buy
    //postcondition: buying will lead to a person's money going down, and the amount of shares of that stock(Person Stocks) to go up
    public void buy(String n,int shares, Stock s){
        boolean own = false;
        for (PersonStocks x :myStocks) {//if the name is a stock that exists
            if(n.equals(x.getName())) {
                x.addShares(shares);
                money-= (s.getPrice()*shares);
                own = true;
            }
        }
        if (!own){//makes the user own it now
            myStocks.add(new PersonStocks(n,shares));
            money-= (s.getPrice()*shares);

        }

    }
    //precondition: Controller requested person name
    //postcondition: name will be returned
    public String getName(){
        return name;
    }

//precondition: Controller requested what stocks person owns
    //postcondition: returns stocks owned
    public ArrayList<PersonStocks> getStock(){
        return myStocks;
    }
    public double getMoney(){
        money*=100;
        money = Math.floor(money);
        money/=100;//returns as hundredths place
        return money;
    }
    //precondition: Controller wants to check if buying is possible
    //checks if price per share time share subtracted from money is still greater than 0
    public boolean canBuy(int shares, Stock s) {
        boolean can = false;
        if (money-(shares*s.getPrice())>0){//checks if user can afford it
            can = true;
        }
        return can;
    }
    //precondition: Controller indicated it wants to check if the user has the hares to sell
    //returns whether user owns the shares
    public boolean doesOwn(String n, int shares){
        boolean own = false;
        for (PersonStocks x :myStocks) {//verifies if the shares are in person stocks
            if(n.equals(x.getName()) && (x.getNumShares()- shares) >= 0) {
                own = true;
            }
        }
        return own;
    }
    //precondition: user has indicated they want to sell and does own verifies they can sell
    //money goes up and shares of the stock are removed
    public void sell(String n,int shares, Stock s){
        for (PersonStocks x :myStocks) {
            if(n.equals(x.getName())) {
                x.addShares(0-shares);
                money+= (s.getPrice()*shares);
            }
        }
        for (int i = 0; i < myStocks.size(); i++) {
            if (myStocks.get(i).getNumShares()<=0){
                myStocks.remove(i);//if they now have 0 and now removes it from person stocks
            }
        }

    }
    //precondition: Controller requests player value
    //postcondition: returns value but rounded to the hundredths place
    public double getValue(){
        value*=100;
        value = Math.floor(value);
        value/=100;//returns to hundredths place
        return value;
    }
    //precondition: ArrayList for stocks has been passed as a parameter
    //postcondition: value of player is changed by adding up the value of each stock plus the money remaining to buy stuff
    public void changeValue(ArrayList<Stock> ss){
        value = money;//sets as purchasing power
        for (int i = 0; i < ss.size(); i++) {//adds values of the stocks
            for (int j = 0; j < myStocks.size(); j++) {
                if (myStocks.get(j).getName().equalsIgnoreCase(ss.get(i).getName())){
                    value += myStocks.get(j).getNumShares() * ss.get(i).getPrice();

                }
            }
        }
    }
}

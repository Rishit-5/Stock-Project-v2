package StockP;

public class PersonStocks {
    private String name;
    private int numShares;
    public PersonStocks(String n,int ns){
        name = n;
        numShares = ns;
    }
    public int getNumShares() {
        return numShares;
    }
    public void addShares(double s){
        numShares +=s;
    }


    public String getName() {
        return name;
    }
}

package StockP;

import java.util.ArrayList;

public class Stock {
    private String name;
    private double price;

    public Stock(String n, double p){
        name = n;
        price = p;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
    //precondition: Price of the stock has been requested to change
    //postcondition: price of the stock changes anywhere from -8 to 8%, with more extreme values being rarer
    public void priceChange(){
        int[] ams = {-8,-5,-3,-1,1,3,5,8};
        int rand = (int)(Math.random()*(100)+1);
        if (rand<=5){//5% chance it changes from -8% to -5%
            rand = 0;
        }
        else if (rand<=15){//10% chance that it changes from -5% to -3%
            rand = 1;
        }
        else if (rand <=30){//15% chance that it changes from -3% to -1%
            rand = 2;
        }
        else if (rand <=70){//40% chance it changes from -1 to 1%
            rand = 3;
        }
        else if (rand <= 85){//15% chance that it changes from 1% to 3%
            rand = 4;
        }
        else if (rand <=95){//10% chance that it changes from 3% to 5%
            rand = 5;
        }
        else{//5% chance that it changes from 5% to 8%
            rand = 6;
        }
        int lowBound = ams[rand];
        int upBound = ams[rand+1];
        double am = ((Math.random() * (upBound-lowBound))+lowBound) / 100;
        price+=price*am;
        price*=100;
        price = Math.floor(price);
        price/=100;
//        System.out.println(price + " " + (am*100));
    }
    //precondition: integer indicating whether stock goes up or down has been passed as a parameter
    //postcondition: stock price changes anywhere from -50 to 25% or 25 to 50%
    public void eventPrice(int r){
        int[] ams = {-25,-16,16,25};
        if (r==1){//50% chance between -25 and -16
            r = 0;
        }
        else if (r==2) {//50% chance between 16 and 25
            r = 2;
        }
        int lowBound = ams[r];
        int upBound = ams[r+1];
        double am = ((Math.random() * (upBound-lowBound))+lowBound) / 100;
        price+=price*am;
        price*=100;
        price = Math.floor(price);
        price/=100;
    }

    public void choosePrice(ArrayList<Double> temp5){
        price = temp5.get(temp5.size()-1);//makes the current price the last price of the array list
        price*=100;
        price = Math.floor(price);
        price/=100;
    }
    public void choosePrice2(double num){
        price = num;
    }

    public void eventPrice2(int r) {
        int[] ams = {-16,-8,8,16};
        if (r==1){//50% chance between -25 and -16
            r = 0;
        }
        else if (r==2) {//50% chance between 16 and 25
            r = 2;
        }
        int lowBound = ams[r];
        int upBound = ams[r+1];
        double am = ((Math.random() * (upBound-lowBound))+lowBound) / 100;
        price+=price*am;
        price*=100;
        price = Math.floor(price);
        price/=100;
    }
}

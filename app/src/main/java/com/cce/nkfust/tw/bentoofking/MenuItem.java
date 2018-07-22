package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/7/22.
 */

public class MenuItem {
    String Name,Price;
    public MenuItem(){}
    public MenuItem(String Name,String Price){
        this.Name = Name;
        this.Price = Price;
    }
    void putName(String Name){this.Name = Name;}
    void putPrice(String Price){this.Price = Price;}
    String getName(){return Name;}
    String getPrice(){return Price;}
}

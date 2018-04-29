package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/28.
 */

public class Meal implements Serializable {
    private String ID,Store,Name,Price,Sequence;
    public Meal(){};
    public Meal(String ID,String Store,String Name,String Price,String Sequence){
        this.ID = ID;
        this.Store = Store;
        this.Name = Name;
        this.Price = Price;
        this.Sequence = Sequence;
    };
    public String getID(){return ID;}
    public String getStore(){return Store;}
    public String getName(){return Name;}
    public String getPrice(){return Price;}
    public String getSequence(){return Sequence;}
    public void putID(String ID){this.ID = ID;}
    public void putStore(String Store){this.Store = Store;}
    public void putName(String Name){this.Name = Name;}
    public void putPrice(String Price){this.Price = Price;}
    public void putSequence(String Sequence){this.Sequence = Sequence;}
}

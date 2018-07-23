package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/7/23.
 */

public class OrderMenuItem implements Serializable {
    String Name,Price,Count,ID;
    public OrderMenuItem(){}
    public OrderMenuItem(String Name,String Price,String Count,String ID){
        this.Name = Name;
        this.Price = Price;
        this.Count = Count;
        this.ID = ID;
    }
    void putName(String Name){this.Name = Name;}
    void putPrice(String Price){this.Price = Price;}
    void putCount(String Count){this.Count = Count;}
    void putID(String ID){this.Count = ID;}
    String getName(){return Name;}
    String getPrice(){return Price;}
    String getCount(){return Count;}
    String getID(){return ID;}

}

package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/28.
 */

public class Meal implements Serializable {
    private String ID,MealClass,Name,Price,Sequence;
    public Meal(){};
    public Meal(String ID,String MealClass,String Name,String Price,String Sequence){
        this.ID = ID;
        this.MealClass = MealClass;
        this.Name = Name;
        this.Price = Price;
        this.Sequence = Sequence;
    };
    public String getID(){return ID;}
    public String getMealClass(){return MealClass;}
    public String getName(){return Name;}
    public String getPrice(){return Price;}
    public String getSequence(){return Sequence;}
    public void putID(String ID){this.ID = ID;}
    public void putMealClass(String MealClass){this.MealClass = MealClass;}
    public void putName(String Name){this.Name = Name;}
    public void putPrice(String Price){this.Price = Price;}
    public void putSequence(String Sequence){this.Sequence = Sequence;}
}

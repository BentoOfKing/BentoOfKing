package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/5/5.
 */

public class OrderIncludeMeal {
    private String OrderID,MealID,Count;
    public OrderIncludeMeal(){}
    public OrderIncludeMeal(String OrderID,String MealID,String Count){
        this.OrderID = OrderID;
        this.MealID = MealID;
        this.Count = Count;
    }
    public String getOrderID(){return this.OrderID;}
    public String getMealID(){return this.MealID;}
    public String getCount(){return this.Count;}
    public void putOrderID(String OrderID){this.OrderID = OrderID;}
    public void putMealID(String MealID){this.MealID = MealID;}
    public void putCount(String Count){this.Count = Count;}
}

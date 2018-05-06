package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/5/5.
 */

public class OrderIncludeMeal {
    private String OrderID,Count;
    private Meal meal;
    public OrderIncludeMeal(){}
    public OrderIncludeMeal(String OrderID,Meal meal,String Count){
        this.OrderID = OrderID;
        this.meal = meal;
        this.Count = Count;
    }
    public String getOrderID(){return this.OrderID;}
    public Meal getMeal(){return this.meal;}
    public String getCount(){return this.Count;}
    public void putOrderID(String OrderID){this.OrderID = OrderID;}
    public void putMeal(Meal meal){this.meal = meal;}
    public void putCount(String Count){this.Count = Count;}
}

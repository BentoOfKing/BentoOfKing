package com.cce.nkfust.tw.bentoofking;

import java.util.ArrayList;

/**
 * Created by John on 2018/7/22.
 */

public class MealClass {
    private String ID,Store,Name,Sequence;
    private ArrayList<Meal> meal;
    public MealClass(){
        meal = new ArrayList<Meal>();
    };
    public MealClass(String ID,String Store,String Name,String Sequence,ArrayList<Meal> meal){
        this.ID = ID;
        this.Store = Store;
        this.Name = Name;
        this.Sequence = Sequence;
        this.meal = meal;
    };
    public String getID(){return ID;}
    public String getStore(){return Store;}
    public String getName(){return Name;}
    public String getSequence(){return Sequence;}
    public ArrayList<Meal> getMeal(){return meal;}
    public void putID(String ID){this.ID = ID;}
    public void putStore(String Store){this.Store = Store;}
    public void putName(String Name){this.Name = Name;}
    public void putSequence(String Sequence){this.Sequence = Sequence;}
    public void putMeal(ArrayList<Meal> meal){this.meal = meal;}
}

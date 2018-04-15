package com.cce.nkfust.tw.bentoofking;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Asus on 2018/4/13.
 */

public class store_list {


    private ImageView image;
    private String storename;
    private String evaluation;
    private String price;
    private String distance;
    private String status;
    public store_list(String storename ,String evaluation ,String price ,String distance,String status){
        this.storename=storename;
        this.evaluation=evaluation;
        this.price=price;
        this.distance=distance;
        this.status = status;
    }
    public String getStorename(){
        return this.storename;
    }
    public void setStorename(String storename){
        this.storename=storename;
    }
    public String getEvaluation(){
        return this.evaluation;
    }
    public void setEvaluation(String evaluation){
        this.evaluation=evaluation;
    }
    public String getPrice(){
        return this.price;
    }
    public void setPrice(String price){
        this.price=price;
    }
    public String getDistance(){
        return this.distance;
    }
    public void setDistance(String distance){ this.distance=distance; }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status=status;
    }
}

package com.cce.nkfust.tw.bentoofking;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Asus on 2018/4/13.
 */

public class store_list {


    private String imageURL;
    private String storename;
    private String evaluation;
    private String price;
    private String distance;
    private String status;
    private Store store;
    public store_list(Store inputStore){
        store = inputStore;
        this.imageURL = store.getPhoto();
        this.storename=store.getStoreName();
        this.evaluation=store.getRank();
        this.price=store.getPrice();
        this.distance=store.getDistance();
        this.status = store.getState();
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
    public void setEvaluation(String evaluation){ this.evaluation=evaluation; }
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
    public String getImageURL(){ return this.imageURL; }
    public void setImageURL(String imageURL){ this.imageURL = imageURL; }
    public Store getStoreInfo(){ return this.store; }
}

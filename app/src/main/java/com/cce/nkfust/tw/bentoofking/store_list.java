package com.cce.nkfust.tw.bentoofking;

import android.text.format.Time;
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
        this.imageURL = store.getFirstPhoto();
        this.storename=store.getStoreName();
        this.evaluation=store.getRank();
        this.price=store.getPrice();
        this.distance=store.getDistance();
        setDoBusiness(inputStore.getBusinessHours());
    }

    private void setDoBusiness(String storeTime){
        Time time = new Time();
        time.setToNow();
        int timeNow = time.hour*60 + time.minute;
        int amStart=0,amEnd=0,pmStart=0,pmEnd=0;
        if(storeTime.length()>=8) {
            amStart = Integer.valueOf(storeTime.substring(0, 2)) * 60 + Integer.valueOf(storeTime.substring(2, 4));
            amEnd = Integer.valueOf(storeTime.substring(4, 6)) * 60 + Integer.valueOf(storeTime.substring(6, 8));
        }
        if(storeTime.length()>=16) {
            pmStart = Integer.valueOf(storeTime.substring(8, 10)) * 60 + Integer.valueOf(storeTime.substring(10, 12));
            pmEnd = Integer.valueOf(storeTime.substring(12, 14)) * 60 + Integer.valueOf(storeTime.substring(14, 16));
        }
        if((timeNow>=amStart&&timeNow<amEnd)||(timeNow>=pmStart&&timeNow<pmEnd))
            this.status = "營業中";
        else
            this.status = "未營業";
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

package com.cce.nkfust.tw.bentoofking;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Asus on 2018/4/13.
 */

public class store_list {


    private ImageView image;
    private String store;
    private String storename;
    private String fraction;
    private String star;
    private String price;
    private String money;
    private String distance;
    public store_list(String storename ,String star ,String money ,String distance){
        this.store="店名";
        this.storename=storename;
        this.fraction="評分";
        this.star=star;
        this.price="價格";
        this.money=money;
        this.distance=distance;
    }
    public String getStore(){
        return store;
    }
    public void setStore(){
        this.store=store;
    }
    public String getStorename(){
        return storename;
    }
    public void setStorename(){
        this.storename=storename;
    }
    public String getFraction(){
        return fraction;
    }
    public void setFraction(){
        this.fraction=fraction;
    }
    public String getStar(){
        return star;
    }
    public void setStar(){
        this.star=star;
    }
    public String getPrice(){
        return price;
    }
    public void setPrice(){
        this.price=price;
    }
    public String getMoney(){
        return money;
    }
    public void setMoney(){
        this.money=money;
    }
    public String getDistance(){
        return distance;
    }
    public void setDistance(){
        this.distance=distance;
    }
}

package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/5/5.
 */

public class MemberOrder implements Serializable {
    private String ID,Member,Store,Price,Time,Address,State,Phone,Name,SendTime;//0:店家未確認，1:會員未確認，2:已確認，3:送餐中，4:已完成，5:已刪除
    public MemberOrder(){}
    public MemberOrder(String ID,String Member,String Store,String Time,String State){
        this.ID = ID;
        this.Member = Member;
        this.Store = Store;
        this.Time = Time;
        this.State = State;
        this.Phone = "";
        this.Name = "";
        this.SendTime = "";
    }
    public String getID(){return this.ID;}
    public String getMember(){return this.Member;}
    public String getStore(){return this.Store;}
    public String getPrice(){return this.Price;}
    public String getTime(){return this.Time;}
    public String getState(){return this.State;}
    public String getAddress(){return this.Address;}
    public String getPhone(){return this.Phone;}
    public String getName(){return this.Name;}
    public String getSendTime(){return this.SendTime;}
    public void putID(String ID){this.ID = ID;}
    public void putMember(String Member){this.Member = Member;}
    public void putStore(String Store){this.Store = Store;}
    public void putPrice(String Price){this.Price = Price;}
    public void putTime(String Time){this.Time = Time;}
    public void putState(String State){this.State = State;}
    public void putPhone(String Phone){this.Phone = Phone;}
    public void putAddress(String Address){this.Address = Address;}
    public void putName(String Name){this.Name = Name;}
    public void putSendTime(String SendTime){this.SendTime = SendTime;}
}

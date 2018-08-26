package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/5/5.
 */

public class MemberOrder {
    private String ID,Member,Store,Price,Time,Address,State;//0:店家未確認，1:會員未確認，2:已確認，3:送餐中，4:已完成，5:已刪除
    public MemberOrder(){}
    public MemberOrder(String ID,String Member,String Store,String Time,String State){
        this.ID = ID;
        this.Member = Member;
        this.Store = Store;
        this.Time = Time;
        this.State = State;
    }
    public String getID(){return this.ID;}
    public String getMember(){return this.Member;}
    public String getStore(){return this.Store;}
    public String getPrice(){return this.Price;}
    public String getTime(){return this.Time;}
    public String getState(){return this.State;}
    public String getAddress(){return this.Address;}
    public void putID(String ID){this.ID = ID;}
    public void putMember(String Member){this.Member = Member;}
    public void putStore(String Store){this.Store = Store;}
    public void putPrice(String Price){this.Price = Price;}
    public void putTime(String Time){this.Time = Time;}
    public void putState(String State){this.State = State;}
    public void putAddress(String Address){this.Address = Address;}
}

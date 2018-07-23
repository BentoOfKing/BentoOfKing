package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/5/5.
 */

public class MemberOrder {
    private String ID,Member,Time,State;//0:店家未確認，1:會員未確認，2:已確認，3:送餐中，4:已完成
    public MemberOrder(){}
    public MemberOrder(String ID,String Member,String Time,String State){
        this.ID = ID;
        this.Member = Member;
        this.Time = Time;
        this.State = State;
    }
    public String getID(){return this.ID;}
    public String getMember(){return this.Member;}
    public String getTime(){return this.Time;}
    public String getState(){return this.State;}
    public void putID(String ID){this.ID = ID;}
    public void putMember(String Member){this.Member = Member;}
    public void putTime(String Time){this.Time = Time;}
    public void putState(String State){this.State = State;}
}

package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/5/5.
 */

public class MemberOrder {
    private String ID,Member,Time;
    public MemberOrder(){}
    public MemberOrder(String ID,String Member,String Time){
        this.ID = ID;
        this.Member = Member;
        this.Time = Time;
    }
    public String getID(){return this.ID;}
    public String getMember(){return this.Member;}
    public String getTime(){return this.Time;}
    public void putID(String ID){this.ID = ID;}
    public void putMember(String Member){this.Member = Member;}
    public void putTime(String Time){this.Time = Time;}
}

package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/8/28.
 */

public class Click {
    String Member,Store,Time;
    public Click(){}
    public Click(String Member,String Store ,String Time){
        this.Member = Member;
        this.Store = Store;
        this.Time = Time;
    }
    public String getMember(){return Member;}
    public String getStore(){return Store;}
    public String getTime(){return Time;}
    public void putMember(String Member){this.Member = Member;}
    public void putStore(String Store){this.Store = Store;}
    public void putTime(String Time){this.Time = Time;}
}

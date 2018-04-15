package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/15.
 */

public class UserInfo implements Serializable {
    private int identity;//visitor:0,member:1,,store:2,admin:3
    private Member member;
    private Store store;
    private Admin admin;
    public UserInfo(){
        identity = 0;
        member = null;
        store = null;
        admin = null;
    }
    void setIdentity(int identity){
        this.identity = identity;
    }
    void putMember(Member member){
        this.member = member;
    }
    void putStore(Store store){
        this.store = store;
    }
    void putAdmin(Admin admin){
        this.admin = admin;
    }
    int getIdentity(){
        return identity;
    }
    Member getMember(){
        return member;
    }
    Store getStore(){
        return store;
    }
    Admin getAdmin(){
        return admin;
    }
}

package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/17.
 */

public class Comment implements Serializable{
    private String ID,Member,Store,Score,StoreContent,Time,Reply,Note,MemberNickName;
    private Member memberInfo;
    public Comment(){}
    public Comment(String ID,String Member,String Store,String Score,String StoreContent,String Time,String Reply,String Note){
        this.ID = ID;
        this.Member = Member;
        this.Store = Store;
        this.Score = Score;
        this.StoreContent = StoreContent;
        this.Time = Time;
        this.Reply = Reply;
        this.Note = Note;
    }
    public String getID(){
        return ID;
    }
    public String getMember(){
        return Member;
    }
    public String getStore(){
        return Store;
    }
    public String getScore(){
        return Score;
    }
    public String getStoreContent(){
        return StoreContent;
    }
    public String getContentTime(){
        return Time;
    }
    public String getReply(){
        return Reply;
    }
    public String getNote(){
        return Note;
    }
    public void putID(String ID){
        this.ID = ID;
    }
    public void putMember(String Member){
        this.Member = Member;
    }
    public void putStore(String Store){
        this.Store = Store;
    }
    public void putScore(String Score){
        this.Score = Score;
    }
    public void putStoreContent(String StoreContent){
        this.StoreContent = StoreContent;
    }
    public void putContentTime(String Time){
        this.Time = Time;
    }
    public void putReply(String Reply){
        this.Reply = Reply;
    }
    public void putMemberInfo( Member member ){
        this.memberInfo = member;
    }
    public Member getMemberInfo() { return this.memberInfo; }
    public void putNote(String Note){
        this.Note = Note;
    }
    public void setMemberNickName(String nickName){ this.MemberNickName = nickName; }
    public String getMemberNickName(){ return this.MemberNickName; }
}


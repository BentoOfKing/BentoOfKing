package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/4/17.
 */

public class Comment {
    String ID,Member,Store,StoreContent,Time,Reply,Note;
    public Comment(){}
    public Comment(String ID,String Member,String Store,String StoreContent,String Time,String Reply,String Note){
        this.ID = ID;
        this.Member = Member;
        this.Store = Store;
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
    public String getStoreContent(){
        return StoreContent;
    }
    public String getTime(){
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
    public void putStoreContent(String StoreContent){
        this.StoreContent = StoreContent;
    }
    public void putTime(String Time){
        this.Time = Time;
    }
    public void putReply(String Reply){
        this.Reply = Reply;
    }
    public void putNote(String Note){
        this.Note = Note;
    }
}


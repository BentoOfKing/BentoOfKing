package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/5/5.
 */

public class Appeal {
    private String ID,Member,Type,Title,Content,Result,Note;
    public Appeal(){}
    public Appeal(String ID,String Member,String Type,String Title,String Content,String Result,String Note){
        this.ID = ID;
        this.Member = Member;
        this.Type = Type;
        this.Title = Title;
        this.Content = Content;
        this.Result = Result;
        this.Note = Note;
    }
    public String getID(){return this.ID;}
    public String getMember(){return this.Member;}
    public String getType(){return this.Type;}
    public String getTitle(){return this.Title;}
    public String getContent(){return this.Content;}
    public String getResult(){return this.Result;}
    public String getNote(){return this.Note;}
    public void putID(String ID){this.ID = ID;}
    public void putMember(String Member){this.Member = Member;}
    public void putType(String Type){this.Type = Type;}
    public void putTitle(String Title){this.Title = Title;}
    public void putContent(String Content){this.Content = Content;}
    public void putResult(String Result){this.Result = Result;}
    public void putNote(String Note){this.Note = Note;}
}

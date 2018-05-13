package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/5/5.
 */

public class Appeal implements Serializable {
    private String ID,Declarant,Appealed,Type,Title,Content,Result,Note;
    public Appeal(){}
    public Appeal(String ID,String Declarant,String Appealed,String Type,String Title,String Content,String Result,String Note){
        this.ID = ID;
        this.Declarant = Declarant;
        this.Appealed = Appealed;
        this.Type = Type; //0:會員申訴，1:店家申訴，2:店家錯誤，3:檢舉評論
        this.Title = Title;
        this.Content = Content;
        this.Result = Result;
        this.Note = Note;
    }
    public String getID(){return this.ID;}
    public String getDeclarant(){return this.Declarant;}
    public String getAppealed(){return this.Appealed;}
    public String getType(){return this.Type;}
    public String getTitle(){return this.Title;}
    public String getContent(){return this.Content;}
    public String getResult(){return this.Result;}
    public String getNote(){return this.Note;}
    public void putID(String ID){this.ID = ID;}
    public void putDeclarant(String Declarant){this.Declarant = Declarant;}
    public void putAppealed(String Appealed){this.Appealed = Appealed;}
    public void putType(String Type){this.Type = Type;}
    public void putTitle(String Title){this.Title = Title;}
    public void putContent(String Content){this.Content = Content;}
    public void putResult(String Result){this.Result = Result;}
    public void putNote(String Note){this.Note = Note;}
}

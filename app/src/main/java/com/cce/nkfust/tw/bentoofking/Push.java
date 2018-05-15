package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/5/14.
 */

public class Push implements Serializable {
    String ID,Store,Content,State,Note;
    public Push(){}
    public Push(String ID,String Store,String Content,String State,String Note){
        this.ID = ID;
        this.Store = Store;
        this.Content = Content;
        this.State = State; //0:未審核，1:待審核，2:已審核，3:已發送
        this.Note = Note;
    }
    void putID(String ID){this.ID = ID;}
    void putStore(String Store){this.Store = Store;}
    void putContent(String Content){this.Content = Content;}
    void putState(String State){this.State = State;}
    void putNote(String Note){this.Note = Note;}
    String getID(){return ID;}
    String getStore(){return Store;}
    String getContent(){return Content;}
    String getState(){return State;}
    String getNote(){return Note;}
}

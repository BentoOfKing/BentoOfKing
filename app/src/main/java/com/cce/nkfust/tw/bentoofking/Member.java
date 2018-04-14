package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/4/13.
 */

public class Member {
    String Email,Password,Nickname,Sex,Favorite,State,Note;
    public Member(){}
    public Member(String Email,String Password,String Nickname,String Sex,String Favorite,String State,String Note){
        this.Email = Email;
        this.Password = Password;
        this.Nickname = Nickname;
        this.Sex = Sex;
        this.Favorite = Favorite;
        this.State = State;
        this.Note = Note;
    }
    void putEmail(String Email){
        this.Email = Email;
    }
    void putPassword(String Password){
        this.Password = Password;
    }
    void putNickname(String Nickname){
        this.Nickname = Nickname;
    }
    void putSex(String Sex){
        this.Sex = Sex;
    }
    void putFavorite(String Information){
        this.Favorite = Favorite;
    }
    void putState(String State){
        this.State = State;
    }
    void putNote(String Note){
        this.Note = Note;
    }
    String getEmail(){
        return Email;
    }
    String getPassword(){
        return Password;
    }
    String getNickname(){
        return Nickname;
    }
    String getSex(){
        return Sex;
    }
    String getFavorite(){
        return Favorite;
    }
    String getState(){
        return State;
    }
    String getNote(){
        return Note;
    }
}

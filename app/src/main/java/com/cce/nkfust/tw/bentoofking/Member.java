package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/13.
 */


public class Member implements Serializable{
    String Email,Password,Nickname,Sex,Point,Favorite,State,Note,Longitude,Latitude,Token;
    public Member(){}
    public Member(String Email,String Password,String Nickname,String Sex,String Point,String Favorite,String State,String Note){
        this.Email = Email;
        this.Password = Password;
        this.Nickname = Nickname;
        this.Sex = Sex;
        this.Point = Point;
        this.Favorite = Favorite;
        this.State = State;
        this.Note = Note;
        this.Longitude = "";
        this.Latitude = "";
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
    void putPoint(String Point){
        this.Point = Point;
    }
    void putFavorite(String Favorite){
        this.Favorite = Favorite;
    }
    void putState(String State){
        this.State = State;
    }
    void putNote(String Note){
        this.Note = Note;
    }
    void putLongitude(String Longitude){
        this.Longitude = Longitude;
    }
    void putLatitude(String Latitude){
        this.Latitude = Latitude;
    }
    void putToken(String Token){
        this.Token = Token;
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
    String getPoint(){
        return Point;
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
    String getLongitude(){
        return Longitude;
    }
    String getLatitude(){
        return Latitude;
    }
    String getToken(){
        return Token;
    }
}

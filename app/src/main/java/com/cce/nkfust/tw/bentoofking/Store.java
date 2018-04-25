package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/10.
 */

public class Store implements Serializable {
    private static String storeIconURL = "http://163.18.104.169/storeImage/";
    String ID,Email,Password,Name,Address,Information,BusinessHours,Phone,Photo,Point,State,Note,Longitude,Latitude,Rank,Price,Distance;
    public Store(){}
    public Store(String ID,String Email,String Password,String Name,String Address,String Information,String BusinessHours,String Phone,String Photo,String Point,String State,String Note,String Longitude,String Latitude,String Rank,String Price){
        this.ID = ID;
        this.Email = Email;
        this.Password = Password;
        this.Name = Name;
        this.Address = Address;
        this.Information = Information;
        this.BusinessHours = BusinessHours;
        this.Phone = Phone;
        String firstPhoto;
        if(Photo.indexOf(',')>0)
            firstPhoto = Photo.substring(0,Photo.indexOf(','));
        else
            firstPhoto = Photo;
        this.Photo = storeIconURL + firstPhoto;
        this.Point = Point;
        this.State = State;
        this.Note = Note;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.Rank = Rank;
        this.Price = Price;
    }
    void putID(String ID){
        this.ID = ID;
    }
    void putEmail(String Email){
        this.Email = Email;
    }
    void putPassword(String Password){
        this.Password = Password;
    }
    void putStoreName(String Name){
        this.Name = Name;
    }
    void putAddress(String Address){
        this.Address = Address;
    }
    void putInformation(String Information){
        this.Information = Information;
    }
    void putBusinessHours(String BusinessHours){
        this.BusinessHours = BusinessHours;
    }
    void putPhone(String Phone){
        this.Phone = Phone;
    }
    void putPhoto(String Photo){
        this.Photo = Photo;
    }
    void putPoint(String Point){
        this.Point = Point;
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
    void putRank(String Rank){
        this.Rank = Rank;
    }
    void putPrice(String Price){
        this.Price = Price;
    }
    void putDistance(String Distance){
        this.Distance = Distance;
    }
    String getID(){
        return ID;
    }
    String getEmail(){
        return Email;
    }
    String getPassword(){
        return Password;
    }
    String getStoreName(){
        return Name;
    }
    String getAddress(){
        return Address;
    }
    String getInformation(){
        return Information;
    }
    String getBusinessHours(){
        return BusinessHours;
    }
    String getPhone(){
        return Phone;
    }
    String getPhoto(){return Photo; }
    String getPoint(){
        return Point;
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
    String getLatitude(){return Latitude;}
    String getRank(){
        return Rank;
    }
    String getPrice(){
        return Price;
    }
    String getDistance(){
        return Distance;
    }
}

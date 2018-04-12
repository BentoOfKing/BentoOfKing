package com.cce.nkfust.tw.bentoofking;

/**
 * Created by John on 2018/4/10.
 */

public class Store {
    String ID,Email,Password,Name,Address,Information,BusinessHours,Phone,Photo,Point,State,Note;
    public Store(){}
    public Store(String ID,String Email,String Password,String Name,String Address,String Information,String BusinessHours,String Phone,String Photo,String Point,String State,String Note){
        this.ID = ID;
        this.Email = Email;
        this.Password = Password;
        this.Name = Name;
        this.Address = Address;
        this.Information = Information;
        this.BusinessHours = BusinessHours;
        this.Phone = Phone;
        this.Photo = Photo;
        this.Point = Point;
        this.State = State;
        this.Note = Note;
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
    String getPhoto(){
        return Photo;
    }
    String getPoint(){
        return Point;
    }
    String getState(){
        return State;
    }
    String getNote(){
        return Note;
    }
}

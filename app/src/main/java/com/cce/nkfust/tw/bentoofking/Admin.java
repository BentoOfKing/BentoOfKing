package com.cce.nkfust.tw.bentoofking;

import java.io.Serializable;

/**
 * Created by John on 2018/4/15.
 */

public class Admin implements Serializable {
    private String Email;
    private String Password;
    public Admin(){}
    public Admin(String Email,String Password){
        this.Email=Email;
        this.Password=Password;
    }
    public void putEmail(String Email){
        this.Email=Email;
    }
    public void putPassword(String Password){
        this.Password=Password;
    }
    public String getEmail(){
        return Email;
    }
    public String getPassword(){
        return Password;
    }
}

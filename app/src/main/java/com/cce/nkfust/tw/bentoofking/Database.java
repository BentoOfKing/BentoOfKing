package com.cce.nkfust.tw.bentoofking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John on 2018/4/7.
 */

public class Database {
    private static String getStoreURL = "http://163.18.104.169/databaseConnect/getStore.php";
    private static String memberLoginURL = "http://163.18.104.169/databaseConnect/member_login.php";
    private static String storeLoginURL = "http://163.18.104.169/databaseConnect/store_login.php";
    private static String adminLoginURL = "http://163.18.104.169/databaseConnect/admin_login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_STORES = "store";
    private static final String TAG_MEMBERS = "member";
    private static final String TAG_ADMINS = "admin";
    private static final String TAG_Message = "message";
    private static final String TAG_ID = "ID";
    private static final String TAG_Email = "Email";
    private static final String TAG_Password = "Password";
    private static final String TAG_Name = "Name";
    private static final String TAG_Nickname = "Nickname";
    private static final String TAG_Sex = "Sex";
    private static final String TAG_Favorite = "Favorite";
    private static final String TAG_Address = "Address";
    private static final String TAG_Information = "Information";
    private static final String TAG_BusinessHours = "BusinessHours";
    private static final String TAG_Phone = "Phone";
    private static final String TAG_Photo = "Photo";
    private static final String TAG_Point = "Point";
    private static final String TAG_State = "State";
    private static final String TAG_Note = "Note";
    JSONParser jParser;
    JSONObject json;

    int range,index=0;
    ArrayList<HashMap<String, String>> storesList;

    String result = "";
    HttpURLConnection urlConnection = null;
    InputStream is = null;

    public Member MemberLogin(String Email,String Password){
        int success;
        try {
            jParser = null;
            jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Email", Email));
            params.add(new BasicNameValuePair("Password", Password));
            json = null;
            json = jParser.makeHttpRequest(memberLoginURL, "GET", params);
            Log.d("Member login.", json.toString());
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray(TAG_MEMBERS); // JSON Array
                JSONObject m = productObj.getJSONObject(0);
                Member member = new Member(m.getString(TAG_Email),m.getString(TAG_Password),m.getString(TAG_Nickname),m.getString(TAG_Sex),m.getString(TAG_Favorite),m.getString(TAG_State),m.getString(TAG_Note));
                return member;
            }else{
                Member member = new Member();
                member.putEmail(json.getString(TAG_Message));
                return member;
            }
        }catch(Exception e){
            return null;
        }
    }

    public Store StoreLogin(String Email,String Password){
        int success;
        try {
            jParser = null;
            jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Email", Email));
            params.add(new BasicNameValuePair("Password", Password));
            json = null;
            json = jParser.makeHttpRequest(storeLoginURL, "GET", params);
            Log.d("Store login.", json.toString());
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray(TAG_STORES); // JSON Array
                JSONObject s = productObj.getJSONObject(0);
                Store store = new Store(s.getString(TAG_ID),s.getString(TAG_Email),s.getString(TAG_Password),s.getString(TAG_Name),s.getString(TAG_Address),s.getString(TAG_Information),s.getString(TAG_BusinessHours),s.getString(TAG_Phone),s.getString(TAG_Photo),s.getString(TAG_Point),s.getString(TAG_State),s.getString(TAG_Note));
                return store;
            }else{
                Store store = new Store();
                store.putEmail(json.getString(TAG_Message));
                return store;
            }
        }catch(Exception e){
            return null;
        }
    }

    public Admin AdminLogin(String Email,String Password){
        int success;
        try {
            jParser = null;
            jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Email", Email));
            params.add(new BasicNameValuePair("Password", Password));
            json = null;
            json = jParser.makeHttpRequest(adminLoginURL, "GET", params);
            Log.d("Admin login.", json.toString());
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray(TAG_ADMINS); // JSON Array
                JSONObject a = productObj.getJSONObject(0);
                Admin admin = new Admin(a.getString(TAG_Email),a.getString(TAG_Password));
                return admin;
            }else{
                Admin admin = new Admin();
                admin.putEmail(json.getString(TAG_Message));
                return null;
            }
        }catch(Exception e){
            return null;
        }
    }


    public Store[] GetStore() {
        JSONObject json;
        JSONArray stores = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Store returnStore[];
        try {
            storesList = new ArrayList<HashMap<String, String>>();
            params = new ArrayList<NameValuePair>();
            json = jParser.makeHttpRequest(getStoreURL, "GET", params);
            Log.d("All Stores: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            stores = json.getJSONArray(TAG_STORES);

        }catch (Exception e){
            return null;
        }
        try {
            System.out.println("OK");
                // looping through All Products
                if(stores.length()>=10) {
                    range = 10;
                }else{
                    range = stores.length();
                }
                returnStore = new Store[range];
                for (int i = 0; i < range; i++) {
                    JSONObject c = stores.getJSONObject(i+index);

                    // Storing each json item in variable
                    returnStore[i] = new Store(c.getString(TAG_ID),c.getString(TAG_Email),c.getString(TAG_Password),c.getString(TAG_Name),c.getString(TAG_Address),c.getString(TAG_Information),c.getString(TAG_BusinessHours),c.getString(TAG_Phone),c.getString(TAG_Photo),c.getString(TAG_Point),c.getString(TAG_State),c.getString(TAG_Note));
                }
                index+=range;
            return returnStore;
        } catch (Exception e) {
            System.out.println("error");
            System.out.print(e);
            return null;
        }
    }

    public boolean GetStoreInit() {
        return true;
    }

}

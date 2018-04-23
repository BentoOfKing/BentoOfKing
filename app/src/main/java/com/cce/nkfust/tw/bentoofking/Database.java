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
    private static String memberRegisterURL = "http://163.18.104.169/databaseConnect/member_register.php";
    private static String getCommentURL = "http://163.18.104.169/databaseConnect/getComment.php";
    private static String getSingleMemberURL = "http://163.18.104.169/databaseConnect/getSingleMember.php";
    private static String addCommentURL = "http://163.18.104.169/databaseConnect/addComment.php";
    private static String deleteCommentURL = "http://163.18.104.169/databaseConnect/deleteComment.php";
    private static String updateCommentURL = "http://163.18.104.169/databaseConnect/updateComment.php";
    private static String updateMemberURL = "http://163.18.104.169/databaseConnect/updateMember.php";
    private static String updateStoreURL = "http://163.18.104.169/databaseConnect/updateStore.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_STORES = "store";
    private static final String TAG_MEMBERS = "member";
    private static final String TAG_ADMINS = "admin";
    private static final String TAG_COMMENTS = "comment";
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
    private static final String TAG_Member = "Member";
    private static final String TAG_Store = "Store";
    private static final String TAG_Score = "Score";
    private static final String TAG_StoreContent = "StoreContent";
    private static final String TAG_Time = "Time";
    private static final String TAG_Reply = "Reply";

    JSONParser jParser;
    JSONObject json;

    int range,index=0;
    ArrayList<HashMap<String, String>> storesList;

    String result = "";
    HttpURLConnection urlConnection = null;
    InputStream is = null;

    public Comment[] getComment(String item,String content){
        JSONArray comments = null;
        Store returnStore[];
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Item", item));
        params.add(new BasicNameValuePair("Content", content));
        params.add(new BasicNameValuePair("Index", Integer.toString(index)));
        json = null;
        json = jParser.makeHttpRequest(getCommentURL, "GET", params);
        Log.d("Comments: ", json.toString());
        try{
            comments = json.getJSONArray(TAG_COMMENTS);
            Comment retrunComment[] = new Comment[comments.length()];
            index +=comments.length();
            for(int i=0;i<comments.length();i++){
                JSONObject c = comments.getJSONObject(i);
                retrunComment[i] = new Comment(c.getString(TAG_ID),c.getString(TAG_Member),c.getString(TAG_Store),c.getString(TAG_Score),c.getString(TAG_StoreContent),c.getString(TAG_Time),c.getString(TAG_Reply),c.getString(TAG_Note));
            }
            return retrunComment;
        }catch (Exception e){
            return new Comment[0];
        }
    }

    public String addComment(Comment comment){
        String member = comment.getMember();
        String store = comment.getStore();
        String score = comment.getScore();
        String storeContent = comment.getStoreContent();
        String time = comment.getContentTime();
        String reply = comment.getReply();
        String note = comment.getNote();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("Member", member));
        params.add(new BasicNameValuePair("Store", store));
        params.add(new BasicNameValuePair("Score", score));
        params.add(new BasicNameValuePair("StoreContent", storeContent));
        params.add(new BasicNameValuePair("Time", time));
        params.add(new BasicNameValuePair("Reply", reply));
        params.add(new BasicNameValuePair("Note", note));
        json = null;
        json = jParser.makeHttpRequest(addCommentURL,"POST", params);
        Log.d("Add Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return "Successful.";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public String deleteComment(String id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", id));
        json = null;
        json = jParser.makeHttpRequest(deleteCommentURL,"POST", params);
        Log.d("Delete Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return "Successful.";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public String updateComment(Comment comment){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", comment.getID()));
        params.add(new BasicNameValuePair("Note", comment.getNote()));
        params.add(new BasicNameValuePair("Reply", comment.getReply()));
        json = null;
        json = jParser.makeHttpRequest(updateCommentURL,"POST", params);
        Log.d("Update Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return "Successful.";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public String MemberRegister(Member member){
        String email = member.getEmail();
        String password = member.getPassword();
        String sex= member.getSex();
        String nickname = member.getNickname();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("Email", email));
        params.add(new BasicNameValuePair("Password", password));
        params.add(new BasicNameValuePair("Sex", sex));
        params.add(new BasicNameValuePair("Nickname", nickname));
        json = null;
        json = jParser.makeHttpRequest(memberRegisterURL,"POST", params);
        Log.d("Register Response", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return "Successful.";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }
    public String UpdateMember(Member member){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("Email", member.getEmail()));
        params.add(new BasicNameValuePair("Password", member.getPassword()));
        params.add(new BasicNameValuePair("Nickname", member.getNickname()));
        json = null;
        json = jParser.makeHttpRequest(updateMemberURL,"POST", params);
        Log.d("Update Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return "Successful.";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }
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
                return admin; //台安修改   原本: return null;
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
            Store[] nullStore = new Store[0];
            return nullStore;
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
            Store[] nullStore = new Store[0];
            return nullStore;
        }
    }

    public String UpdateStore(Store store){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", store.getID()));
        params.add(new BasicNameValuePair("Password", store.getPassword()));
        params.add(new BasicNameValuePair("Name", store.getStoreName()));
        params.add(new BasicNameValuePair("Address", store.getAddress()));
        params.add(new BasicNameValuePair("Information", store.getInformation()));
        params.add(new BasicNameValuePair("BusinessHours", store.getBusinessHours()));
        params.add(new BasicNameValuePair("Phone", store.getPhone()));
        params.add(new BasicNameValuePair("Photo", store.getPhoto()));
        //params.add(new BasicNameValuePair("Longitude", store.getLongitude()));
        //params.add(new BasicNameValuePair("Latitude", store.getLatitude()));

        json = null;
        json = jParser.makeHttpRequest(updateStoreURL,"POST", params);
        Log.d("Update Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                return "Successful.";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public Member GetSingleMember(String Email){
        int success;
        try {
            jParser = null;
            jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Email", Email));
            json = null;
            json = jParser.makeHttpRequest(getSingleMemberURL, "GET", params);
            Log.d("Get single member.", json.toString());
            success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                JSONArray productObj = json.getJSONArray(TAG_MEMBERS); // JSON Array
                JSONObject m = productObj.getJSONObject(0);
                Member member = new Member(m.getString(TAG_Email),"",m.getString(TAG_Nickname),m.getString(TAG_Sex),m.getString(TAG_Favorite),m.getString(TAG_State),m.getString(TAG_Note));
                return member;
            }else{
                return null;
            }
        }catch(Exception e){
            return null;
        }
    }

    public boolean GetStoreInit() {
        return true;
    }

}

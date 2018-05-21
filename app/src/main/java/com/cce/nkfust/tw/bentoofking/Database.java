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
import java.io.UnsupportedEncodingException;
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
    private static String getStoreByPositionURL = "http://163.18.104.169/databaseConnect/getStoreByPosition.php";
    private static String getStoreByMapURL = "http://163.18.104.169/databaseConnect/getStoreByMap.php";
    private static String getReviewStoreURL = "http://163.18.104.169/databaseConnect/getReviewStore.php";
    private static String getSingleStoreURL = "http://163.18.104.169/databaseConnect/getSingleStore.php";
    private static String getStoreForRegisterURL = "http://163.18.104.169/databaseConnect/getStoreForRegister.php";
    private static String getSpecifiedStoreURL = "http://163.18.104.169/databaseConnect/getSpecifiedStore.php";
    private static String memberLoginURL = "http://163.18.104.169/databaseConnect/member_login.php";
    private static String storeLoginURL = "http://163.18.104.169/databaseConnect/store_login.php";
    private static String adminLoginURL = "http://163.18.104.169/databaseConnect/admin_login.php";
    private static String memberRegisterURL = "http://163.18.104.169/databaseConnect/member_register.php";
    private static String getCommentURL = "http://163.18.104.169/databaseConnect/getComment.php";
    private static String getSingleCommentURL = "http://163.18.104.169/databaseConnect/getSingleComment.php";
    private static String getSingleMemberURL = "http://163.18.104.169/databaseConnect/getSingleMember.php";
    private static String getBanedMemberURL = "http://163.18.104.169/databaseConnect/getBanedMember.php";
    private static String addCommentURL = "http://163.18.104.169/databaseConnect/addComment.php";
    private static String deleteCommentURL = "http://163.18.104.169/databaseConnect/deleteComment.php";
    private static String updateCommentURL = "http://163.18.104.169/databaseConnect/updateComment.php";
    private static String updateMemberURL = "http://163.18.104.169/databaseConnect/updateMember.php";
    private static String updateStoreURL = "http://163.18.104.169/databaseConnect/updateStore.php";
    private static String addStoreURL = "http://163.18.104.169/databaseConnect/addStore.php";
    private static String addMealURL = "http://163.18.104.169/databaseConnect/addMeal.php";
    private static String getMealURL = "http://163.18.104.169/databaseConnect/getMeal.php";
    private static String getOrderStoreCostURL = "http://163.18.104.169/databaseConnect/getOrderStoreCost.php";
    private static String deleteMealURL = "http://163.18.104.169/databaseConnect/deleteMeal.php";
    private static String updateMealURL = "http://163.18.104.169/databaseConnect/updateMeal.php";
    private static String addOrderURL = "http://163.18.104.169/databaseConnect/addOrder.php";
    private static String getOrderURL = "http://163.18.104.169/databaseConnect/getOrder.php";
    private static String addOrderMealURL = "http://163.18.104.169/databaseConnect/addOrderMeal.php";
    private static String getOrderMealURL = "http://163.18.104.169/databaseConnect/getOrderMeal.php";
    private static String addAppealURL = "http://163.18.104.169/databaseConnect/addAppeal.php";
    private static String updateAppealURL = "http://163.18.104.169/databaseConnect/updateAppeal.php";
    private static String getAppealURL = "http://163.18.104.169/databaseConnect/getAppeal.php";
    private static String getOneStoreAppealURL = "http://163.18.104.169/databaseConnect/getOneStoreAppeal.php";
    private static String addPushURL = "http://163.18.104.169/databaseConnect/addPush.php";
    private static String updatePushURL = "http://163.18.104.169/databaseConnect/updatePush.php";
    private static String deletePushURL = "http://163.18.104.169/databaseConnect/deletePush.php";
    private static String getPushURL = "http://163.18.104.169/databaseConnect/getPush.php";
    private static String sendPushURL = "http://163.18.104.169/databaseConnect/sendPush.php";
    private static String updateTokenURL = "http://163.18.104.169/databaseConnect/updateToken.php";
    private static String updatePointURL = "http://163.18.104.169/databaseConnect/updatePoint.php";
    private static final String TAG_APPEAL = "appeal";
    private static final String TAG_PUSH = "push";
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
    private static final String TAG_Longitude = "Longitude";
    private static final String TAG_Latitude = "Latitude";
    private static final String TAG_Rank = "Rank";
    private static final String TAG_Price = "Price";
    private static final String TAG_Distance = "Distance";
    private static final String TAG_Meal = "Meal";
    private static final String TAG_Sequence = "Sequence";
    private static final String TAG_Declarant = "Declarant";
    private static final String TAG_Appealed = "Appealed";
    private static final String TAG_Type = "Type";
    private static final String TAG_Title = "Title";
    private static final String TAG_Content = "Content";
    private static final String TAG_Result = "Result";
    private static final String TAG_OrderIncludeMeal = "order_include_meal";

    JSONParser jParser;
    JSONObject json;

    private int range, index = 0,reviewRange,reviewIndex=0,commentIndex=0;
    ArrayList<HashMap<String, String>> storesList;

    String result = "";
    HttpURLConnection urlConnection = null;
    InputStream is = null;

    public Comment[] getComment(String item, String content) {
        JSONArray comments = null;
        Store returnStore[];
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Item", item));
        params.add(new BasicNameValuePair("Content", content));
        params.add(new BasicNameValuePair("Index", Integer.toString(commentIndex)));
        json = null;
        json = jParser.makeHttpRequest(getCommentURL, "GET", params);
        Log.d("Comments: ", json.toString());
        try {
            comments = json.getJSONArray(TAG_COMMENTS);
            Comment retrunComment[] = new Comment[comments.length()];
            commentIndex += comments.length();
            for (int i = 0; i < comments.length(); i++) {
                JSONObject c = comments.getJSONObject(i);
                retrunComment[i] = new Comment(c.getString(TAG_ID), c.getString(TAG_Member), c.getString(TAG_Store), c.getString(TAG_Score), c.getString(TAG_StoreContent), c.getString(TAG_Time), c.getString(TAG_Reply), c.getString(TAG_Note));
            }
            return retrunComment;
        } catch (Exception e) {
            return new Comment[0];
        }
    }

    public Comment GetSingleComment(String ID) {
        JSONObject json;
        JSONArray comments = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Comment returnComment;
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", ID));
            json = jParser.makeHttpRequest(getSingleCommentURL, "GET", params);
            Log.d("Get A Comment: ", json.toString());
            comments = json.getJSONArray(TAG_COMMENTS);
        } catch (Exception e) {
            return null;
        }
        try {
            System.out.println("OK");
            // looping through All Products
            JSONObject c = comments.getJSONObject(0);
            // Storing each json item in variable
            returnComment = new Comment(c.getString(TAG_ID), c.getString(TAG_Member), c.getString(TAG_Store), c.getString(TAG_Score), c.getString(TAG_StoreContent), c.getString(TAG_Time), c.getString(TAG_Reply), c.getString(TAG_Note));
            return returnComment;
        } catch (Exception e) {
            return null;
        }
    }

    public String addComment(Comment comment) {
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
        try {
            params.add(new BasicNameValuePair("Member", member));
            params.add(new BasicNameValuePair("Store", store));
            params.add(new BasicNameValuePair("Score", score));
            params.add(new BasicNameValuePair("StoreContent", new String(storeContent.getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Time", time));
            params.add(new BasicNameValuePair("Reply", new String(reply.getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Note", new String(note.getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(addCommentURL, "POST", params);
        Log.d("Add Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                String point = json.getString(TAG_Point);
                return "Successful."+"point";
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public String deleteComment(String id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", id));
        json = null;
        json = jParser.makeHttpRequest(deleteCommentURL, "POST", params);
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

    public String updateComment(Comment comment) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("ID", comment.getID()));
            params.add(new BasicNameValuePair("Note", new String(comment.getNote().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Reply", new String(comment.getReply().getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(updateCommentURL, "POST", params);
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

    public String MemberRegister(Member member) {
        String email = member.getEmail();
        String password = member.getPassword();
        String sex = member.getSex();
        String nickname = member.getNickname();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("Email", email));
            params.add(new BasicNameValuePair("Password", password));
            params.add(new BasicNameValuePair("Sex", sex));
            params.add(new BasicNameValuePair("Nickname", new String(nickname.getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(memberRegisterURL, "POST", params);
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

    public String UpdateMember(Member member) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("Email", member.getEmail()));
            params.add(new BasicNameValuePair("Password", member.getPassword()));
            params.add(new BasicNameValuePair("Favorite", member.getFavorite()));
            params.add(new BasicNameValuePair("Nickname", new String(member.getNickname().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("State", member.getState()));
            params.add(new BasicNameValuePair("Note", new String(member.getNote().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Longitude", member.getLongitude()));
            params.add(new BasicNameValuePair("Latitude", member.getLatitude()));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(updateMemberURL, "POST", params);
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

    public Member MemberLogin(String Email, String Password) {
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
                Member member = new Member(m.getString(TAG_Email), m.getString(TAG_Password), m.getString(TAG_Nickname), m.getString(TAG_Sex), m.getString(TAG_Point), m.getString(TAG_Favorite), m.getString(TAG_State), m.getString(TAG_Note));
                return member;
            } else {
                Member member = new Member();
                member.putEmail(json.getString(TAG_Message));
                return member;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public Store StoreLogin(String Email, String Password) {
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
                Store store = new Store(s.getString(TAG_ID), s.getString(TAG_Email), s.getString(TAG_Password), s.getString(TAG_Name), s.getString(TAG_Address), s.getString(TAG_Information), s.getString(TAG_BusinessHours), s.getString(TAG_Phone), s.getString(TAG_Photo), s.getString(TAG_Point), s.getString(TAG_State), s.getString(TAG_Note), s.getString(TAG_Longitude), s.getString(TAG_Latitude), s.getString(TAG_Rank), s.getString(TAG_Price));
                return store;
            } else {
                Store store = new Store();
                store.putEmail(json.getString(TAG_Message));
                return store;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Admin AdminLogin(String Email, String Password) {
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
                Admin admin = new Admin(a.getString(TAG_Email), a.getString(TAG_Password));
                return admin;
            } else {
                Admin admin = new Admin();
                admin.putEmail(json.getString(TAG_Message));
                return admin; //台安修改   原本: return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Store[] GetSpecifiedStore(ArrayList<String> ID){
        int success;
        Store[] store = new Store[ID.size()];
        for(int i=0;i<ID.size();i++) {
            try {
                jParser = null;
                jParser = new JSONParser();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("ID", ID.get(i)));
                json = null;
                json = jParser.makeHttpRequest(getSpecifiedStoreURL, "GET", params);
                Log.d("Get specified atore.", json.toString());
                JSONArray productObj = json.getJSONArray(TAG_STORES); // JSON Array
                JSONObject c = productObj.getJSONObject(0);
                store[i] = new Store(c.getString(TAG_ID), "", "", c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));

            } catch (Exception e) {
                store = new Store[0];
                return store;
            }
        }
        return store;
    }

    public Store GetStoreForRegister(String Name,String Address,String Phone){
        int success;
        try {
            jParser = null;
            jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Name", Name));
            params.add(new BasicNameValuePair("Address", Address));
            params.add(new BasicNameValuePair("Phone", Phone));
            json = null;
            json = jParser.makeHttpRequest(getStoreForRegisterURL, "GET", params);
            Log.d("Get single member.", json.toString());
            JSONArray productObj = json.getJSONArray(TAG_STORES); // JSON Array
            JSONObject c = productObj.getJSONObject(0);
            Store store = new Store(c.getString(TAG_ID), "", "", c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));
            return store;

        } catch (Exception e) {
            return null;
        }
    }

    public Store[] GetStore(String Search,String country, int rankState, int priceState) {
        JSONObject json;
        JSONArray stores = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        String rankString, priceString;
        if (rankState == 0) rankString = "ASC";
        else rankString = "DESC";
        if (priceState == 0) priceString = "ASC";
        else priceString = "DESC";
        Store returnStore[];
        country += "%";
        try {
            //storesList = new ArrayList<HashMap<String, String>>();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(index)));
            params.add(new BasicNameValuePair("Country", country));
            params.add(new BasicNameValuePair("RankString", rankString));
            params.add(new BasicNameValuePair("PriceString", priceString));
            params.add(new BasicNameValuePair("Search", Search));
            json = jParser.makeHttpRequest(getStoreURL, "GET", params);
            Log.d("All Stores: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            stores = json.getJSONArray(TAG_STORES);

        } catch (Exception e) {
            Store[] nullStore = new Store[0];
            return nullStore;
        }
        try {
            System.out.println("OK");
            // looping through All Products

            if (stores.length() >= 10) {
                range = 10;
            } else {
                range = stores.length();
            }
            returnStore = new Store[range];
            for (int i = 0; i < range; i++) {
                JSONObject c = stores.getJSONObject(i);

                // Storing each json item in variable
                returnStore[i] = new Store(c.getString(TAG_ID), c.getString(TAG_Email), c.getString(TAG_Password), c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));
            }
            index += range;
            return returnStore;
        } catch (Exception e) {
            System.out.println("error");
            System.out.print(e);
            Store[] nullStore = new Store[0];
            return nullStore;
        }
    }

    public Store GetSingleStore(String ID) {
        JSONObject json;
        JSONArray stores = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Store returnStore;
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ID", ID));
            json = jParser.makeHttpRequest(getSingleStoreURL, "GET", params);
            Log.d("Get A Store: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            stores = json.getJSONArray(TAG_STORES);

        } catch (Exception e) {
            return null;
        }
        try {
            System.out.println("OK");
            // looping through All Products
            JSONObject c = stores.getJSONObject(0);
            // Storing each json item in variable
            returnStore = new Store(c.getString(TAG_ID), c.getString(TAG_Email), c.getString(TAG_Password), c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));
            return returnStore;
        } catch (Exception e) {
            return null;
        }
    }

    public Store[] GetReviewStore() {
        JSONObject json;
        JSONArray stores = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Store returnStore[];
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(reviewIndex)));
            json = jParser.makeHttpRequest(getReviewStoreURL, "GET", params);
            Log.d("All Stores: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            stores = json.getJSONArray(TAG_STORES);

        } catch (Exception e) {
            Store[] nullStore = new Store[0];
            return nullStore;
        }
        try {
            System.out.println("OK");
            // looping through All Products

            if (stores.length() == 10) {
                reviewRange = 10;
            } else {
                reviewRange = stores.length();
            }
            returnStore = new Store[reviewRange];
            for (int i = 0; i < reviewRange; i++) {
                JSONObject c = stores.getJSONObject(i);

                // Storing each json item in variable
                returnStore[i] = new Store(c.getString(TAG_ID), c.getString(TAG_Email), c.getString(TAG_Password), c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));
            }
            reviewIndex += reviewRange;
            return returnStore;
        } catch (Exception e) {
            System.out.println("error");
            System.out.print(e);
            Store[] nullStore = new Store[0];
            return nullStore;
        }
    }

    public void refreshStoreIndex() {
        index = 0;
    }
    public void refreshReviewStoreIndex() { reviewIndex = 0; }
    public void refreshCommentIndex() { commentIndex=0; }

    public Store[] GetStoreByPosition(String Search,String Longitude, String Latitude, int distanceState, int rankState, int priceState, int distance) {
        JSONObject json;
        JSONArray stores = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        String rankString, priceString, distanceString;
        if (rankState == 0) rankString = "ASC";
        else rankString = "DESC";
        if (priceState == 0) priceString = "ASC";
        else priceString = "DESC";
        if (distanceState == 0) distanceString = "ASC";
        else distanceString = "DESC";
        Store returnStore[];
        try {
            storesList = new ArrayList<HashMap<String, String>>();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(index)));
            params.add(new BasicNameValuePair("Longitude", Longitude));
            params.add(new BasicNameValuePair("Latitude", Latitude));
            params.add(new BasicNameValuePair("DistanceString", distanceString));
            params.add(new BasicNameValuePair("RankString", rankString));
            params.add(new BasicNameValuePair("PriceString", priceString));
            params.add(new BasicNameValuePair("Distance", Integer.toString(distance)));
            params.add(new BasicNameValuePair("Search", Search));
            json = jParser.makeHttpRequest(getStoreByPositionURL, "GET", params);
            Log.d("All Stores: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            stores = json.getJSONArray(TAG_STORES);

        } catch (Exception e) {
            Store[] nullStore = new Store[0];
            return nullStore;
        }
        try {
            // looping through All Products

            if (stores.length() == 10) {
                range = 10;
            } else {
                range = stores.length();
            }
            returnStore = new Store[range];
            for (int i = 0; i < range; i++) {
                JSONObject c = stores.getJSONObject(i);

                // Storing each json item in variable
                returnStore[i] = new Store(c.getString(TAG_ID), c.getString(TAG_Email), c.getString(TAG_Password), c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));
                returnStore[i].putDistance(c.getString(TAG_Distance));
            }
            index += range;
            return returnStore;
        } catch (Exception e) {
            System.out.println("error");
            System.out.print(e);
            Store[] nullStore = new Store[0];
            return nullStore;
        }
    }

    public Store[] GetStoreByMap(String Longitude, String Latitude, String Search) {
        JSONObject json;
        JSONArray stores = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Store returnStore[];
        try {
            storesList = new ArrayList<HashMap<String, String>>();
            params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("Index", Integer.toString(index)));
            params.add(new BasicNameValuePair("Longitude", Longitude));
            params.add(new BasicNameValuePair("Latitude", Latitude));
            params.add(new BasicNameValuePair("Distance", Integer.toString(25)));
            params.add(new BasicNameValuePair("Search", Search));
            json = jParser.makeHttpRequest(getStoreByMapURL, "GET", params);
            Log.d("All Stores: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            stores = json.getJSONArray(TAG_STORES);

        } catch (Exception e) {
            Store[] nullStore = new Store[0];
            return nullStore;
        }
        try {
            // looping through All Products

            if (stores.length() == 10) {
                range = 10;
            } else {
                range = stores.length();
            }
            returnStore = new Store[range];
            for (int i = 0; i < range; i++) {
                JSONObject c = stores.getJSONObject(i);

                // Storing each json item in variable
                returnStore[i] = new Store(c.getString(TAG_ID), c.getString(TAG_Email), "", c.getString(TAG_Name), c.getString(TAG_Address), c.getString(TAG_Information), c.getString(TAG_BusinessHours), c.getString(TAG_Phone), c.getString(TAG_Photo), c.getString(TAG_Point), c.getString(TAG_State), c.getString(TAG_Note), c.getString(TAG_Longitude), c.getString(TAG_Latitude), c.getString(TAG_Rank), c.getString(TAG_Price));
                returnStore[i].putDistance(c.getString(TAG_Distance));
            }
            index += range;
            return returnStore;
        } catch (Exception e) {
            System.out.println("error");
            System.out.print(e);
            Store[] nullStore = new Store[0];
            return nullStore;
        }
    }

    public String UpdateStore(Store store) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("ID", store.getID()));
            params.add(new BasicNameValuePair("Email", store.getEmail()));
            params.add(new BasicNameValuePair("Password", store.getPassword()));
            params.add(new BasicNameValuePair("Name", new String(store.getStoreName().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Address", new String(store.getAddress().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Note", new String(store.getNote().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("State", store.getState()));
            params.add(new BasicNameValuePair("Information", store.getInformation()));
            params.add(new BasicNameValuePair("BusinessHours", store.getBusinessHours()));
            params.add(new BasicNameValuePair("Phone", store.getPhone()));
            params.add(new BasicNameValuePair("Photo", store.getPhoto()));
            params.add(new BasicNameValuePair("Longitude", store.getLongitude()));
            params.add(new BasicNameValuePair("Latitude", store.getLatitude()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(updateStoreURL, "POST", params);
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

    public String addStore(Store store) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("Email", store.getEmail()));
            params.add(new BasicNameValuePair("Password", store.getPassword()));
            params.add(new BasicNameValuePair("Name", new String(store.getStoreName().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Address", new String(store.getAddress().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Information", store.getInformation()));
            params.add(new BasicNameValuePair("BusinessHours", store.getBusinessHours()));
            params.add(new BasicNameValuePair("Phone", store.getPhone()));
            params.add(new BasicNameValuePair("Photo", store.getPhoto()));
            params.add(new BasicNameValuePair("Point", store.getPoint()));
            params.add(new BasicNameValuePair("State", store.getState()));
            params.add(new BasicNameValuePair("Longitude", store.getLongitude().substring(0,10)));
            params.add(new BasicNameValuePair("Latitude", store.getLatitude().substring(0,10)));
            params.add(new BasicNameValuePair("Rank", store.getRank()));
            params.add(new BasicNameValuePair("Price", store.getPrice()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(addStoreURL, "POST", params);
        Log.d("Update Comment.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                String ID = json.getString(TAG_ID);
                return ID;
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public Member GetSingleMember(String Email) {
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
                Member member = new Member(m.getString(TAG_Email), m.getString(TAG_Password), m.getString(TAG_Nickname), m.getString(TAG_Sex), m.getString(TAG_Point), m.getString(TAG_Favorite), m.getString(TAG_State), m.getString(TAG_Note));
                return member;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public Member[] GetBanedMember() {
        JSONObject json;
        JSONArray members = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Member returnMember[];
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(reviewIndex)));
            json = jParser.makeHttpRequest(getBanedMemberURL, "GET", params);
            Log.d("All Member: ", json.toString());
            members = json.getJSONArray(TAG_MEMBERS);
        } catch (Exception e) {
            Member[] nullMember = new Member[0];
            return nullMember;
        }
        try {
            if (members.length() == 10) {
                reviewRange = 10;
            } else {
                reviewRange = members.length();
            }
            returnMember = new Member[reviewRange];
            for (int i = 0; i < reviewRange; i++) {
                JSONObject m = members.getJSONObject(i);
                returnMember[i] = new Member(m.getString(TAG_Email), m.getString(TAG_Password), m.getString(TAG_Nickname), m.getString(TAG_Sex), m.getString(TAG_Point), m.getString(TAG_Favorite), m.getString(TAG_State), m.getString(TAG_Note));
            }
            reviewIndex += reviewRange;
            return returnMember;
        } catch (Exception e) {
            return null;
        }
    }

    public String addMeal(ArrayList<Meal> meal) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        for (int i = 0; i < meal.size(); i++) {
            try {
                params = null;
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Store", meal.get(i).getStore()));
                params.add(new BasicNameValuePair("Name", new String(meal.get(i).getName().getBytes(), "8859_1")));
                params.add(new BasicNameValuePair("Price", meal.get(i).getPrice()));
                params.add(new BasicNameValuePair("Sequence", meal.get(i).getSequence()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            json = null;
            json = jParser.makeHttpRequest(addMealURL, "POST", params);
            Log.d("Add meal.", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success != 1) {
                    return "An error occurred.";
                }
                if(meal.size()==1){
                    return json.getString(TAG_ID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "Fail.";
            }
        }
        return "Successful.";
    }

    public ArrayList<OrderIncludeMeal> GetOrderMeal(String ID) {
        JSONArray orderIncludeMeals = null;
        JSONArray meals = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("OrderID", ID));
        json = null;
        json = jParser.makeHttpRequest(getOrderMealURL, "GET", params);
        Log.d("Get meal.", json.toString());
        try {
            orderIncludeMeals = json.getJSONArray(TAG_OrderIncludeMeal);
            meals = json.getJSONArray(TAG_Meal);
            ArrayList<OrderIncludeMeal> returnOrderIncludeMeal = new ArrayList<OrderIncludeMeal>();
            for(int i=0;i<meals.length();i++) {
                JSONObject m = meals.getJSONObject(i);
                JSONObject o = orderIncludeMeals.getJSONObject(i);
                Meal meal = new Meal(m.getString(TAG_ID), m.getString(TAG_Store), m.getString(TAG_Name), m.getString(TAG_Price), m.getString(TAG_Sequence));
                OrderIncludeMeal orderIncludeMeal = new OrderIncludeMeal(o.getString("OrderID"), meal, o.getString("Count"));
                returnOrderIncludeMeal.add(orderIncludeMeal);
            }
            return returnOrderIncludeMeal;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Meal> getMeal(String storeID) {
        JSONArray meals = null;
        ArrayList<Meal> meal = new ArrayList<Meal>();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", storeID));
        json = null;
        json = jParser.makeHttpRequest(getMealURL, "GET", params);
        Log.d("Get meal.", json.toString());
        try {
            meals = json.getJSONArray(TAG_Meal);
            for (int i = 0; i < meals.length(); i++) {
                JSONObject m = meals.getJSONObject(i);
                meal.add(new Meal(m.getString(TAG_ID), storeID, m.getString(TAG_Name), m.getString(TAG_Price), m.getString(TAG_Sequence)));
            }
            return meal;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String deleteMeal(String id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", id));
        json = null;
        json = jParser.makeHttpRequest(deleteMealURL, "POST", params);
        Log.d("Delete meal.", json.toString());
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

    public String updateMeal(ArrayList<Meal> meal) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        for (int i = 0; i < meal.size(); i++) {
            try {
                params = null;
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("ID", meal.get(i).getID()));
                params.add(new BasicNameValuePair("Name", new String(meal.get(i).getName().getBytes(), "8859_1")));
                params.add(new BasicNameValuePair("Price", meal.get(i).getPrice()));
                params.add(new BasicNameValuePair("Sequence", meal.get(i).getSequence()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            json = null;
            json = jParser.makeHttpRequest(updateMealURL, "POST", params);
            Log.d("Update meal.", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success != 1) {
                    return "An error occurred.";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "Fail.";
            }
        }
        return "Successful.";
    }
    public MemberOrder[] GetOrder(String Member) {
        JSONObject json;
        JSONArray orders = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        MemberOrder returnOrder[];
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(reviewIndex)));
            params.add(new BasicNameValuePair("Member", Member));
            json = jParser.makeHttpRequest(getOrderURL, "GET", params);
            Log.d("All Order: ", json.toString());
            orders = json.getJSONArray("order");
        } catch (Exception e) {
            MemberOrder[] nullOrder = new MemberOrder[0];
            return nullOrder;
        }
        try {
            if (orders.length() == 10) {
                reviewRange = 10;
            } else {
                reviewRange = orders.length();
            }
            returnOrder = new MemberOrder[reviewRange];
            for (int i = 0; i < reviewRange; i++) {
                JSONObject m = orders.getJSONObject(i);
                returnOrder[i] = new MemberOrder(m.getString(TAG_ID), m.getString(TAG_Member), m.getString(TAG_Time));
            }
            reviewIndex += reviewRange;
            return returnOrder;
        } catch (Exception e) {
            return null;
        }
    }
    public String AddOrder(MemberOrder memberOrder) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("Member", memberOrder.getMember()));
        params.add(new BasicNameValuePair("Time", memberOrder.getTime()));
        json = null;
        json = jParser.makeHttpRequest(addOrderURL, "POST", params);
        Log.d("Add order.", json.toString());
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                String ID = json.getString(TAG_ID);
                return ID;
            } else {
                return "An error occurred.";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Fail.";
        }
    }

    public String GetOrderStoreCost(String ID) {
        List<NameValuePair> params;
        jParser = null;
        jParser = new JSONParser();
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("ID", ID));
        json = null;
        json = jParser.makeHttpRequest(getOrderStoreCostURL, "GET", params);
            Log.d("Get Meal Store Cost.", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success != 1) {
                    return null;
                }else {
                    return json.getString(TAG_Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
    }

    public String AddOrderMeal(ArrayList<OrderIncludeMeal> order) {
        List<NameValuePair> params;
        jParser = null;
        jParser = new JSONParser();
        for (int i = 0; i < order.size(); i++) {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("OrderID", order.get(i).getOrderID()));
            params.add(new BasicNameValuePair("MealID", order.get(i).getMeal().getID()));
            params.add(new BasicNameValuePair("Count", order.get(i).getCount()));
            json = null;
            json = jParser.makeHttpRequest(addOrderMealURL, "POST", params);
            Log.d("Add order meal.", json.toString());
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success != 1) {
                    return "An error occurred.";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "Fail.";
            }
        }
        return "Successful.";
    }
    public String AddAppeal(Appeal appeal) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try{
            params.add(new BasicNameValuePair("Declarant",appeal.getDeclarant()));
            params.add(new BasicNameValuePair("Appealed", appeal.getAppealed()));
            params.add(new BasicNameValuePair("Type", appeal.getType()));
            params.add(new BasicNameValuePair("Title", new String(appeal.getTitle().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Content", new String(appeal.getContent().getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(addAppealURL, "POST", params);
        Log.d("Add appeal.", json.toString());
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
    public String UpdateAppeal(Appeal appeal) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try{
            params.add(new BasicNameValuePair("ID",appeal.getID()));
            params.add(new BasicNameValuePair("Result", new String(appeal.getResult().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("Note", new String(appeal.getNote().getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(updateAppealURL, "POST", params);
        Log.d("Edit appeal.", json.toString());
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

    public Appeal[] GetAppeal(String type) {
        JSONObject json;
        JSONArray appeals = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Appeal returnAppeal[];
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(reviewIndex)));
            params.add(new BasicNameValuePair("Type", type));
            json = jParser.makeHttpRequest(getAppealURL, "GET", params);
            Log.d("All Appeal: ", json.toString());
            appeals = json.getJSONArray(TAG_APPEAL);
        } catch (Exception e) {
            return null;
        }
        try {
            if (appeals.length() == 10) {
                reviewRange = 10;
            } else {
                reviewRange = appeals.length();
            }
            returnAppeal = new Appeal[reviewRange];
            for (int i = 0; i < reviewRange; i++) {
                JSONObject c = appeals.getJSONObject(i);
                returnAppeal[i] = new Appeal(c.getString(TAG_ID), c.getString(TAG_Declarant), c.getString(TAG_Appealed), c.getString(TAG_Type), c.getString(TAG_Title), c.getString(TAG_Content), c.getString(TAG_Result), c.getString(TAG_Note));
            }
            reviewIndex += reviewRange;
            return returnAppeal;
        } catch (Exception e) {
            return null;
        }
    }
    public Appeal[] GetOneStoreAppeal(String type,String ID) {
        JSONObject json;
        JSONArray appeals = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Appeal returnAppeal[];
        try {
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(reviewIndex)));
            params.add(new BasicNameValuePair("Type", type));
            params.add(new BasicNameValuePair("ID", ID));
            json = jParser.makeHttpRequest(getOneStoreAppealURL, "GET", params);
            Log.d("All Appeal: ", json.toString());
            appeals = json.getJSONArray(TAG_APPEAL);
        } catch (Exception e) {
            return null;
        }
        try {
            if (appeals.length() == 10) {
                reviewRange = 10;
            } else {
                reviewRange = appeals.length();
            }
            returnAppeal = new Appeal[reviewRange];
            for (int i = 0; i < reviewRange; i++) {
                JSONObject c = appeals.getJSONObject(i);
                returnAppeal[i] = new Appeal(c.getString(TAG_ID), c.getString(TAG_Declarant), c.getString(TAG_Appealed), c.getString(TAG_Type), c.getString(TAG_Title), c.getString(TAG_Content), c.getString(TAG_Result), c.getString(TAG_Note));
            }
            reviewIndex += reviewRange;
            return returnAppeal;
        } catch (Exception e) {
            return null;
        }
    }
    public String AddPush(Push push) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("Store", push.getStore()));
            params.add(new BasicNameValuePair("Content", new String(push.getContent().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("State", push.getState()));
            params.add(new BasicNameValuePair("Note", new String(push.getNote().getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(addPushURL, "POST", params);
        Log.d("Add Push.", json.toString());
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
    public String UpdatePush(Push push) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("ID", push.getID()));
            params.add(new BasicNameValuePair("Store", push.getStore()));
            params.add(new BasicNameValuePair("Content", new String(push.getContent().getBytes(), "8859_1")));
            params.add(new BasicNameValuePair("State", push.getState()));
            params.add(new BasicNameValuePair("Note", new String(push.getNote().getBytes(), "8859_1")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        json = null;
        json = jParser.makeHttpRequest(updatePushURL, "POST", params);
        Log.d("Add Push.", json.toString());
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
    public String DeletePush(String ID) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", ID));
        json = null;
        json = jParser.makeHttpRequest(deletePushURL, "POST", params);
        Log.d("Add Push.", json.toString());
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
    public Push[] GetPush(String Store, String State) {
        JSONObject json;
        JSONArray pushes = null;
        jParser = null;
        jParser = new JSONParser();
        List<NameValuePair> params;
        Push returnPush[];
        try {
            storesList = new ArrayList<HashMap<String, String>>();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Index", Integer.toString(index)));
            params.add(new BasicNameValuePair("Store", Store));
            params.add(new BasicNameValuePair("State", State));
            json = jParser.makeHttpRequest(getPushURL, "GET", params);
            Log.d("All Pushes: ", json.toString());
            //int success = json.getInt(TAG_SUCCESS);
            pushes = json.getJSONArray(TAG_PUSH);

        } catch (Exception e) {
            Push[] nullpush = new Push[0];
            return nullpush;
        }
        try {
            // looping through All Products
            if (pushes.length() == 10) {
                range = 10;
            } else {
                range = pushes.length();
            }
            returnPush = new Push[range];
            for (int i = 0; i < range; i++) {
                JSONObject p = pushes.getJSONObject(i);
                // Storing each json item in variable
                returnPush[i] = new Push(p.getString(TAG_ID),p.getString(TAG_Store),p.getString(TAG_Content),p.getString(TAG_State),p.getString(TAG_Note));
            }
            index += range;
            return returnPush;
        } catch (Exception e) {
            System.out.println("error");
            System.out.print(e);
            Push[] nullpush = new Push[0];
            return nullpush;
        }
    }
    public void SendPush(Push push) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        try {
            params.add(new BasicNameValuePair("Content", new String(push.getContent().getBytes(), "8859_1")));
        }catch (Exception e){

        }
        json = null;
        json = jParser.makeHttpRequest(sendPushURL, "POST", params);
    }
    public void UpdateToken(Member member){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("Email", member.getEmail()));
        params.add(new BasicNameValuePair("Token", member.getToken()));
        json = null;
        json = jParser.makeHttpRequest(updateTokenURL, "POST", params);
    }
    public void UpdatePoint(Store store){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        jParser = null;
        jParser = new JSONParser();
        params.add(new BasicNameValuePair("ID", store.getID()));
        params.add(new BasicNameValuePair("Point", store.getPoint()));
        json = null;
        json = jParser.makeHttpRequest(updatePointURL, "POST", params);
    }
}
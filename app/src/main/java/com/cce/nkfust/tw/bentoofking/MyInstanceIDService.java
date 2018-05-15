package com.cce.nkfust.tw.bentoofking;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by John on 2018/5/15.
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {
    String token;
    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
    }
    public String getToken(){
        return token;
    }
}

package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditStoreActivity extends AppCompatActivity {

    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private Context context;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Button nextButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText time1EditText;
    private EditText time2EditText;
    private EditText time3EditText;
    private EditText time4EditText;
    private TextView infoContentTextView;
    private String storeInfoString = "0000000";
    private int hour=0, minute=0;
    private char[] bussinessTimeChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        bussinessTimeChar = new char[16];
        for(int i=0;i<bussinessTimeChar.length;i++)bussinessTimeChar[i]='0';
        setContentView(R.layout.activity_edit_store);
    }
}

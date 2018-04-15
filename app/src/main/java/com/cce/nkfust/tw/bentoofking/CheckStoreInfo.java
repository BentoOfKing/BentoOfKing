package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckStoreInfo extends AppCompatActivity {
    private Button reportButton;
    private ImageView storeIcon;
    private TextView storeName;
    private TextView storeEvaluation;
    private Button menuButton;
    private TextView storeAddress;
    private TextView storeEmail;
    private TextView storeParkInfo;
    private TextView storeFreeInfo;
    private EditText commentEditText;
    private Button sentCommentButton;
    private UserInfo storeInfoBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_check_storeinfo);
        UIconnect();
        Intent intent = getIntent();
        this.storeInfoBundle = (UserInfo) intent.getSerializableExtra("storeInfo");
        if(storeInfoBundle.getIdentity()==2)
            UpdateUI();
    }

    private void UpdateUI(){
        this.storeName.setText(storeInfoBundle.getStore().getStoreName());
        this.storeEvaluation.setText("*****");
        this.storeAddress.setText(storeInfoBundle.getStore().getAddress());
        this.storeEmail.setText(storeInfoBundle.getStore().getEmail());
        this.storeParkInfo.setText("後門有山豬戲水區");
        String storeInfoString = getStoreInfoString();
        this.storeFreeInfo.setText(storeInfoString);
    }

    private String getStoreInfoString(){
        String storeFreeinfoBool = storeInfoBundle.getStore().getInformation();
        String storeInfoString = "";
        if(storeFreeinfoBool.charAt(0)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="可以內用";
        }
        if(storeFreeinfoBool.charAt(1)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="冷氣開放";
        }
        if(storeFreeinfoBool.charAt(2)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="提供廁所";
        }
        if(storeFreeinfoBool.charAt(3)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="專屬停車位";
        }
        if(storeFreeinfoBool.charAt(4)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="飲料暢飲";
        }
        if(storeFreeinfoBool.charAt(5)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="免費湯品";
        }
        if(storeFreeinfoBool.charAt(6)=='1'){
            if(!storeInfoString.isEmpty())
                storeInfoString+="/";
            storeInfoString+="提供素食";
        }
        return storeInfoString;
    }


    private void UIconnect(){
        this.reportButton = findViewById(R.id.reportButton);
        this.storeIcon = findViewById(R.id.storeIcon);
        this.storeName = findViewById(R.id.storeName);
        this.storeEvaluation = findViewById(R.id.storeEvaluation);
        this.menuButton = findViewById(R.id.menuButton);
        this.storeAddress = findViewById(R.id.storeAddress);
        this.storeEmail = findViewById(R.id.storeEmail);
        this.storeParkInfo = findViewById(R.id.storeParkInfo);
        this.storeFreeInfo = findViewById(R.id.storeFreeInfo);
        this.commentEditText = findViewById(R.id.commentEditText);
        this.sentCommentButton = findViewById(R.id.sentCommentButton);
    }
}

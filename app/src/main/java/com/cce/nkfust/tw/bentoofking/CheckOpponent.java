package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckOpponent extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int GET_BITMAP = 69, UPDATE_UI = 70;
    private ImageView storeIcon;
    private TextView opponentName;
    private HandlerThread anotherThread;
    private UserInfo storeInfoBundle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Drawer drawer;
    private UserInfo userInfo;
    private Intent intent;
    private Database database;
    private MainThreadHandler mainHandler;
    private ImageView[] yourInfo;
    private ImageView[] opponentInfo;
    private TextView yourScore;
    private TextView opponentScore;
    private TextView yourPrice;
    private TextView yourBusiness1;
    private TextView yourBusiness2;
    private TextView opponentPrice;
    private TextView opponentBusiness1;
    private TextView opponentBusiness2;
    private Bitmap storePhotoBitmap;
    private Handler_A anotherHandler;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.check_opponent);
        InfoReceive();
        variableSetup();
        UIconnect();
        UIsetup();
        UIhandle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIupdate();
    }

    private void InfoReceive() {
        intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if(userInfo == null) userInfo = new UserInfo();
        this.storeInfoBundle = (UserInfo) intent.getSerializableExtra("storeInfo");
    }

    private void UIhandle(){

    }
    private void variableSetup(){
        mainHandler = new MainThreadHandler(Looper.getMainLooper());
        database = new Database();
        drawer = new Drawer();
        yourInfo = new ImageView[7];
        opponentInfo = new ImageView[7];
        anotherThread = new HandlerThread("anotherThread");
        anotherThread.start();
        CheckOpponent.this.anotherHandler = new Handler_A(CheckOpponent.this.anotherThread.getLooper());
    }
    private void UIsetup(){
        context = this;
        setSupportActionBar(toolbar);
        drawer.init(this,this.toolbar,drawerListView,drawerLayout,this.userInfo);
        drawer.setToolbarNavigation();
    }



    private void UIupdate(){
        anotherHandler.sendEmptyMessage(GET_BITMAP);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void updateStoreInfo(){
        String storeFreeinfoBool = storeInfoBundle.getStore().getInformation();
        String yourFreeInfoBool = userInfo.getStore().getInformation();
        if(storeFreeinfoBool.charAt(0)=='1')
            this.opponentInfo[0].setImageResource(R.drawable.store_info1_off);
        if(storeFreeinfoBool.charAt(1)=='1')
            this.opponentInfo[1].setImageResource(R.drawable.store_info2_off);
        if(storeFreeinfoBool.charAt(2)=='1')
            this.opponentInfo[2].setImageResource(R.drawable.store_info3_off);
        if(storeFreeinfoBool.charAt(3)=='1')
            this.opponentInfo[3].setImageResource(R.drawable.store_info4_off);
        if(storeFreeinfoBool.charAt(4)=='1')
            this.opponentInfo[4].setImageResource(R.drawable.store_info5_off);
        if(storeFreeinfoBool.charAt(5)=='1')
            this.opponentInfo[5].setImageResource(R.drawable.store_info6_off);
        if(storeFreeinfoBool.charAt(6)=='1')
            this.opponentInfo[6].setImageResource(R.drawable.store_info7_off);
        if(yourFreeInfoBool.charAt(0)=='1')
            this.yourInfo[0].setImageResource(R.drawable.store_info1_off);
        if(yourFreeInfoBool.charAt(1)=='1')
            this.yourInfo[1].setImageResource(R.drawable.store_info2_off);
        if(yourFreeInfoBool.charAt(2)=='1')
            this.yourInfo[2].setImageResource(R.drawable.store_info3_off);
        if(yourFreeInfoBool.charAt(3)=='1')
            this.yourInfo[3].setImageResource(R.drawable.store_info4_off);
        if(yourFreeInfoBool.charAt(4)=='1')
            this.yourInfo[4].setImageResource(R.drawable.store_info5_off);
        if(yourFreeInfoBool.charAt(5)=='1')
            this.yourInfo[5].setImageResource(R.drawable.store_info6_off);
        if(yourFreeInfoBool.charAt(6)=='1')
            this.yourInfo[6].setImageResource(R.drawable.store_info7_off);
    }

    private void updateStoreScore(){
        double storeScoreDouble = Double.valueOf(storeInfoBundle.getStore().getRank());
        int storeScoreInt = (int)storeScoreDouble;
        opponentScore.setText(String.valueOf(storeScoreInt));
        storeScoreDouble = Double.valueOf(userInfo.getStore().getRank());
        storeScoreInt = (int)storeScoreDouble;
        yourScore.setText(String.valueOf(storeScoreInt));
    }

    private void getStoreBusiness(){
        String storeBusinessTime = storeInfoBundle.getStore().getBusinessHours();
        String yourBusinessTime = userInfo.getStore().getBusinessHours();
        String printString = String.format("%s:%s~%s:%s",storeBusinessTime.substring(0,2),storeBusinessTime.substring(2,4),storeBusinessTime.substring(4,6),storeBusinessTime.substring(6,8));
        this.opponentBusiness1.setText(printString);
        printString = String.format("%s:%s~%s:%s",storeBusinessTime.substring(8,10),storeBusinessTime.substring(10,12),storeBusinessTime.substring(12,14),storeBusinessTime.substring(14,16));
        this.opponentBusiness2.setText(printString);
        printString = String.format("%s:%s~%s:%s",yourBusinessTime.substring(0,2),yourBusinessTime.substring(2,4),yourBusinessTime.substring(4,6),yourBusinessTime.substring(6,8));
        this.yourBusiness1.setText(printString);
        printString = String.format("%s:%s~%s:%s",yourBusinessTime.substring(8,10),yourBusinessTime.substring(10,12),yourBusinessTime.substring(12,14),yourBusinessTime.substring(14,16));
        this.yourBusiness2.setText(printString);
    }


    private void UIconnect(){
        this.storeIcon = findViewById(R.id.storePicture);
        this.toolbar = findViewById(R.id.toolbar);
        this.drawerListView = findViewById(R.id.drawerListView);
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.opponentName = findViewById(R.id.opponentName);
        this.yourScore = findViewById(R.id.yourScore);
        this.opponentScore = findViewById(R.id.opponentScore);
        this.yourPrice = findViewById(R.id.yourPrice);
        this.opponentPrice = findViewById(R.id.opponentPrice);
        this.yourInfo[0] = findViewById(R.id.yourInfo1);
        this.yourInfo[1] = findViewById(R.id.yourInfo2);
        this.yourInfo[2] = findViewById(R.id.yourInfo3);
        this.yourInfo[3] = findViewById(R.id.yourInfo4);
        this.yourInfo[4] = findViewById(R.id.yourInfo5);
        this.yourInfo[5] = findViewById(R.id.yourInfo6);
        this.yourInfo[6] = findViewById(R.id.yourInfo7);
        this.opponentInfo[0] = findViewById(R.id.opponentInfo1);
        this.opponentInfo[1] = findViewById(R.id.opponentInfo2);
        this.opponentInfo[2] = findViewById(R.id.opponentInfo3);
        this.opponentInfo[3] = findViewById(R.id.opponentInfo4);
        this.opponentInfo[4] = findViewById(R.id.opponentInfo5);
        this.opponentInfo[5] = findViewById(R.id.opponentInfo6);
        this.opponentInfo[6] = findViewById(R.id.opponentInfo7);
        this.yourBusiness1 = findViewById(R.id.yourBussiness1);
        this.yourBusiness2 = findViewById(R.id.yourBussiness2);
        this.opponentBusiness1 = findViewById(R.id.opponentBussiness1);
        this.opponentBusiness2 = findViewById(R.id.opponentBussiness2);
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    private class MainThreadHandler extends Handler {
        public MainThreadHandler() {
            super();
        }
        public MainThreadHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    CheckOpponent.this.toolbar.setTitle("對手比較");
                    CheckOpponent.this.opponentName.setText(storeInfoBundle.getStore().getStoreName());
                    CheckOpponent.this.opponentPrice.setText(storeInfoBundle.getStore().getPrice());
                    CheckOpponent.this.yourPrice.setText(userInfo.getStore().getPrice());
                    getStoreBusiness();
                    updateStoreInfo();
                    updateStoreScore();
                    storeIcon.setImageBitmap(storePhotoBitmap);
                    break;

            }
            super.handleMessage(msg);
        }
    }



    private class Handler_A extends Handler{
        public Handler_A(Looper looper){super(looper);}

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_BITMAP:
                    storePhotoBitmap = getBitmapFromURL(storeInfoBundle.getStore().getFirstPhoto());
                    mainHandler.sendEmptyMessage(UPDATE_UI);
                    break;
            }
        }
    }



    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}

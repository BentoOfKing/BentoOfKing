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
    private UserInfo storeInfoBundle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Drawer drawer;
    private UserInfo userInfo;
    private Intent intent;
    private Database database;
    private HandlerThread commentThread;
    private MainThreadHandler mainHandler;
    private ImageView storeInfo1;
    private ImageView storeInfo2;
    private ImageView storeInfo3;
    private ImageView storeInfo4;
    private ImageView storeInfo5;
    private ImageView storeInfo6;
    private ImageView storeInfo7;
    private ImageView[] starArray;
    private TextView storeAveragePrice;
    private TextView storeBusiness;
    private Bitmap storePhotoBitmap;
    private Handler_A anotherHandler;

    ;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_2_check_storeinfonew);
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
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CheckOpponent.this.anotherHandler = new Handler_A(CheckOpponent.this.commentThread.getLooper());
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
        if(storeFreeinfoBool.charAt(0)=='1')
            storeInfo1.setImageResource(R.drawable.store_info1_on);
        if(storeFreeinfoBool.charAt(1)=='1')
            storeInfo2.setImageResource(R.drawable.store_info2_on);
        if(storeFreeinfoBool.charAt(2)=='1')
            storeInfo3.setImageResource(R.drawable.store_info3_on);
        if(storeFreeinfoBool.charAt(3)=='1')
            storeInfo4.setImageResource(R.drawable.store_info4_on);
        if(storeFreeinfoBool.charAt(4)=='1')
            storeInfo5.setImageResource(R.drawable.store_info5_on);
        if(storeFreeinfoBool.charAt(5)=='1')
            storeInfo6.setImageResource(R.drawable.store_info6_on);
        if(storeFreeinfoBool.charAt(6)=='1')
            storeInfo7.setImageResource(R.drawable.store_info7_on);
    }

    private void updateStoreScore(){
        double storeScoreDouble = Double.valueOf(storeInfoBundle.getStore().getRank());
        int storeScoreInt = (int)storeScoreDouble;
    }

    private String getStoreBusiness(){
        String storeBusinessTime = storeInfoBundle.getStore().getBusinessHours();
        String returnString = "";
        if(storeBusinessTime.length()>=8)
            returnString+=String.format("%s:%s ~ %s:%s     ",storeBusinessTime.substring(0,2),storeBusinessTime.substring(2,4),storeBusinessTime.substring(4,6),storeBusinessTime.substring(6,8));
        if(storeBusinessTime.length()>=16)
            returnString+=String.format("%s:%s ~ %s:%s",storeBusinessTime.substring(8,10),storeBusinessTime.substring(10,12),storeBusinessTime.substring(12,14),storeBusinessTime.substring(14,16));
        return returnString;
    }


    private void UIconnect(){
        this.storeIcon = findViewById(R.id.storePicture);
        this.toolbar = findViewById(R.id.toolbar);
        this.drawerListView = findViewById(R.id.drawerListView);
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.storeInfo1 = findViewById(R.id.storeInfo1);
        this.storeInfo2 = findViewById(R.id.storeInfo2);
        this.storeInfo3 = findViewById(R.id.storeInfo3);
        this.storeInfo4 = findViewById(R.id.storeInfo4);
        this.storeInfo5 = findViewById(R.id.storeInfo5);
        this.storeInfo6 = findViewById(R.id.storeInfo6);
        this.starArray[0] = findViewById(R.id.storeScore1);
        this.starArray[1] = findViewById(R.id.storeScore2);
        this.starArray[2] = findViewById(R.id.storeScore3);
        this.starArray[3] = findViewById(R.id.storeScore4);
        this.starArray[4] = findViewById(R.id.storeScore5);
        this.storeAveragePrice = findViewById(R.id.storeAveragePrice);
        this.storeBusiness = findViewById(R.id.storeBusiness);
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
                    CheckOpponent.this.toolbar.setTitle(storeInfoBundle.getStore().getStoreName());
                    CheckOpponent.this.storeAveragePrice.setText(storeInfoBundle.getStore().getPrice());
                    CheckOpponent.this.storeBusiness.setText(getStoreBusiness());
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

    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bag = new Bundle();
        bag.putSerializable("USER_INFO",userInfo);
        intent.putExtras(bag);
        CheckOpponent.this.setResult(RESULT_OK,intent);
        super.finish();
    }
}

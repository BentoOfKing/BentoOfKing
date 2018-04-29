package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckStoreInfo extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int NEW_COMMENT = 63;
    private static final int ACTION_DIALOG = 71;
    private static final int SENT_COMMENT = 64;
    private String[] commentScoreArray = {"☆☆☆☆☆","☆☆☆☆","☆☆☆","☆☆","☆"};
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
    private ListView commentListView;
    private Button sentCommentButton;
    private UserInfo storeInfoBundle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private UserInfo userInfo;
    private Intent intent;
    private Database database;
    private Database databaseForComment;
    private Comment[] commentlist;
    private ArrayList<Comment> commentArrayList;
    private CommentListViewBaseAdapter adapter;
    private Thread getArrayList;
    private HandlerThread sentCommentThread;
    private CommentHandler sentCmtHandler;
    private Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        createMainHandler();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_2_check_storeinfo);
        intent = getIntent();
        getUserInfo();
        this.storeInfoBundle = (UserInfo) intent.getSerializableExtra("storeInfo");
        ConnectDataBaseThread();
        UIconnect();
        if(storeInfoBundle.getIdentity()==2)
            UpdateUI();

    }

    private void createMainHandler(){
        mainHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case NEW_COMMENT:
                        ConnectDataBaseThread();
                        UpdateUI();
                        break;
                }
            }
        };
    }

    private void UpdateUI(){
        this.storeName.setText(storeInfoBundle.getStore().getStoreName());
        this.storeEvaluation.setText("*****");
        this.storeAddress.setText(storeInfoBundle.getStore().getAddress());
        this.storeEmail.setText(storeInfoBundle.getStore().getEmail());
        this.storeParkInfo.setText("後門有山豬戲水區");
        final String storeInfoString = getStoreInfoString();
        this.storeFreeInfo.setText(storeInfoString);
        Thread bitMapthread = new Thread (new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getBitmapFromURL(storeInfoBundle.getStore().getPhoto());
                CheckStoreInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        storeIcon.setImageBitmap(bitmap);
                    }
                });
            }
        });
        bitMapthread.start();
        try {
            bitMapthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            getArrayList.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CommentListViewBaseAdapter adapter = new CommentListViewBaseAdapter(commentArrayList,inflater);
        commentListView.setAdapter(adapter);
        SentComment sentCommentListener = new SentComment();
        sentCommentButton.setOnClickListener(sentCommentListener);
        adapter.notifyDataSetChanged();
        DoSomethingToComment commentListListener = new DoSomethingToComment();
        commentListView.setOnItemClickListener(commentListListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_DIALOG){
            if(resultCode == RESULT_OK){
                UpdateCommment();
            }
        }
    }

    public void UpdateCommment(){
        sentCmtHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHandler.sendEmptyMessage(NEW_COMMENT);
            }
        }, 500);
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
        this.toolbar = findViewById(R.id.toolbar);
        this.drawerListView = findViewById(R.id.drawerListView);
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.commentListView = findViewById(R.id.Dejavu);
        newDrawer();
    }

    private void newDrawer(){
        Drawer drawer = new Drawer();
        drawer.init(this,this.toolbar,drawerListView,drawerLayout,this.userInfo);
        drawer.setToolbarNavigation();
    }

    private void ConnectDataBaseThread(){
        ConnectDatabase connectDatabase = new ConnectDatabase();
        getArrayList = new Thread(connectDatabase);
        getArrayList.start();
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    private void getUserInfo(){
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if(userInfo == null) userInfo = new UserInfo();
    }

    public class ConnectDatabase implements Runnable{
        @Override
        public void run() {
            CheckStoreInfo.this.database = new Database();
            commentArrayList = new ArrayList<Comment>();
            commentlist = database.getComment("Store",CheckStoreInfo.this.storeInfoBundle.getStore().getID());
            for(int i=0;i<commentlist.length;i++){
                commentArrayList.add(commentlist[i]);
                Member member = database.GetSingleMember(commentlist[i].getMember());
                commentArrayList.get(i).setMemberNickName(member.getNickname());
            }
        }
    }

    private class SentComment implements View.OnClickListener{
        AlertDialog.Builder askForCommentScore = new AlertDialog.Builder(CheckStoreInfo.this);
        @Override
        public void onClick(View v) {
            askForCommentScore.setTitle("請給這個店家一個評分");
            askForCommentScore.setItems(commentScoreArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CheckStoreInfo.this.sentCommentThread = new HandlerThread("SentComment");
                    CheckStoreInfo.this.sentCommentThread.start();
                    CheckStoreInfo.this.sentCmtHandler = new CommentHandler(CheckStoreInfo.this.sentCommentThread.getLooper());
                    Message msg = new Message();
                    msg.what = SENT_COMMENT;
                    Bundle commentInfo = new Bundle();
                    commentInfo.putString("Score",commentScoreArray[which]);
                    msg.setData(commentInfo);
                    sentCmtHandler.sendMessage(msg);
                }
            });
            AlertDialog dialog = askForCommentScore.create();
            dialog.show();
        }
    }

    private class CommentHandler extends Handler{
        public CommentHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case SENT_COMMENT:
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
                    String timeString = formatter.format(curDate);
                    String text = "";
                    text = CheckStoreInfo.this.commentEditText.getText().toString();
                    Comment newComment = new Comment("0",userInfo.getMember().getEmail()/*"john8654john@gmail.com"*/,CheckStoreInfo.this.storeInfoBundle.getStore().getID(), msg.getData().getString("Score"),"123",timeString,"", text);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    CheckStoreInfo.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CheckStoreInfo.this.commentEditText.setText("") ;
                        }
                    });
                    databaseForComment = new Database();
                    databaseForComment.addComment(newComment);
                    sentCmtHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mainHandler.sendEmptyMessage(NEW_COMMENT);
                        }
                    }, 500);
                    break;
            }
            super.handleMessage(msg);
        }
    }



    public class SentCommentToDataBase implements Runnable{
        @Override
        public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
            String timeString = formatter.format(curDate);
            String text = "";
            text = CheckStoreInfo.this.commentEditText.getText().toString();
            Comment newComment = new Comment("0",userInfo.getMember().getEmail()/*"john8654john@gmail.com"*/,CheckStoreInfo.this.storeInfoBundle.getStore().getID(),"*****","123",timeString,"", text);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            CheckStoreInfo.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CheckStoreInfo.this.commentEditText.setText("") ;
                }
            });
            databaseForComment = new Database();
            databaseForComment.addComment(newComment);
            sentCmtHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainHandler.sendEmptyMessage(NEW_COMMENT);
                }
            }, 500);
        }
    }

    private class DoSomethingToComment implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Comment selectComment = commentArrayList.get(position);
            Intent showDialog = new Intent(CheckStoreInfo.this,EditDataBaseCommentDialog.class);
            Bundle bag = new Bundle();
            bag.putSerializable("SelectComment",selectComment);
            bag.putSerializable(passUserInfo,userInfo);
            showDialog.putExtras(bag);
            startActivityForResult(showDialog,ACTION_DIALOG);
        }
    }




}

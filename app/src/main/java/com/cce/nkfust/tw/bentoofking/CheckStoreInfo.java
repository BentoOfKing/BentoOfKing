package com.cce.nkfust.tw.bentoofking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckStoreInfo extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int UPDATE_COMMENT = 63;
    private static final int ACTION_DIALOG = 71;
    private static final int SENT_COMMENT = 64;
    private static final int DELETE_COMMENT = 65;
    private static final int EDIT_COMMENT = 66;
    private static final int EDIT_REPLY = 67;
    private String[] commentScoreArray = {"☆☆☆☆☆","☆☆☆☆","☆☆☆","☆☆","☆"};
    private int commentMode;
    private Comment editComment;
    private Button reportButton;
    private ImageView storeIcon;
    private TextView storeName;
    private TextView storeEvaluation;
    private ImageButton menuButton;
    private TextView storeAddress;
    private TextView storeEmail;
    private TextView storeParkInfo;
    private TextView storeFreeInfo;
    private EditText commentEditText;
    private ListView commentListView;
    private ImageButton sentCommentButton;
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
    private HandlerThread commentThread;
    private CommentHandler CmtHandler;
    private Handler mainHandler;
    private Snackbar snackbar;
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
    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        createMainHandler();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_2_check_storeinfonew);
        variableSetup();
        intent = getIntent();
        getUserInfo();
        this.storeInfoBundle = (UserInfo) intent.getSerializableExtra("storeInfo");
        ConnectDataBaseThread();
        UIconnect();
        if(storeInfoBundle.getIdentity()==2)
            UpdateUI();
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.setClass(CheckStoreInfo.this,OrderActivity.class);
                intent.putExtra(passStoreInfo,storeInfoBundle.getStore());
                intent.putExtra(passUserInfo,userInfo);
                startActivity(intent);
            }
        });

    }

    private void variableSetup(){
        starArray = new ImageView[5];
    }

    private void createMainHandler(){
        mainHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case UPDATE_COMMENT:
                        ConnectDataBaseThread();
                        UpdateUI();
                        break;
                }
            }
        };
    }

    private void UpdateUI(){
        this.storeName.setText(storeInfoBundle.getStore().getStoreName());
        this.storeAddress.setText(storeInfoBundle.getStore().getAddress());
        this.storeAveragePrice.setText(storeInfoBundle.getStore().getPrice());
        this.storeBusiness.setText(getStoreBusiness());
        updateStoreInfo();
        updateStoreScore();/*
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
        }*/
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
        commentMode = SENT_COMMENT;
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
        CmtHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainHandler.sendEmptyMessage(UPDATE_COMMENT);
            }
        }, 500);
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
        for(int i=0;i<storeScoreInt;i++)
            starArray[i].setImageResource(R.drawable.star_on);
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
        this.storeName = findViewById(R.id.storeNameTextView);
        this.menuButton = findViewById(R.id.storeMenuButton);
        this.storeAddress = findViewById(R.id.storeAddress);
        this.commentEditText = findViewById(R.id.commentEditText);
        this.sentCommentButton = findViewById(R.id.sendComment);
        this.toolbar = findViewById(R.id.toolbar);
        this.drawerListView = findViewById(R.id.drawerListView);
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.commentListView = findViewById(R.id.commentListView);
        this.storeInfo1 = findViewById(R.id.storeInfo1);
        this.storeInfo2 = findViewById(R.id.storeInfo2);
        this.storeInfo3 = findViewById(R.id.storeInfo3);
        this.storeInfo4 = findViewById(R.id.storeInfo4);
        this.storeInfo5 = findViewById(R.id.storeInfo5);
        this.storeInfo6 = findViewById(R.id.storeInfo6);
        this.storeInfo7 = findViewById(R.id.storeInfo7);
        this.starArray[0] = findViewById(R.id.storeScore1);
        this.starArray[1] = findViewById(R.id.storeScore2);
        this.starArray[2] = findViewById(R.id.storeScore3);
        this.starArray[3] = findViewById(R.id.storeScore4);
        this.starArray[4] = findViewById(R.id.storeScore5);
        this.storeAveragePrice = findViewById(R.id.storeAveragePrice);
        this.storeBusiness = findViewById(R.id.storeBusiness);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(commentMode == SENT_COMMENT) {
                if (userInfo.getIdentity() == 0) {
                    snackbar = Snackbar.make(CheckStoreInfo.this.findViewById(R.id.contentView), "訪客無法進行留言", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                } else if (userInfo.getIdentity() == 2) {
                    snackbar = Snackbar.make(CheckStoreInfo.this.findViewById(R.id.contentView), "店家帳號無法進行留言", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                askForCommentScore.setTitle("請給這個店家一個評分");
                askForCommentScore.setItems(commentScoreArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckStoreInfo.this.commentThread = new HandlerThread("SentComment");
                        CheckStoreInfo.this.commentThread.start();
                        CheckStoreInfo.this.CmtHandler = new CommentHandler(CheckStoreInfo.this.commentThread.getLooper());
                        Message msg = new Message();
                        msg.what = SENT_COMMENT;
                        Bundle commentInfo = new Bundle();
                        commentInfo.putString("Score", String.valueOf(5 - which));
                        commentInfo.putString("CommentID", "0");
                        commentInfo.putString("Note",CheckStoreInfo.this.commentEditText.getText().toString());
                        msg.setData(commentInfo);
                        CmtHandler.sendMessage(msg);
                        commentEditText.setText("");
                    }
                });
                AlertDialog dialog = askForCommentScore.create();
                dialog.show();
            }else if(commentMode == EDIT_COMMENT){
                CheckStoreInfo.this.commentThread = new HandlerThread("EditComment");
                CheckStoreInfo.this.commentThread.start();
                CheckStoreInfo.this.CmtHandler = new CommentHandler(CheckStoreInfo.this.commentThread.getLooper());
                editComment.putNote(commentEditText.getText().toString());
                commentEditText.setText("");
                Message msg = new Message();
                msg.what = EDIT_COMMENT;
                Bundle commentInfo = new Bundle();
                commentInfo.putSerializable("CommentEdit",editComment);
                msg.setData(commentInfo);
                CmtHandler.sendMessage(msg);
            }else if(commentMode == EDIT_REPLY){
                CheckStoreInfo.this.commentThread = new HandlerThread("EditComment");
                CheckStoreInfo.this.commentThread.start();
                CheckStoreInfo.this.CmtHandler = new CommentHandler(CheckStoreInfo.this.commentThread.getLooper());
                editComment.putReply(commentEditText.getText().toString());
                Message msg = new Message();
                msg.what = EDIT_REPLY;
                Bundle commentInfo = new Bundle();
                commentInfo.putSerializable("CommentEdit",editComment);
                msg.setData(commentInfo);
                CmtHandler.sendMessage(msg);
                commentEditText.setText("");
            }
            commentMode = SENT_COMMENT;
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
                    Comment newComment = new Comment(msg.getData().getString("CommentID"),userInfo.getMember().getEmail()/*"john8654john@gmail.com"*/,CheckStoreInfo.this.storeInfoBundle.getStore().getID(), msg.getData().getString("Score"),"123",timeString,"", msg.getData().getString("Note"));
                    databaseForComment = new Database();
                    databaseForComment.addComment(newComment);
                    break;
                case DELETE_COMMENT:
                    databaseForComment = new Database();
                    databaseForComment.deleteComment(msg.getData().getString("CommentID"));
                    break;
                case EDIT_COMMENT:
                    databaseForComment = new Database();
                    databaseForComment.updateComment((Comment)msg.getData().getSerializable("CommentEdit"));
                    break;
                case EDIT_REPLY:
                    databaseForComment = new Database();
                    databaseForComment.updateComment((Comment)msg.getData().getSerializable("CommentEdit"));
                    break;
            }
            CmtHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainHandler.sendEmptyMessage(UPDATE_COMMENT);
                }
            }, 500);
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
            CmtHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainHandler.sendEmptyMessage(UPDATE_COMMENT);
                }
            }, 500);
        }
    }

    private class DoSomethingToComment implements AdapterView.OnItemClickListener{
        AlertDialog.Builder askForCommentScore = new AlertDialog.Builder(CheckStoreInfo.this);
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Comment selectComment = commentArrayList.get(position);
            final ArrayList<String> selections = new ArrayList<String>();
            if(userInfo.getIdentity()==3){
                selections.add("回覆");
                selections.add("編輯");
                selections.add("檢舉");
                selections.add("刪除");
            }else
                selections.add("檢舉");
            if(userInfo.getIdentity()==2&&userInfo.getStore().getID().equals(storeInfoBundle.getStore().getID()))
                selections.add("回覆");
            if(userInfo.getIdentity()==1&&userInfo.getMember().getEmail().equals(selectComment.getMember())){
                selections.add("刪除");
                selections.add("編輯");
            }
            askForCommentScore.setTitle(selectComment.getMemberNickName()+"的評論");
            askForCommentScore.setItems( selections.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(selections.get(which).equals("刪除")){
                        CheckStoreInfo.this.commentThread = new HandlerThread("DeleteComment");
                        CheckStoreInfo.this.commentThread.start();
                        CheckStoreInfo.this.CmtHandler = new CommentHandler(CheckStoreInfo.this.commentThread.getLooper());
                        Message msg = new Message();
                        msg.what = DELETE_COMMENT;
                        Bundle bag = new Bundle();
                        bag.putString("CommentID",selectComment.getID());
                        msg.setData(bag);
                        CmtHandler.sendMessage(msg);
                        CmtHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainHandler.sendEmptyMessage(UPDATE_COMMENT);
                            }
                        }, 500);
                    }else if(selections.get(which).equals("編輯")){
                        commentMode = EDIT_COMMENT;
                        editComment = new Comment();
                        editComment.putID(selectComment.getID());
                        editComment.putReply(selectComment.getReply());
                        commentEditText.setText(selectComment.getNote());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                    }else if(selections.get(which).equals("回覆")){
                        commentMode = EDIT_REPLY;
                        editComment = new Comment();
                        editComment.putID(selectComment.getID());
                        editComment.putNote(selectComment.getNote());
                        commentEditText.setText(selectComment.getReply());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });
            AlertDialog dialog = askForCommentScore.create();
            dialog.show();

        }
    }




}

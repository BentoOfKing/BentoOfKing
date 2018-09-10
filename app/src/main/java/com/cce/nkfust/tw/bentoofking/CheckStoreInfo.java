package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class CheckStoreInfo extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int ADD_APPEAL = 3;
    private static final int ADD_APPEAL_FAIL = 4;
    private static final int UPDATE_MEMBER = 1;
    private static final int UPDATE_MEMBER_FAIL = 2;
    private static final int UPDATE_COMMENT = 63;
    private static final int UPDATE_ARRAYLIST = 68;
    private static final int UPDATE_UI = 71;
    private static final int SENT_COMMENT = 64;
    private static final int DELETE_COMMENT = 65;
    private static final int EDIT_COMMENT = 66;
    private static final int EDIT_REPLY = 67;
    private static final int GET_BITMAP = 69;
    private static final int LAYOUT_WEIGHT_31 = 104,LAYOUT_WEIGHT_ADDRESS = 48,LAYOUT_WEIGHT_BUSINESS = 48,LAYOUT_WEIGHT_LISTVIEW = 108;
    private String[] commentScoreArray = {"☆☆☆☆☆","☆☆☆☆","☆☆☆","☆☆","☆"};
    private LinearLayout store31Layout;
    private LinearLayout storeAddressLayout;
    private LinearLayout storeBusinessLayout;
    private NestedScrollView nestedScrollLayout;
    private int commentMode;
    private Comment editComment;
    private Button reportButton;
    //private ImageView storeIcon;
    private TextView storeName;
    private TextView storeEvaluation;
    private ImageButton menuButton;
    private TextView storeAddress;
    private TextView storeEmail;
    private TextView storeParkInfo;
    private TextView storeFreeInfo;
    private EditText commentEditText;
    private ImageButton sentCommentButton;
    private UserInfo storeInfoBundle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Drawer drawer;
    private UserInfo userInfo;
    private Intent intent;
    private Database database;
    private Database databaseForComment;
    private Comment[] commentlist;
    private ArrayList<Comment> commentArrayList;
    private Thread getArrayList;
    private HandlerThread commentThread;
    private CommentHandler CmtHandler;
    private MainThreadHandler mainHandler;
    private Snackbar snackbar;
    private ImageView storeInfo1;
    private ImageView storeInfo2;
    private ImageView storeInfo3;
    private ImageView storeInfo4;
    private ImageView storeInfo5;
    private ImageView storeInfo6;
    private ImageView storeInfo7;
    private ViewPager storeIcon;
    private ImageView[] starArray;
    private TextView storeAveragePrice;
    private TextView storeBusiness;
    private ArrayList<StoreImagePageView> pageList;
    private Bitmap storePhotoBitmap;
    private ArrayList<Bitmap> storePhotoBitmapArray;
    private ArrayList<String> storePhotoURLArray;
    private Handler_A anotherHandler;
    private RecyclerView commentRecyclerView;
    private LinearLayoutManager recyclerManager;
    private CommentListViewRecyclerAdapter recyclerAdapter;
    private ConstraintLayout storePictureLayout;
    private int imageHeight;
    private ImageButton storeScroll;
    private ConstraintLayout.LayoutParams params;
    private Context context;
    private ArrayList<String> myFavoriteTokens;
    private StorePhotoPagerAdapter storePhotoAdapter;
    private Boolean isFavorite;

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
        if(userInfo.getIdentity() == 1){
            String[] Tokens = userInfo.getMember().getFavorite().split(",");
            myFavoriteTokens = new ArrayList<String>();
            isFavorite = false;
            for(int i=0;i<Tokens.length;i++){
                myFavoriteTokens.add(Tokens[i]);
                if(Tokens[i].equals(storeInfoBundle.getStore().getID()))
                    isFavorite = true;
            }
        }
    }

    private void UIhandle(){
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInfo.getIdentity() == 0) {
                    snackbar = Snackbar.make(CheckStoreInfo.this.findViewById(R.id.contentView), "訪客無法使用菜單功能", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }else if (userInfo.getIdentity() == 1 && userInfo.getMember().getState().equals("1")) {
                    snackbar = Snackbar.make(CheckStoreInfo.this.findViewById(R.id.contentView), "停權會員無法使用菜單功能", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }else if (userInfo.getIdentity() == 2) {
                    snackbar = Snackbar.make(CheckStoreInfo.this.findViewById(R.id.contentView), "店家帳號無法使用菜單功能", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                intent.setClass(CheckStoreInfo.this,OrderActivity.class);
                intent.putExtra(passStoreInfo,storeInfoBundle.getStore());
                intent.putExtra(passUserInfo,userInfo);
                startActivity(intent);
            }
        });
        sentCommentButton.setOnClickListener(new SentComment());

    }
    private void variableSetup(){
        mainHandler = new MainThreadHandler(Looper.getMainLooper());
        starArray = new ImageView[5];
        database = new Database();
        drawer = new Drawer();
        storePhotoBitmapArray = new ArrayList<Bitmap>();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        commentArrayList = new ArrayList<Comment>();
        recyclerAdapter = new CommentListViewRecyclerAdapter(commentArrayList,storeInfoBundle.getStore().getStoreName());
        commentMode = SENT_COMMENT;
        databaseForComment = new Database();
        pageList = new ArrayList<StoreImagePageView>();
        storePhotoAdapter = new StorePhotoPagerAdapter(pageList);
        CheckStoreInfo.this.recyclerManager = new LinearLayoutManager(CheckStoreInfo.this);
        recyclerManager.setSmoothScrollbarEnabled(true);
        recyclerManager.setAutoMeasureEnabled(true);
        recyclerManager.setOrientation(LinearLayoutManager.VERTICAL);
        CheckStoreInfo.this.commentThread = new HandlerThread("CommentThread");
        CheckStoreInfo.this.commentThread.start();
        CheckStoreInfo.this.CmtHandler = new CommentHandler(CheckStoreInfo.this.commentThread.getLooper());
        CheckStoreInfo.this.anotherHandler = new Handler_A(CheckStoreInfo.this.commentThread.getLooper());
    }
    private void UIsetup(){
        context = this;
        commentRecyclerView.setLayoutManager(recyclerManager);
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setNestedScrollingEnabled(false);
        commentRecyclerView.setAdapter(recyclerAdapter);
        commentRecyclerView.setFocusable(false);
        commentRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        storeIcon.setAdapter(storePhotoAdapter);/*
        storeIcon.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) { if(state>0) storeIcon.getAdapter().notifyDataSetChanged(); }
        });*/
        params = (ConstraintLayout.LayoutParams)storeIcon.getLayoutParams();
        recyclerAdapter.setOnItemClickListener(new RecyclerListItemHandler());
        storeScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHandler.sendEmptyMessage(998);
            }
        });
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            Intent intent;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String favorite;
                switch (item.getItemId()) {
                    case R.id.item1:
                        if (userInfo.getIdentity() == 1) {//我的最愛
                            if(isFavorite) {
                                favorite = new String("");
                                for(int i=0 ,size = myFavoriteTokens.size();i<size;i++) {
                                    if (myFavoriteTokens.get(i).equals(storeInfoBundle.getStore().getID())){
                                        myFavoriteTokens.remove(i);
                                        i--;
                                        size--;
                                    }else {
                                        favorite += myFavoriteTokens.get(i) + ",";
                                    }
                                }
                                    userInfo.getMember().putFavorite(favorite);
                                    Thread t = new Thread(new UpdateMember());
                                    t.start();
                                }else {
                                if (userInfo.getMember().getFavorite().length() > 512) {
                                    Toast.makeText(context, getResources().getString(R.string.favoriteFull), Toast.LENGTH_SHORT).show();
                                } else {
                                    favorite = new String("");
                                    favorite += userInfo.getMember().getFavorite() + storeInfoBundle.getStore().getID() + ",";
                                    userInfo.getMember().putFavorite(favorite);
                                    Thread t = new Thread(new UpdateMember());
                                    t.start();
                                }
                            }
                        } else if (userInfo.getIdentity() == 3) {//編輯店家
                            intent = new Intent();
                            intent.setClass(context, PreviewStoreInfoActivity.class);
                            intent.putExtra(passUserInfo, userInfo);
                            intent.putExtra(passStoreInfo, storeInfoBundle.getStore());
                            startActivity(intent);
                        }
                        break;
                    case R.id.item2:
                        if (userInfo.getIdentity() == 1) {//錯誤回報
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View view = inflater.inflate(R.layout.alertdialog_report, null);
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                            builder.setTitle(getResources().getString(R.string.errorReport));
                            builder.setView(view);
                            final EditText titleEditText = view.findViewById(R.id.titleEditText);
                            final EditText contentEditText = view.findViewById(R.id.contentEditText);
                            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(getResources().getString(R.string.check),null);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                Appeal appeal;
                                @Override
                                public void onClick(View view) {
                                    if(titleEditText.getText().toString().equals("")){
                                        Toast.makeText(context, getResources().getString(R.string.pleaseEnterTitle), Toast.LENGTH_SHORT).show();
                                    }else if(contentEditText.getText().toString().equals("")){
                                        Toast.makeText(context, getResources().getString(R.string.pleaseEnterContent), Toast.LENGTH_SHORT).show();
                                    }else{
                                        class AddAppeal implements Runnable{
                                            @Override
                                            public void run() {
                                                Database d = new Database();
                                                if(d.AddAppeal(appeal).equals("Successful.")){
                                                    mainHandler.sendEmptyMessage(ADD_APPEAL);
                                                }else{
                                                    mainHandler.sendEmptyMessage(ADD_APPEAL_FAIL);
                                                }
                                            }
                                        }
                                        appeal = new Appeal();
                                        appeal.putDeclarant(userInfo.getMember().getEmail());
                                        appeal.putAppealed(storeInfoBundle.getStore().getID());
                                        appeal.putTitle(titleEditText.getText().toString());
                                        appeal.putContent(contentEditText.getText().toString());
                                        appeal.putType("2");
                                        Thread t = new Thread(new AddAppeal());
                                        t.start();
                                        alertDialog.dismiss();
                                    }
                                }
                            });
                        } else if (userInfo.getIdentity() == 3) {//編輯菜單
                            intent = new Intent();
                            intent.setClass(context, EditExistedMenuActivity.class);
                            intent.putExtra(passUserInfo, userInfo);
                            intent.putExtra(passStoreInfo, storeInfoBundle.getStore());
                            startActivity(intent);
                        }
                        break;
                    case R.id.item3:
                        if (userInfo.getIdentity() == 1) {//支付點數
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View view = inflater.inflate(R.layout.alertdialog_stored_point, null);
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                            builder.setTitle(getResources().getString(R.string.payPoint));
                            builder.setView(view);
                            final EditText pointEditText = view.findViewById(R.id.pointEditText);
                            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(getResources().getString(R.string.check), null);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (pointEditText.getText().toString().equals("")) {
                                        Toast.makeText(context, getResources().getString(R.string.pleaseEnterPoint), Toast.LENGTH_SHORT).show();
                                    } else {
                                        class AddPoint implements Runnable {
                                            @Override
                                            public void run() {
                                                Database d = new Database();
                                                int point = Integer.parseInt(storeInfoBundle.getStore().getPoint());
                                                point += Integer.parseInt(pointEditText.getText().toString());
                                                storeInfoBundle.getStore().putPoint(Integer.toString(point));
                                                d.UpdatePoint(storeInfoBundle.getStore());
                                            }
                                        }
                                        Thread t = new Thread(new AddPoint());
                                        t.start();
                                        alertDialog.dismiss();
                                    }
                                }
                            });
                        }else if (userInfo.getIdentity() == 3) {//編輯照片
                            intent = new Intent();
                            intent.setClass(context, EditExistedPhotoActivity.class);
                            intent.putExtra(passUserInfo, userInfo);
                            intent.putExtra(passStoreInfo, storeInfoBundle.getStore());
                            startActivity(intent);
                        }
                        break;
                    case R.id.item4:
                        if (userInfo.getIdentity() == 3) {//儲值點數
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View view = inflater.inflate(R.layout.alertdialog_stored_point, null);
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                            builder.setTitle(getResources().getString(R.string.storedPoint));
                            builder.setView(view);
                            final EditText pointEditText = view.findViewById(R.id.pointEditText);
                            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(getResources().getString(R.string.check),null);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(pointEditText.getText().toString().equals("")){
                                        Toast.makeText(context, getResources().getString(R.string.pleaseEnterPoint), Toast.LENGTH_SHORT).show();
                                    }else{
                                        class AddPoint implements Runnable{
                                            @Override
                                            public void run() {
                                                Database d = new Database();
                                                int point = Integer.parseInt(storeInfoBundle.getStore().getPoint());
                                                point += Integer.parseInt(pointEditText.getText().toString());
                                                storeInfoBundle.getStore().putPoint(Integer.toString(point));
                                                d.UpdatePoint(storeInfoBundle.getStore());
                                            }
                                        }
                                        Thread t = new Thread(new AddPoint());
                                        t.start();
                                        alertDialog.dismiss();
                                    }
                                }
                            });
                        }
                        break;
                }
                return false;
            }
        });
        drawer.init(this,this.toolbar,drawerListView,drawerLayout,this.userInfo);
        drawer.setToolbarNavigation();

    }

    public class UpdateMember implements Runnable{

        @Override
        public void run() {
            Database d = new Database();
            if(d.UpdateMember(userInfo.getMember()).equals("Successful.")) {
                mainHandler.sendEmptyMessage(UPDATE_MEMBER);
            }else{
                mainHandler.sendEmptyMessage(UPDATE_MEMBER_FAIL);
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_more, menu);
        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);
        MenuItem item4 = menu.findItem(R.id.item4);
        if(userInfo.getIdentity()==1){
            if(isFavorite){
                item1.setTitle(getResources().getString(R.string.removeFavorite));
            }else{
                item1.setTitle(getResources().getString(R.string.addFavorite));
            }
            item2.setTitle(getResources().getString(R.string.errorReport));
            item3.setTitle(getResources().getString(R.string.payPoint));
            item4.setVisible(false);
        }else if(userInfo.getIdentity()==2 || userInfo.getIdentity()==0 || userInfo.getIdentity()==4){
            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);
            item4.setVisible(false);
        }else if(userInfo.getIdentity()==3){
            item1.setTitle(getResources().getString(R.string.editStore));
            item2.setTitle(getResources().getString(R.string.editMenu));
            item3.setTitle(getResources().getString(R.string.editPhoto));
            item4.setTitle(getResources().getString(R.string.storedPoint));
        }
        return true;
    }
    private void UIupdate(){
        anotherHandler.sendEmptyMessage(GET_BITMAP);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainHandler.sendEmptyMessage(UPDATE_COMMENT);
        CmtHandler.sendEmptyMessage(UPDATE_ARRAYLIST);
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
        if(!storeBusinessTime.substring(0,8).equals("00000000"))
            returnString+=String.format("%s:%s ~ %s:%s     ",storeBusinessTime.substring(0,2),storeBusinessTime.substring(2,4),storeBusinessTime.substring(4,6),storeBusinessTime.substring(6,8));
        if(!storeBusinessTime.substring(8,16).equals("00000000"))
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
        this.store31Layout = findViewById(R.id.storeInfo31Layout);
        this.storeAddressLayout = findViewById(R.id.storeAddressLayout);
        this.storeBusinessLayout = findViewById(R.id.storeBusinessLayout);
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
        this.commentRecyclerView = findViewById(R.id.commentRecyclerView);
        this.storePictureLayout = findViewById(R.id.storePictureLayout);
        this.storeScroll = findViewById(R.id.scrollButton);
    }





    private void setLayoutExpandListView(){
        store31Layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0));
        storeAddressLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0));
        storeBusinessLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,0));
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
                case UPDATE_COMMENT:
                    recyclerAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_UI:
                    CheckStoreInfo.this.toolbar.setTitle(storeInfoBundle.getStore().getStoreName());
                    CheckStoreInfo.this.storeAddress.setText(storeInfoBundle.getStore().getAddress());
                    CheckStoreInfo.this.storeAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri mapUri = Uri.parse("google.navigation:q="+storeInfoBundle.getStore().getLatitude()+","+storeInfoBundle.getStore().getLongitude());
                            Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
                            startActivity(intent);
                        }
                    });
                    CheckStoreInfo.this.storeAveragePrice.setText(storeInfoBundle.getStore().getPrice());
                    CheckStoreInfo.this.storeBusiness.setText(getStoreBusiness());
                    updateStoreInfo();
                    updateStoreScore();
                    CheckStoreInfo.this.params.height = getScreenWidth()*9/16;
                    storeIcon.setLayoutParams(CheckStoreInfo.this.params);
                    storeIcon.getAdapter().notifyDataSetChanged();
                    recyclerAdapter.notifyDataSetChanged();
                    break;
                case 998:
                    ViewGroup.LayoutParams params = storePictureLayout.getLayoutParams();
                    if(storePictureLayout.getMeasuredHeight()>0) {
                        imageHeight = storePictureLayout.getMeasuredHeight();
                        params.height = 0;
                        storePictureLayout.setLayoutParams(params);
                        storeScroll.setImageResource(R.drawable.comment_scroll_down);
                    }else{
                        params.height = imageHeight;
                        storePictureLayout.setLayoutParams(params);
                        storeScroll.setImageResource(R.drawable.comment_scroll_up);
                    }
                    break;
                case UPDATE_MEMBER:
                    isFavorite = !isFavorite;
                    supportInvalidateOptionsMenu();
                    if(isFavorite) {
                        Toast.makeText(context, getResources().getString(R.string.addSuc), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, getResources().getString(R.string.removeSuc), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UPDATE_MEMBER_FAIL:
                    if(isFavorite){
                        Toast.makeText(context, getResources().getString(R.string.removeFail), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, getResources().getString(R.string.addFail), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ADD_APPEAL:
                    Toast.makeText(context, getResources().getString(R.string.appelaSuc), Toast.LENGTH_SHORT).show();
                    break;
                case ADD_APPEAL_FAIL:
                    Toast.makeText(context, getResources().getString(R.string.appelaFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    }


    public int getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return  dm.widthPixels ;
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
                editComment.putNote(commentEditText.getText().toString());
                commentEditText.setText("");
                Message msg = new Message();
                msg.what = EDIT_COMMENT;
                Bundle commentInfo = new Bundle();
                commentInfo.putSerializable("CommentEdit",editComment);
                msg.setData(commentInfo);
                CmtHandler.sendMessage(msg);
            }else if(commentMode == EDIT_REPLY){
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

    private class Handler_A extends Handler{
        public Handler_A(Looper looper){super(looper);}

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_BITMAP:
                    //storePhotoBitmapArray.clear();
                    pageList.clear();
                    storePhotoURLArray = transformStorePictureURL(storeInfoBundle.getStore().getStoreIconURL(),storeInfoBundle.getStore().getPhoto());
                    ArrayList<String> storePhotoMarkArray = transformStorePictureMark(storeInfoBundle.getStore().getPhotoText());
                    for(int i=0;i<storePhotoURLArray.size();i++)
                        pageList.add(new StoreImagePageView(CheckStoreInfo.this,getBitmapFromURL(storePhotoURLArray.get(i)),getScreenWidth(),storePhotoMarkArray.get(i)));
                        //storePhotoBitmapArray.add(getBitmapFromURL(storePhotoURLArray.get(i)));
                        //storePhotoBitmap = getBitmapFromURL(storeInfoBundle.getStore().getFirstPhoto());
                    mainHandler.sendEmptyMessage(UPDATE_UI);
                    break;
            }
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
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
                    Date curDate = new Date(System.currentTimeMillis()) ; // 獲取當前時間
                    String timeString = formatter.format(curDate);
                    Comment newComment = new Comment(msg.getData().getString("CommentID"),userInfo.getMember().getEmail()/*"john8654john@gmail.com"*/,CheckStoreInfo.this.storeInfoBundle.getStore().getID(), msg.getData().getString("Score"),"123",timeString,"", msg.getData().getString("Note"));
                    databaseForComment.addComment(newComment);
                    CmtHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CmtHandler.sendEmptyMessage(UPDATE_ARRAYLIST);
                        }
                    }, 500);
                    break;
                case DELETE_COMMENT:
                    databaseForComment.deleteComment(msg.getData().getString("CommentID"));
                    CmtHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CmtHandler.sendEmptyMessage(UPDATE_ARRAYLIST);
                        }
                    }, 500);
                    break;
                case EDIT_COMMENT:
                    databaseForComment.updateComment((Comment)msg.getData().getSerializable("CommentEdit"));
                    CmtHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CmtHandler.sendEmptyMessage(UPDATE_ARRAYLIST);
                        }
                    }, 500);
                    break;
                case EDIT_REPLY:
                    databaseForComment.updateComment((Comment)msg.getData().getSerializable("CommentEdit"));
                    CmtHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CmtHandler.sendEmptyMessage(UPDATE_ARRAYLIST);
                        }
                    }, 500);
                    break;
                case UPDATE_ARRAYLIST:
                    commentArrayList.clear();
                    database.refreshCommentIndex();
                    commentlist = database.getComment("Store",CheckStoreInfo.this.storeInfoBundle.getStore().getID());
                    for(int i=0;i<commentlist.length;i++){
                        commentArrayList.add(commentlist[i]);
                        Member member = database.GetSingleMember(commentlist[i].getMember());
                        commentArrayList.get(i).setMemberNickName(member.getNickname());
                        commentArrayList.get(i).putMemberInfo(member);
                    }
                    mainHandler.sendEmptyMessage(UPDATE_COMMENT);
                    break;


            }

            super.handleMessage(msg);
        }
    }



    private class RecyclerListItemHandler implements CommentListViewRecyclerAdapter.OnItemClickListener{
        AlertDialog.Builder askForCommentScore = new AlertDialog.Builder(CheckStoreInfo.this);
        @Override
        public void onItemClick(View view, int position) {
            final Comment selectComment = commentArrayList.get(position);
            final ArrayList<String> selections = new ArrayList<String>();
            if(userInfo.getIdentity()!=1&&userInfo.getIdentity()!=0) {
                selections.add("關於" + selectComment.getMemberNickName());
            }
            if(userInfo.getIdentity()==3){
                selections.add("回覆");
                selections.add("編輯");
                selections.add("檢舉");
                selections.add("刪除");
            }
            if(userInfo.getIdentity()==2&&userInfo.getStore().getID().equals(storeInfoBundle.getStore().getID()))
                selections.add("回覆");
            if(userInfo.getIdentity()==1&&userInfo.getMember().getEmail().equals(selectComment.getMember())){
                selections.add("刪除");
                selections.add("編輯");
            }
            if(userInfo.getIdentity()==1){
                selections.add("檢舉");
            }
            askForCommentScore.setTitle(selectComment.getMemberNickName()+"的評論");
            askForCommentScore.setItems( selections.toArray(new String[0]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(selections.get(which).equals("刪除")){
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
                    else if(selections.get(which).equals("檢舉")){
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.alertdialog_report, null);
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                        builder.setTitle(getResources().getString(R.string.appelaComment));
                        builder.setView(view);
                        final EditText titleEditText = view.findViewById(R.id.titleEditText);
                        final EditText contentEditText = view.findViewById(R.id.contentEditText);
                        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton(getResources().getString(R.string.check),null);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            Appeal appeal;
                            @Override
                            public void onClick(View view) {
                                if(titleEditText.getText().toString().equals("")){
                                    Toast.makeText(context, getResources().getString(R.string.pleaseEnterTitle), Toast.LENGTH_SHORT).show();
                                }else if(contentEditText.getText().toString().equals("")){
                                    Toast.makeText(context, getResources().getString(R.string.pleaseEnterContent), Toast.LENGTH_SHORT).show();
                                }else{
                                    class AddAppeal implements Runnable{
                                        @Override
                                        public void run() {
                                            Database d = new Database();
                                            if(d.AddAppeal(appeal).equals("Successful.")){
                                                mainHandler.sendEmptyMessage(ADD_APPEAL);
                                            }else{
                                                mainHandler.sendEmptyMessage(ADD_APPEAL_FAIL);
                                            }
                                        }
                                    }
                                    appeal = new Appeal();
                                    appeal.putDeclarant(userInfo.getMember().getEmail());
                                    appeal.putAppealed(selectComment.getID());
                                    appeal.putTitle(titleEditText.getText().toString());
                                    appeal.putContent(contentEditText.getText().toString());
                                    appeal.putType("3");
                                    Thread t = new Thread(new AddAppeal());
                                    t.start();
                                    alertDialog.dismiss();
                                }
                            }
                        });
                    }else if (selections.get(which).equals("關於"+selectComment.getMemberNickName())){
                        Intent intent = new Intent();
                        intent.setClass(CheckStoreInfo.this,CheckMemberActivity.class);
                        intent.putExtra("MEMBER_INFO",selectComment.getMemberInfo());
                        intent.putExtra("USER_INFO",userInfo);
                        startActivity(intent);
                    }

                }
            });
            AlertDialog dialog = askForCommentScore.create();
            dialog.show();



        }

    }

    private ArrayList<String> transformStorePictureURL(String storeIconURL,String transformURL) {
        String remainString = transformURL;
        ArrayList<String> URLArray = new ArrayList<String>();
        if(transformURL.indexOf(",")<0)
            URLArray.add(storeIconURL+transformURL);
        else {
            while(remainString.indexOf(",")>=0) {
                URLArray.add(storeIconURL+remainString.substring(0,remainString.indexOf(",")));
                remainString = remainString.substring(remainString.indexOf(",")+1);
            }
            URLArray.add(storeIconURL+remainString);
        }
        return URLArray;
    }
    private ArrayList<String> transformStorePictureMark(String transformMark) {
        String remainString = transformMark;
        ArrayList<String> markArray = new ArrayList<String>();
        if(transformMark.indexOf(",")<0)
            markArray.add(transformMark);
        else {
            while(remainString.indexOf(",")>=0) {
                markArray.add(remainString.substring(0,remainString.indexOf(",")));
                remainString = remainString.substring(remainString.indexOf(",")+1);
            }
            markArray.add(remainString);
        }
        return markArray;
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
        CheckStoreInfo.this.setResult(RESULT_OK,intent);
        super.finish();
    }
}

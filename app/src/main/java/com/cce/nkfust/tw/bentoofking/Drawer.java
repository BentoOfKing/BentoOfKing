package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Drawer extends AppCompatActivity{
    private static String passUserInfo = "USER_INFO";
    Context context;
    Toolbar toolbar;
    UserInfo userInfo;
    DrawerLayout drawerLayout;
    void init(Context context, Toolbar toolbar, ListView drawerListView, DrawerLayout drawerLayout,UserInfo userInfo){
        this.userInfo = userInfo;
        this.context = context;
        this.toolbar = toolbar;
        this.drawerLayout = drawerLayout;
        toolbar.setTitle("便當王");
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle((Activity) context,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(context,R.color.white));
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        switch (userInfo.getIdentity()){
            case 1:
                String[] memberMenuItem ={context.getResources().getString(R.string.hello)+userInfo.getMember().getNickname(),context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.favorite),context.getResources().getString(R.string.addStore),context.getResources().getString(R.string.findOrders),context.getResources().getString(R.string.edidInfo),context.getResources().getString(R.string.about),context.getResources().getString(R.string.logout)};
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,memberMenuItem));
                drawerListView.setOnItemClickListener(memberDrawerListener);
                break;
            case 2:
                String[] storeMenuItem ={context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.lookStore),context.getResources().getString(R.string.editStore),context.getResources().getString(R.string.lookOpponent),context.getResources().getString(R.string.addpush),context.getResources().getString(R.string.storedPoint),context.getResources().getString(R.string.about),context.getResources().getString(R.string.logout)};
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,storeMenuItem));
                drawerListView.setOnItemClickListener(storeDrawerListener);
                break;
            case 3:
                String[] adminMenuItem ={context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.reviewStore),context.getResources().getString(R.string.logout)};
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,adminMenuItem));
                drawerListView.setOnItemClickListener(adminDrawerListener);
                break;
            default:
                String[] visitorMenuItem = {context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.memberLogin),context.getResources().getString(R.string.storeLogin),context.getResources().getString(R.string.register),context.getResources().getString(R.string.becomeStore),context.getResources().getString(R.string.about)};
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,visitorMenuItem));
                drawerListView.setOnItemClickListener(visitorDrawerListener);
                break;
        }




    }
    private ListView.OnItemClickListener visitorDrawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    //主頁
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1:
                    //會員登入
                    intent.setClass(context,LoginActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 2:
                    //店家登入
                    intent.setClass(context,StoreLoginActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 3:
                    //註冊
                    intent.setClass(context,RegisterActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 4:
                    //成為店家
                    intent.setClass(context,BecomeStoreActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 5:
                    //關於
                    intent.setClass(context,AboutActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }}, 500);

        }
    };
    private ListView.OnItemClickListener memberDrawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    break;
                case 1:
                    //主頁
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 2://我的最愛

                    break;
                case 3://新增店家
                    intent.setClass(context,CommentAddStore.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 4://查看訂單

                    break;
                case 5://修改資料
                    intent.setClass(context,EditMember.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 6:
                    intent.setClass(context,AboutActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }}, 500);
        }
    };
    private ListView.OnItemClickListener storeDrawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    //主頁
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1://查看店家
                    intent.setClass(context,CheckStoreInfo.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 2://編輯店家
                    intent.setClass(context,EditStoreActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;


                case 3://查看對手

                    break;
                case 4://新增推播

                    break;
                case 5://儲值點數

                    break;
                case 6:
                    intent.setClass(context,AboutActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }}, 500);
        }
    };
    private ListView.OnItemClickListener adminDrawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    //主頁
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1:
                    //審核店家

                    break;
                default:
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }}, 500);
        }
    };
    public void setToolbarNavigation(){
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
    }

}

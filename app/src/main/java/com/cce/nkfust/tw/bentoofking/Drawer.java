package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    Context context;
    Toolbar toolbar;
    UserInfo userInfo;
    int identity;
    void init(Context context, Toolbar toolbar, ListView drawerListView, DrawerLayout drawerLayout){
        String[] visitorMenuItem = {context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.memberLogin),context.getResources().getString(R.string.storeLogin),context.getResources().getString(R.string.register),context.getResources().getString(R.string.becomeStore),context.getResources().getString(R.string.about)};
        String[] memberMenuItem ={context.getResources().getString(R.string.hello)+userInfo.getMember().getNickname(),context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.logout)};
        String[] storeMenuItem ={context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.logout)};
        String[] adminMenuItem ={context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.logout)};
        this.context = context;
        this.toolbar = toolbar;
        toolbar.setTitle("便當王");
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle((Activity) context,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(context,R.color.white));
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        switch (identity){
            case 1:
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,memberMenuItem));
                drawerListView.setOnItemClickListener(memberDrawerListener);
                break;
            case 2:
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,storeMenuItem));
                drawerListView.setOnItemClickListener(storeDrawerListener);
                break;
            case 3:
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,adminMenuItem));
                drawerListView.setOnItemClickListener(adminDrawerListener);
                break;
            default:
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
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1:
                    //會員登入
                    intent.setClass(context,LoginActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:
                    //店家登入
                    intent.setClass(context,StoreLoginActivity.class);
                    context.startActivity(intent);
                    break;
                case 3:
                    //註冊
                    intent.setClass(context,RegisterActivity.class);
                    context.startActivity(intent);
                    break;
                case 4:
                    //成為店家
                    intent.setClass(context,BecomeStoreActivity.class);
                    context.startActivity(intent);
                    break;
                case 5:
                    //關於
                    break;
                default:
                    break;
            }
        }
    };
    private ListView.OnItemClickListener memberDrawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    //主頁
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1:
                    //會員登入
                    //intent.setClass(context,LoginActivity.class);
                    //context.startActivity(intent);
                    break;
                case 2:
                    //店家登入
                    //intent.setClass(context,StoreLoginActivity.class);
                    //context.startActivity(intent);
                    break;
                case 3:
                    //註冊
                    //intent.setClass(context,RegisterActivity.class);
                    //context.startActivity(intent);
                    break;
                case 4:
                    //成為店家
                    //intent.setClass(context,BecomeStoreActivity.class);
                    //context.startActivity(intent);
                    break;
                case 5:
                    //關於
                    break;
                default:
                    break;
            }
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
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1:
                    //會員登入
                    //intent.setClass(context,LoginActivity.class);
                    //context.startActivity(intent);
                    break;
                case 2:
                    //店家登入
                    //intent.setClass(context,StoreLoginActivity.class);
                    //context.startActivity(intent);
                    break;
                case 3:
                    //註冊
                    //intent.setClass(context,RegisterActivity.class);
                    //context.startActivity(intent);
                    break;
                case 4:
                    //成為店家
                    //intent.setClass(context,BecomeStoreActivity.class);
                    //context.startActivity(intent);
                    break;
                case 5:
                    //關於
                    break;
                default:
                    break;
            }
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
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;
                case 1:
                    //會員登入
                    //intent.setClass(context,LoginActivity.class);
                    //context.startActivity(intent);
                    break;
                case 2:
                    //店家登入
                    //intent.setClass(context,StoreLoginActivity.class);
                    //context.startActivity(intent);
                    break;
                case 3:
                    //註冊
                    //intent.setClass(context,RegisterActivity.class);
                    //context.startActivity(intent);
                    break;
                case 4:
                    //成為店家
                    //intent.setClass(context,BecomeStoreActivity.class);
                    //context.startActivity(intent);
                    break;
                case 5:
                    //關於
                    break;
                default:
                    break;
            }
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

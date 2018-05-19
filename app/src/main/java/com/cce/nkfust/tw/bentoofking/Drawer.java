package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import okhttp3.internal.framed.FrameReader;

public class Drawer extends AppCompatActivity{
    private ProgressDialog progressDialog;
    private static final int ADD_APPEAL = 3;
    private static final int ADD_APPEAL_FAIL = 4;
    private static String passUserInfo = "USER_INFO";
    private static String passMemberInfo = "MEMBER_INFO";
    Member member;
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
                String[] memberMenuItem ={context.getResources().getString(R.string.hello)+userInfo.getMember().getNickname(),context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.favorite),context.getResources().getString(R.string.addStore),context.getResources().getString(R.string.findOrders),context.getResources().getString(R.string.edidInfo),context.getResources().getString(R.string.appeal),context.getResources().getString(R.string.about),context.getResources().getString(R.string.logout)};
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,memberMenuItem));
                drawerListView.setOnItemClickListener(memberDrawerListener);
                break;
            case 2:
                String[] storeMenuItem ={context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.lookStore),context.getResources().getString(R.string.editStore),context.getResources().getString(R.string.editMenu),context.getResources().getString(R.string.editPhoto),context.getResources().getString(R.string.lookOpponent),context.getResources().getString(R.string.pushmanage),context.getResources().getString(R.string.checkError),context.getResources().getString(R.string.appeal),context.getResources().getString(R.string.about),context.getResources().getString(R.string.logout)};
                drawerListView.setAdapter(new ArrayAdapter<String>(context,R.layout.drawer_list_item,storeMenuItem));
                drawerListView.setOnItemClickListener(storeDrawerListener);
                break;
            case 3:
                String[] adminMenuItem ={context.getResources().getString(R.string.mainActivity),context.getResources().getString(R.string.banMember),context.getResources().getString(R.string.reviewStore),context.getResources().getString(R.string.storeError),context.getResources().getString(R.string.reportComment),context.getResources().getString(R.string.previewPush),context.getResources().getString(R.string.memberAppeal),context.getResources().getString(R.string.storeAppeal),context.getResources().getString(R.string.logout)};
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
                    intent.setClass(context,CheckMemberActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    intent.putExtra(passMemberInfo,userInfo.getMember());
                    context.startActivity(intent);
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
                    intent.setClass(context,MyFavoriteStore.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
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
                    //申訴
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View v = inflater.inflate(R.layout.alertdialog_report, null);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                    builder.setTitle("申訴");
                    builder.setView(v);
                    final EditText titleEditText = v.findViewById(R.id.titleEditText);
                    final EditText contentEditText = v.findViewById(R.id.contentEditText);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("送出",null);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        Appeal appeal;
                        Boolean suc = false;
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
                                            suc = true;
                                        }
                                    }
                                }
                                appeal = new Appeal();
                                appeal.putDeclarant(userInfo.getMember().getEmail());
                                appeal.putAppealed("");
                                appeal.putTitle(titleEditText.getText().toString());
                                appeal.putContent(contentEditText.getText().toString());
                                appeal.putType("0");
                                Thread t = new Thread(new AddAppeal());
                                progressDialog = ProgressDialog.show(context, "請稍等...", "申訴發送中...", true);
                                t.start();
                                try {
                                    t.join();
                                    progressDialog.dismiss();
                                    if(suc) {
                                        Toast.makeText(context, "申訴成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "申訴失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "申訴失敗", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();
                            }
                        }
                    });
                    break;
                case 7:
                    intent.setClass(context,AboutActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                        intent.setClass(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(passUserInfo,userInfo);
                        userInfo.putStore(null);
                        userInfo.putAdmin(null);
                        userInfo.putMember(null);
                        userInfo.setIdentity(4);
                        context.startActivity(intent);
                        ((Activity) context).finish();
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
                    intent.putExtra("storeInfo",userInfo);
                    context.startActivity(intent);
                    break;
                case 2://編輯店家
                    intent.setClass(context,EditStoreActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 3://編輯菜單
                    intent.setClass(context,EditExistedMenuActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 4://編輯照片
                    intent.setClass(context,EditExistedPhotoActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 5://查看對手
                    intent.setClass(context,CheckOpponentList.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 6://新增推播
                    intent.setClass(context,PushManageActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 7://查看錯誤
                    intent.setClass(context,StoreErrorActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 8:
                    //申訴
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View v = inflater.inflate(R.layout.alertdialog_report, null);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                    builder.setTitle("申訴");
                    builder.setView(v);
                    final EditText titleEditText = v.findViewById(R.id.titleEditText);
                    final EditText contentEditText = v.findViewById(R.id.contentEditText);
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("送出",null);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        Appeal appeal;
                        Boolean suc = false;
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
                                            suc = true;
                                        }
                                    }
                                }
                                appeal = new Appeal();
                                appeal.putDeclarant(userInfo.getStore().getID());
                                appeal.putAppealed("");
                                appeal.putTitle(titleEditText.getText().toString());
                                appeal.putContent(contentEditText.getText().toString());
                                appeal.putType("1");
                                Thread t = new Thread(new AddAppeal());
                                progressDialog = ProgressDialog.show(context, "請稍等...", "申訴發送中...", true);
                                t.start();
                                try {
                                    t.join();
                                    progressDialog.dismiss();
                                    if(suc) {
                                        Toast.makeText(context, "申訴成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "申訴失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "申訴失敗", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();
                            }
                        }
                    });
                    break;
                case 9:
                    intent.setClass(context,AboutActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    userInfo.putStore(null);
                    userInfo.putAdmin(null);
                    userInfo.putMember(null);
                    userInfo.setIdentity(4);
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
                    //停權會員
                    intent.setClass(context,BanedMemberActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 2:
                    //審核店家
                    intent.setClass(context,ReviewStoreActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 3:
                    //店家錯誤
                    intent.setClass(context,StoreErrorActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 4:
                    //檢舉評論
                    intent.setClass(context,AppealCommentActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 5:
                    //審核推播
                    intent.setClass(context,ReviewPushActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 6:
                    //會員申訴
                    intent.setClass(context,MemberAppealActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                case 7:
                    //店家申訴
                    intent.setClass(context,StoreAppealActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    break;
                default:
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    userInfo.putStore(null);
                    userInfo.putAdmin(null);
                    userInfo.putMember(null);
                    userInfo.setIdentity(4);
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

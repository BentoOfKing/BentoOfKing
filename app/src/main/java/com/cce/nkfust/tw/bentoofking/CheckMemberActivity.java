package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckMemberActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passMemberInfo = "MEMBER_INFO";
    private static final int SUCCESSED = 66;
    private static final int FAIL = 69;
    private static final int GET_SUCCESSED = 1;
    private static final int GET_FAIL = 2;
    private UserInfo userInfo;
    private Member member;
    private Toolbar toolbar;
    private ListView drawerListView;
    private TextView emailTextView;
    private TextView sexTextView;
    private TextView nicknameTextView;
    private TextView pointTextView;
    private TextView stateTextView;
    private DrawerLayout drawerLayout;
    private ProgressDialog progressDialog;
    private Context context;
    private MainThreadHandler mainThreadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        member = (Member) intent.getSerializableExtra(passMemberInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.checkMember));
        emailTextView = findViewById(R.id.emailTextView);
        sexTextView = findViewById(R.id.sexTextView);
        nicknameTextView = findViewById(R.id.nicknameTextView);
        pointTextView = findViewById(R.id.pointTextView);
        stateTextView = findViewById(R.id.stateTextView);
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
        mainThreadHandler = new MainThreadHandler();
        Thread thread = new Thread(new GetMember());
        thread.start();
    }
    class GetMember implements Runnable{
        @Override
        public void run() {
            Database d = new Database();
            member = d.GetSingleMember(member.getEmail());
            if(member == null){
                mainThreadHandler.sendEmptyMessage(GET_FAIL);
            }else{
                mainThreadHandler.sendEmptyMessage(GET_SUCCESSED);
            }
        }
    }
    public class MainThreadHandler extends Handler {
        public MainThreadHandler() {
            super();
        }
        public MainThreadHandler(Looper looper) {
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESSED:
                    progressDialog.dismiss();
                    finish();
                    break;
                case FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.connectError), Toast.LENGTH_SHORT).show();
                    break;
                case GET_SUCCESSED:
                    emailTextView.setText(member.getEmail());
                    if(member.getSex().equals("0")){
                        sexTextView.setText("女");
                    }else{
                        sexTextView.setText("男");
                    }
                    nicknameTextView.setText(member.getNickname());
                    pointTextView.setText(member.getPoint());
                    if(member.getState().equals("0")){
                        stateTextView.setText("正常");
                    }else{
                        stateTextView.setText("停權中");
                    }
                    progressDialog.dismiss();
                    break;
                case GET_FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.connectError), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }
}

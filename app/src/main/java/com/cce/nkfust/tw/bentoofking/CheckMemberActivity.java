package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
    private Boolean banFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member);
        banFlag = false;
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
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        banFlag = true;
                        Thread t = new Thread(new UpdateMember());
                        progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
                        t.start();
                        supportInvalidateOptionsMenu();
                        break;
                    case R.id.item2:
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View view = inflater.inflate(R.layout.alertdialog_note, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(getResources().getString(R.string.note));
                        builder.setView(view);
                        final EditText editText = view.findViewById(R.id.noteEditText);
                        editText.setText(member.getNote());
                        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                member.putNote(editText.getText().toString());
                                Thread t = new Thread(new UpdateMember());
                                progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
                                t.start();
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                }
                return false;
            }
        });
        emailTextView = findViewById(R.id.emailTextView);
        sexTextView = findViewById(R.id.sexTextView);
        nicknameTextView = findViewById(R.id.nicknameTextView);
        pointTextView = findViewById(R.id.pointTextView);
        stateTextView = findViewById(R.id.stateTextView);
        emailTextView.setText(member.getEmail());
        if(member.getSex().equals("0")){
            sexTextView.setText("女");
        }else{
            sexTextView.setText("男");
        }
        nicknameTextView.setText(member.getNickname());
        pointTextView.setText(member.getPoint());
        updateState();
        mainThreadHandler = new MainThreadHandler();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_more, menu);
        MenuItem item1 = menu.findItem(R.id.item1);
        MenuItem item2 = menu.findItem(R.id.item2);
        MenuItem item3 = menu.findItem(R.id.item3);
        if(userInfo.getIdentity()==3){
            if(member.getState().equals("")){
                item1.setTitle(getResources().getString(R.string.ban));
            }else{
                item1.setTitle(getResources().getString(R.string.banCancle));
            }
            item2.setTitle(getResources().getString(R.string.note));
            item3.setVisible(false);
        }else{
            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);
        }
        return true;
    }
    class UpdateMember implements Runnable{
        @Override
        public void run() {
            Database d = new Database();
            if(banFlag){
                if(member.getState().equals("")){
                    member.putState("1");
                }else{
                    member.putState("");
                }
            }
            if(d.UpdateMember(member).equals("Successful.")){
                mainThreadHandler.sendEmptyMessage(SUCCESSED);
            }else{
                mainThreadHandler.sendEmptyMessage(FAIL);
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
                    banFlag = false;
                    updateState();
                    progressDialog.dismiss();
                    break;
                case FAIL:
                    if(banFlag) {
                        if (member.getState().equals("")) {
                            member.putState("1");

                        } else {
                            member.putState("");
                        }
                    }
                    updateState();
                    banFlag = false;
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.connectError), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    void updateState(){
        if(member.getState().equals("")){
            stateTextView.setText("正常");
        }else{
            stateTextView.setText("停權中");
        }
    }
}

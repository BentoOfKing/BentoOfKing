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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckStoreErrorActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passAppealInfo = "APPEAL_INFO";
    private static String passMemberInfo = "MEMBER_INFO";
    private static final int SUCCESSED = 66;
    private static final int FAIL = 69;
    private static final int GET_SUCCESSED = 1;
    private static final int GET_FAIL = 2;
    private UserInfo userInfo;
    private Appeal appeal;
    private Store store;
    private Member member;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private TextView titleTextView;
    private TextView declarantTextView;
    private TextView appealedTextView;
    private TextView contentTextView;
    private EditText noteEditText;
    private Button saveButton;
    private Button completeButton;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_store_error);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        appeal = (Appeal) intent.getSerializableExtra(passAppealInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.checkStoreError));
        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(appeal.getTitle());
        declarantTextView = findViewById(R.id.declarantTextView);
        appealedTextView = findViewById(R.id.appealedTextView);
        contentTextView = findViewById(R.id.contentTextView);
        contentTextView.setText(appeal.getContent());
        noteEditText = findViewById(R.id.noteEditText);
        noteEditText.setText(appeal.getNote());
        saveButton = findViewById(R.id.saveButton);
        completeButton = findViewById(R.id.completeButton);
        OnClickHandler onClickHandler = new OnClickHandler();
        declarantTextView.setOnClickListener(onClickHandler);
        appealedTextView.setOnClickListener(onClickHandler);
        saveButton.setOnClickListener(onClickHandler);
        completeButton.setOnClickListener(onClickHandler);
        mainThreadHandler = new MainThreadHandler();
    }
    class OnClickHandler implements View.OnClickListener{
        Intent intent = new Intent();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.declarantTextView:
                    intent.setClass(context,CheckMemberActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    intent.putExtra(passMemberInfo,member);
                    startActivity(intent);
                    break;
                case R.id.appealedTextView:
                    intent.setClass(context,CheckStoreInfo.class);
                    intent.putExtra(passUserInfo,userInfo);
                    UserInfo tmp = userInfo;
                    tmp.putStore(store);
                    intent.putExtra("storeInfo",tmp);
                    startActivity(intent);
                    break;
                case R.id.saveButton:
                    appeal.putNote(noteEditText.getText().toString());
                    Thread t1 = new Thread(new UpdateAppeal());
                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
                    t1.start();
                    break;
                case R.id.completeButton:
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View v = inflater.inflate(R.layout.alertdialog_note, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getResources().getString(R.string.enterDealResult));
                    builder.setView(v);
                    final EditText editText = v.findViewById(R.id.noteEditText);
                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appeal.putResult(editText.getText().toString());
                            if(appeal.getResult().equals("")){
                                appeal.putResult("OK");
                            }
                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
                            Thread t = new Thread(new UpdateAppeal());
                            t.start();
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
            }
        }
    }
    class GetStore implements Runnable{
        @Override
        public void run() {
            Database d = new Database();
            if(d.UpdateAppeal(appeal).equals("Successful.")){
                mainThreadHandler.sendEmptyMessage(SUCCESSED);
            }else{
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }
    class UpdateAppeal implements Runnable{
        @Override
        public void run() {
            Database d = new Database();
            if(d.UpdateAppeal(appeal).equals("Successful.")){
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
                    progressDialog.dismiss();
                    finish();
                    break;
                case FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.connectError), Toast.LENGTH_SHORT).show();
                    break;
                case GET_SUCCESSED:
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
    public class GetStoreAndMember implements Runnable{
        @Override
        public void run() {
            Database d = new Database();
            store = d.GetSingleStore(appeal.getAppealed());
            if(store == null){
                mainThreadHandler.sendEmptyMessage(GET_FAIL);
                return;
            }
            member = d.GetSingleMember(appeal.getDeclarant());
            if(member == null){
                mainThreadHandler.sendEmptyMessage(GET_FAIL);
                return;
            }
            mainThreadHandler.sendEmptyMessage(GET_SUCCESSED);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
        Thread t = new Thread(new GetStoreAndMember());
        t.start();
    }
}

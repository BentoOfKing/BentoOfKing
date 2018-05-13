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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CheckAppealCommentActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passAppealInfo = "APPEAL_INFO";
    private static String passMemberInfo = "MEMBER_INFO";
    private static final int SUCCESSED = 66;
    private static final int FAIL = 69;
    private static final int GET_SUCCESSED = 1;
    private static final int GET_FAIL = 2;
    private UserInfo userInfo;
    private Appeal appeal;
    private Comment comment;
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
        setContentView(R.layout.activity_check_appeal_comment);
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
        toolbar.setTitle(getResources().getString(R.string.appelaComment));
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
        LayoutInflater inflater;
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        View v;
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
                    inflater = LayoutInflater.from(context);
                    v = inflater.inflate(R.layout.item_comment_listitem, null);
                    builder = new AlertDialog.Builder(context);
                    TextView commentMember = v.findViewById(R.id.commentMember);

                    builder.setTitle(getResources().getString(R.string.checkComment));
                    builder.setView(v);
                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //缺更新資料
                            dialog.dismiss();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case R.id.saveButton:
                    appeal.putNote(noteEditText.getText().toString());
                    Thread t1 = new Thread(new UpdateAppeal());
                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料連接中...", true);
                    t1.start();
                    break;
                case R.id.completeButton:
                    inflater = LayoutInflater.from(context);
                    v = inflater.inflate(R.layout.alertdialog_note, null);
                    builder = new AlertDialog.Builder(context);
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
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
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
    public class GetMemberAndComment implements Runnable{
        @Override
        public void run() {
            Database d = new Database();
            comment = d.GetSingleComment(appeal.getAppealed());
            if(comment == null){
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
        Thread t = new Thread(new GetMemberAndComment());
        t.start();
    }
}

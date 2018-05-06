package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;

public class EditMember extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private String registerResult;
    private Member memberRegister;
    private Comment editComment;
    private Database database;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private TextView emailTextView;
    private EditText passwordEditText;
    private EditText passwordCheckEditText;
    private EditText OldpasswordCheckEditText;
    private EditText nicknameEditText;
    private Button registerButton;
    private TextView Sex;

    private HandlerThread RegisterThread;
    private Handler RegisterThreadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_edit_member);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.edidInfo));
        emailTextView = findViewById(R.id.emailTextView);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
        OldpasswordCheckEditText = findViewById(R.id.OldpasswordCheckEditText);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        registerButton = findViewById(R.id.registerButton);
        Sex = findViewById(R.id.Sex1);
        RegisterButtonHandler registerButtonHandler = new RegisterButtonHandler();
        registerButton.setOnClickListener(registerButtonHandler);
        emailTextView.setText(userInfo.getMember().getEmail());
        if (userInfo.getMember().getSex().equals("1")) {
            Sex.setText("男");
        }
        else{
            Sex.setText("女");
        }
        nicknameEditText.setText(userInfo.getMember().getNickname());
        Toast.makeText(EditMember.this,"若要該改密碼請填新舊密碼",Toast.LENGTH_SHORT).show();
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
    public class RegisterButtonHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            RegisterConfirm registerConfirm = new RegisterConfirm();
            RegisterThread = new HandlerThread("Register1");
            RegisterThread.start();
            RegisterThreadHandler = new Handler(RegisterThread.getLooper());
            RegisterThreadHandler.post(registerConfirm);
            //多執行緒監聽須修正
            //缺根據回傳結果registerResult所對應之動作（回傳結果:"Successful.","An error occurred.","Fail."）
        }
    }
    public class RegisterConfirm implements Runnable{
        @Override
        public void run() {
            if(TextUtils.isEmpty(OldpasswordCheckEditText.getText().toString())){
                Toast.makeText(EditMember.this,"請填寫舊密碼",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!userInfo.getMember().getPassword().equals(OldpasswordCheckEditText.getText().toString())){
                Toast.makeText(EditMember.this,"舊密碼錯誤",Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if(TextUtils.isEmpty(nicknameEditText.getText().toString())){
                    Toast.makeText(EditMember.this,"請輸入暱稱",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if(TextUtils.isEmpty(passwordEditText.getText().toString())&&TextUtils.isEmpty(passwordCheckEditText.getText().toString())){
                        userInfo.getMember().putNickname(nicknameEditText.getText().toString());
                        database = new Database();
                        database.UpdateMember(userInfo.getMember());
                        Toast toast = Toast.makeText(EditMember.this,
                                "修改成功", Toast.LENGTH_SHORT);
                        toast.show();
                        userInfo.putMember(userInfo.getMember());
                        userInfo.setIdentity(1);
                        Intent intent = new Intent();
                        intent.setClass(EditMember.this, MainActivity.class);//有修改
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(passUserInfo, userInfo);
                        startActivity(intent);
                    }
                    else{
                        if(TextUtils.isEmpty(passwordCheckEditText.getText().toString())){
                            Toast.makeText(EditMember.this,"請輸入密碼確認",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(TextUtils.isEmpty(nicknameEditText.getText().toString())){
                            Toast.makeText(EditMember.this,"請輸入暱稱",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())){
                        }
                        else{
                            Toast toast = Toast.makeText(EditMember.this,
                                    "密碼與確認密碼必須一致", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        Handler RegisterUiHandler = new Handler();  //修改
                        DatabaseMemberRegister databaseMemberRegister = new DatabaseMemberRegister();
                        RegisterUiHandler.post(databaseMemberRegister);
                    }
                }
            }
        }
    }
    public class  DatabaseMemberRegister implements Runnable{
        @Override
        public void run() {














            userInfo.getMember().putNickname(nicknameEditText.getText().toString());
            userInfo.getMember().putPassword(passwordEditText.getText().toString());
            database = new Database();
            database.UpdateMember(userInfo.getMember());
            Toast toast = Toast.makeText(EditMember.this,
                    getResources().getString(R.string.EditSuccessful), Toast.LENGTH_SHORT);
            toast.show();


            userInfo.putMember(userInfo.getMember());
            userInfo.setIdentity(1);
            Intent intent = new Intent();
            intent.setClass(EditMember.this, MainActivity.class);//有修改
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(passUserInfo, userInfo);
            startActivity(intent);



        }
    }
}
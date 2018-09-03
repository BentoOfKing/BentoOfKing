package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Member member;
    private Comment editComment;
    private Database database;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private TextView emailTextView;
    private EditText NewPasswordEditText;  //改 原passwordEditText
    private EditText passwordCheckEditText;
    private EditText passwordEditText; //改 原OldpasswordCheckEditText
    private EditText nicknameEditText;
    private Button registerButton;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;

    private RadioButton radio_m;
    private RadioButton radio_f;
    private String sex="";

    private HandlerThread RegisterThread;
    private Handler RegisterThreadHandler;
    private RadioGroup RadioSex;
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
        NewPasswordEditText = findViewById(R.id.NewPasswordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        registerButton = findViewById(R.id.registerButton);
        radio_m = findViewById(R.id.radio_m);
        radio_f = findViewById(R.id.radio_f);
        RegisterButtonHandler registerButtonHandler = new RegisterButtonHandler();
        registerButton.setOnClickListener(registerButtonHandler);
        emailTextView.setText(userInfo.getMember().getEmail());
        RadioSex = findViewById(R.id.RadioSex);
        if(userInfo.getMember().getSex().equals("1")){
            radio_m.setChecked(true);
        }
        else {
            radio_f.setChecked(true);
        }


        nicknameEditText.setText(userInfo.getMember().getNickname());

       /* nicknameEditText.setText(userInfo.getMember().getNickname());
        Toast.makeText(EditMember.this,"若要該改密碼請填新舊密碼",Toast.LENGTH_SHORT).show();
        */
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
            progressDialog = ProgressDialog.show(EditMember.this, "請稍等...", "資料更新中...", true);
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
            if(RadioSex.getCheckedRadioButtonId() == R.id.radio_f){
                sex="0";
            }
            else if(RadioSex.getCheckedRadioButtonId() == R.id.radio_m){
                sex="1";
            }

            if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                Toast.makeText(EditMember.this,"請填寫密碼",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if(!userInfo.getMember().getPassword().equals(passwordEditText.getText().toString())){
                Toast.makeText(EditMember.this,"密碼錯誤",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            if(RadioSex.getCheckedRadioButtonId() != R.id.radio_f && RadioSex.getCheckedRadioButtonId() != R.id.radio_m){
                Toast.makeText(EditMember.this,"請選擇性別",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            else {
                if(TextUtils.isEmpty(nicknameEditText.getText().toString())){
                    Toast.makeText(EditMember.this,"請輸入暱稱",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
                else{
                    if(TextUtils.isEmpty(NewPasswordEditText.getText().toString())&&TextUtils.isEmpty(passwordCheckEditText.getText().toString())){
                        userInfo.getMember().putNickname(nicknameEditText.getText().toString());
                        userInfo.getMember().putSex(sex);
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
                        progressDialog.dismiss();
                    }
                    else{
                        if(TextUtils.isEmpty(passwordCheckEditText.getText().toString())){
                            Toast.makeText(EditMember.this,"請輸入密碼確認",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        if(TextUtils.isEmpty(nicknameEditText.getText().toString())){
                            Toast.makeText(EditMember.this,"請輸入暱稱",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                        if(NewPasswordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())){
                        }
                        else{
                            Toast toast = Toast.makeText(EditMember.this,
                                    "密碼與確認密碼必須一致", Toast.LENGTH_SHORT);
                            toast.show();
                            progressDialog.dismiss();
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
            userInfo.getMember().putPassword(NewPasswordEditText.getText().toString());
            userInfo.getMember().putSex(sex);
            SharedPreferences LoginRecord = getApplication().
                    getSharedPreferences("LoginRecord", Context.MODE_PRIVATE);
            LoginRecord.edit()
                    .putString("Recordpassword",NewPasswordEditText.getText().toString())
                    .commit();

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
            progressDialog.dismiss();



        }
    }
}
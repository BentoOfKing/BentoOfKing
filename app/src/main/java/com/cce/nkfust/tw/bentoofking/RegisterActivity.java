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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.validation.Validator;

public class RegisterActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private String registerResult;
    private Member memberRegister;
    private Database database;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordCheckEditText;
    private EditText nicknameEditText;
    private RadioButton radio_m;
    private RadioButton radio_f;
    private ImageButton registerButton;

    private HandlerThread RegisterThread;
    private Handler RegisterThreadHandler;
    private RadioGroup RadioSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.register));
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        radio_m = findViewById(R.id.radio_m);
        radio_f = findViewById(R.id.radio_f);
        registerButton = findViewById(R.id.registerButton);
        RegisterButtonHandler registerButtonHandler = new RegisterButtonHandler();
        registerButton.setOnClickListener(registerButtonHandler);

        RadioSex = findViewById(R.id.RadioSex);


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
            //缺判斷是否全部填寫



            if(TextUtils.isEmpty(emailEditText.getText().toString())){
                Toast.makeText(RegisterActivity.this,"請填寫email",Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                int i=0;
                i=emailEditText.getText().toString().indexOf('@');
                if(i==-1){
                    Toast.makeText(RegisterActivity.this,"請填寫正確email格式",Toast.LENGTH_SHORT).show();
                    return;
                }
            }



            if(TextUtils.isEmpty(passwordEditText.getText().toString())){
                Toast.makeText(RegisterActivity.this,"請填寫密碼",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(passwordCheckEditText.getText().toString())){
                Toast.makeText(RegisterActivity.this,"請輸入密碼確認",Toast.LENGTH_SHORT).show();
                return;
            }
            if(RadioSex.getCheckedRadioButtonId() != R.id.radio_f && RadioSex.getCheckedRadioButtonId() != R.id.radio_m){
                Toast.makeText(RegisterActivity.this,"請選擇性別",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(nicknameEditText.getText().toString())){
                Toast.makeText(RegisterActivity.this,"請輸入暱稱",Toast.LENGTH_SHORT).show();
                return;
            }

            if(passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())){

            }
            else{
                Toast toast = Toast.makeText(RegisterActivity.this,
                        "密碼與確認密碼必須一致", Toast.LENGTH_SHORT);
                toast.show();
            }
            //缺判斷是否全部填寫


            Handler RegisterUiHandler = new Handler();  //修改
            DatabaseMemberRegister databaseMemberRegister = new DatabaseMemberRegister();
            RegisterUiHandler.post(databaseMemberRegister);


        }
    }

    public class  DatabaseMemberRegister implements Runnable{
        @Override
        public void run() {


            //SEX判斷
            String sex="";
            if(RadioSex.getCheckedRadioButtonId() == R.id.radio_f){
                sex="0";
            }
            else if(RadioSex.getCheckedRadioButtonId() == R.id.radio_m){
                sex="1";
            }
            //SEX判斷
            memberRegister = new Member(emailEditText.getText().toString(), passwordEditText.getText().toString(), nicknameEditText.getText().toString(), sex, "", "", "");
            database = new Database();
//登入判斷

            int i=0;
            i=emailEditText.getText().toString().indexOf('@');




            if(TextUtils.isEmpty(emailEditText.getText().toString())||  //email為空不能註冊
                    TextUtils.isEmpty(passwordEditText.getText().toString())||  //密碼為空不能註冊
                    TextUtils.isEmpty(passwordCheckEditText.getText().toString())|| //密碼確認為空不能註冊
                    TextUtils.isEmpty(nicknameEditText.getText().toString())||  //暱稱為空不能註冊
                    sex.equals("")||
                    i==-1//性別為空不能註冊
                    ) {
            }

            else {
                if(passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())){  //密碼與確認密碼相同才能註冊
                    registerResult = database.MemberRegister(memberRegister);
                    if(registerResult.equals("An error occurred.")){
                        Toast toast = Toast.makeText(RegisterActivity.this,
                                "帳號已經重複", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else if(registerResult.equals("Successful.")){
                        Toast toast = Toast.makeText(RegisterActivity.this,
                                "註冊成功", Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivity.this, MainActivity.class);//有修改
                        startActivity(intent);

                    }


                }
            }



        }
    }





}
